package telegram

import (
	"context"
	"fmt"
	"strings"

	"github.com/fatemeAfshani/voting/internal/domain"
	log "github.com/fatemeAfshani/voting/internal/infra/logger"
	"github.com/fatemeAfshani/voting/internal/service"
	tgbotapi "github.com/go-telegram-bot-api/telegram-bot-api/v5"
)

type Config struct {
	Token string `koanf:"token"`
}

type TelegramBot struct {
	service     service.Service
	config      Config
	tokens      *domain.TokenStore
	telegramBot *tgbotapi.BotAPI
	messenger   Messenger
	logger      log.Logger
}

func NewTelegramBot(config Config, service service.Service, botApi *tgbotapi.BotAPI, tokens *domain.TokenStore, messenger Messenger, logger log.Logger) TelegramBot {
	return TelegramBot{
		service:     service,
		config:      config,
		tokens:      tokens,
		telegramBot: botApi,
		messenger:   messenger,
		logger:      logger,
	}
}

func (bot TelegramBot) Start(ctx context.Context) {
	logger := bot.logger.With().Str("package", "bot").Str("function", "start").Logger()

	//todo due to documentation we should put offset = UpdateId + 1
	u := tgbotapi.NewUpdate(0)
	u.Timeout = 60

	updates := bot.telegramBot.GetUpdatesChan(u)
	logger.Info().Msg("bot started")

	//todo write this data into queue for scalability
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

		chatID := update.Message.Chat.ID
		text := strings.ToLower(update.Message.Text)
		session := bot.tokens.GetOrCreate(chatID)

		logger.Info().Str("state", string(session.State)).Msg("new message received")

		switch session.State {
		case domain.StateNone:
			bot.handleStateNone(chatID, text)
		case domain.StateChoosingRole:
			bot.handleChoosingRole(chatID, session, text)
		case domain.StateChoosingAuth:
			bot.handleChoosingAuth(ctx, chatID, session, text)
		case domain.StateAwaitPhone:
			bot.handleAwaitPhone(ctx, chatID, session, update.Message.Text)
		case domain.StateAwaitPassword:
			bot.handleAwaitPassword(ctx, chatID, session, update.Message.Text)
		}
	}
}

func (bot TelegramBot) handleStateNone(chatID int64, text string) {
	if text == string(OptionStart) {
		bot.tokens.Set(chatID, &domain.UserSession{State: domain.StateChoosingRole})
		bot.messenger.SendStep(chatID, StepWelcome)
		return
	}
	bot.messenger.Reply(chatID, string(PromptUnknown), false)
}

func (bot TelegramBot) handleChoosingRole(chatID int64, session *domain.UserSession, text string) {
	switch text {
	case OptionCreator:
		session.Role = domain.Creator
		session.State = domain.StateChoosingAuth
		bot.tokens.Set(chatID, session)
		bot.messenger.SendStep(chatID, StepSignInOrSignUp)
	case OptionVoter:
		session.Role = domain.Voter
		session.State = domain.StateChoosingAuth
		bot.tokens.Set(chatID, session)
		bot.messenger.SendStep(chatID, StepSignInOrSignUp)
	default:
		bot.messenger.Reply(chatID, string(PromptUnknown), false)
	}
}

func (bot TelegramBot) handleChoosingAuth(ctx context.Context, chatID int64, session *domain.UserSession, text string) {
	switch text {
	case OptionSignUp, OptionSignIn:
		session.Auth = string(text)
		session.State = domain.StateAwaitPhone
		bot.tokens.Set(chatID, session)

		if session.Role == domain.Creator && session.Auth == string(OptionSignUp) {
			bot.messenger.SendStep(chatID, StepCreatorSignup)
		} else if session.Role == domain.Creator && session.Auth == string(OptionSignIn) {
			bot.messenger.SendStep(chatID, StepCreatorSignIn)
		} else if session.Role == domain.Voter && session.Auth == string(OptionSignUp) {
			bot.messenger.SendStep(chatID, StepVoterSignUp)
		} else if session.Role == domain.Voter && session.Auth == string(OptionSignIn) {
			bot.messenger.SendStep(chatID, StepVoterSignIn)
		}
	default:
		bot.messenger.Reply(chatID, string(PromptUnknown), false)
	}
}

func (bot TelegramBot) handleAwaitPhone(ctx context.Context, chatID int64, session *domain.UserSession, phone string) {
	session.Phone = phone

	if session.Auth == string(OptionSignUp) {
		session.State = domain.StateAwaitPassword
		bot.tokens.Set(chatID, session)
		bot.messenger.Reply(chatID, string(PromptAskPasswordSignUp), true)
		return
	}

	err := bot.service.SignIn(ctx, session.TelegramID)
	if err != nil {
		bot.messenger.Reply(chatID, "❌ Sign-in failed: "+err.Error(), false)
	} else {
		bot.messenger.Reply(chatID, "✅ You are signed in successfully!", false)
	}
	session.State = domain.StateNone
	bot.tokens.Set(chatID, session)
}

func (bot TelegramBot) handleAwaitPassword(ctx context.Context, chatID int64, session *domain.UserSession, password string) {
	session.Password = password

	if session.Auth == string(OptionSignUp) {
		err := bot.service.SignUp(ctx, session.Phone, session.Password, session.TelegramID)
		if err != nil {
			bot.messenger.Reply(chatID, fmt.Sprintf("❌ error in registering. %v", err.Error()), false)
		} else {
			bot.messenger.Reply(chatID, "You are signed up successfully!", false)
		}
	} else {
		err := bot.service.SignIn(ctx, session.TelegramID)
		if err != nil {
			bot.messenger.Reply(chatID, "❌ Sign-in failed: "+err.Error(), false)
		} else {
			bot.messenger.Reply(chatID, "✅ You are signed in successfully!", false)
		}
	}

	session.State = domain.StateNone
	bot.tokens.Set(chatID, session)
}

func (bot TelegramBot) Stop() {
	bot.telegramBot.StopReceivingUpdates()
}
