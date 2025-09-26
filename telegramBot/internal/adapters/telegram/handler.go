package telegram

import (
	"context"
	"github.com/fatemeAfshani/voting/internal/config"
	"strings"

	"github.com/fatemeAfshani/voting/internal/domain"
	log "github.com/fatemeAfshani/voting/internal/infra/logger"
	tgbotapi "github.com/go-telegram-bot-api/telegram-bot-api/v5"
)

type Bot struct {
	config      config.TelegramConfig
	tokens      *domain.TokenStore
	telegramBot *tgbotapi.BotAPI
	messenger   Messenger
	userDomain  *domain.UserDomain
	logger      log.Logger
}

func NewTelegramBot(
	config config.TelegramConfig,
	botApi *tgbotapi.BotAPI,
	tokens *domain.TokenStore,
	userDomain *domain.UserDomain,
	messenger Messenger,
	logger log.Logger,
) *Bot {
	return &Bot{
		config:      config,
		tokens:      tokens,
		userDomain:  userDomain,
		telegramBot: botApi,
		messenger:   messenger,
		logger:      logger,
	}
}

func (bot *Bot) Start(ctx context.Context) {
	logger := bot.logger.With().Str("package", "bot").Str("function", "start").Logger()

	u := tgbotapi.NewUpdate(0)
	u.Timeout = 60

	updates := bot.telegramBot.GetUpdatesChan(u)
	logger.Info().Msg("bot started")

	for update := range updates {
		if update.Message == nil {
			logger.Warn().
				Int("updateId", update.UpdateID).
				Msg("update msg is nil")

			continue
		}

		logger = logger.With().
			Int("updateId", update.UpdateID).
			Int64("chatId", update.Message.Chat.ID).
			Str("text", update.Message.Text).
			Logger()

		chatId := update.Message.Chat.ID
		text := strings.ToLower(update.Message.Text)
		session := bot.tokens.GetOrCreate(chatId)

		logger.Info().Str("state", string(session.State)).Msg("new message received")

		switch session.State {
		case domain.StateNone:
			bot.handleStateNone(chatId, session, text)
		case domain.StateChoosingRole:
			bot.handleChoosingRole(ctx, chatId, session, text)
		case domain.StateAwaitPhone:
			bot.handleAwaitPhone(chatId, session, update.Message.Text)
		case domain.StateAwaitPassword:
			bot.handleAwaitPassword(ctx, chatId, session, update)
		}
	}
}

func (bot *Bot) handleStateNone(chatID int64, session *domain.UserSession, text string) {
	if text == string(OptionStart) {
		session.State = domain.StateChoosingRole
		bot.tokens.Set(chatID, session)
		bot.messenger.SendStep(chatID, StepWelcome)
		return
	}
	bot.messenger.Reply(chatID, bot.messenger.localize(MsgUnknown), false)
}

func (bot *Bot) handleChoosingRole(ctx context.Context, chatID int64, session *domain.UserSession, text string) {
	if opt, ok := bot.messenger.MatchOption(text); ok {
		switch opt {
		case OptionCreator:
			session.Role = domain.Creator
			isSignedIn, _ := bot.userDomain.AutoSignIn(ctx, session)
			if isSignedIn {
				session.State = domain.StateCreatorMenu
			} else {
				session.State = domain.StateAwaitPhone
			}
			bot.tokens.Set(chatID, session)
			if isSignedIn {
				bot.messenger.SendStep(chatID, StepCreatorMenu)
			} else {
				bot.messenger.SendStep(chatID, StepCreatorSignup)
			}

		case OptionVoter:
			session.Role = domain.Voter
			session.State = domain.StateChoosingAuth
			bot.tokens.Set(chatID, session)
			bot.messenger.SendStep(chatID, StepCreatorSignup)
		default:
			bot.messenger.Reply(chatID, bot.messenger.localize(MsgUnknown), false)
		}
		return
	}
	bot.messenger.Reply(chatID, bot.messenger.localize(MsgUnknown), false)
}

func (bot *Bot) handleAwaitPhone(chatID int64, session *domain.UserSession, phone string) {
	session.Phone = phone
	session.State = domain.StateAwaitPassword
	bot.tokens.Set(chatID, session)
	bot.messenger.Reply(chatID, bot.messenger.localize(MsgAskPasswordSignUp), true)
	return
}

func (bot *Bot) handleAwaitPassword(ctx context.Context, chatID int64, session *domain.UserSession, updateData tgbotapi.Update) {
	session.Password = updateData.Message.Text
	session.UserName = updateData.Message.From.FirstName

	isRegistered, err := bot.userDomain.RegisterCreator(ctx, session)
	if err != nil {
		bot.logger.Error().Err(err).Msg("creator registration failed")
	}
	if isRegistered {
		session.State = domain.StateCreatorMenu
	} else {
		session.State = domain.StateNone
	}
	bot.tokens.Set(chatID, session)
	if isRegistered {
		bot.messenger.SendStep(chatID, StepCreatorMenu)
	} else {
		bot.messenger.Reply(chatID, bot.messenger.localize(MsgRegisterError), true)
	}
}

func (bot *Bot) Stop() {
	bot.telegramBot.StopReceivingUpdates()
}
