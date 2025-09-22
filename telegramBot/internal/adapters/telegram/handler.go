package telegram

import (
	"context"
	"fmt"
	"github.com/fatemeAfshani/voting/internal/domain"
	log "github.com/fatemeAfshani/voting/internal/infra/logger"
	"github.com/fatemeAfshani/voting/internal/service"
	tgbotapi "github.com/go-telegram-bot-api/telegram-bot-api/v5"
	"strings"
)

type Config struct {
	Token string `koanf:"token"`
}

type TelegramBot struct {
	service     service.Service
	config      Config
	tokens      *domain.TokenStore
	telegramBot *tgbotapi.BotAPI
}

func NewTelegramBot(config Config, service service.Service) (TelegramBot, error) {
	botApi, err := tgbotapi.NewBotAPI(config.Token)
	if err != nil || botApi == nil {
		return TelegramBot{}, err
	}

	return TelegramBot{
		service:     service,
		config:      config,
		tokens:      domain.NewTokenStore(),
		telegramBot: botApi,
	}, nil
}

func (bot TelegramBot) Start(ctx context.Context) {
	logger := log.Get().With().Str("package", "bot").Str("function", "start").Logger()

	//todo due to documentation we should put offset = UpdateId + 1
	u := tgbotapi.NewUpdate(0)
	u.Timeout = 60

	updates := bot.telegramBot.GetUpdatesChan(u)
	logger.Info().Msg("bot started")

	//todo write this data into queue for scalability
	//todo: must separate each of these in a service function
	for update := range updates {
		if update.Message == nil {
			logger.Warn().
				Int("updateId", update.UpdateID).
				Int64("chatId", update.Message.Chat.ID).
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
			if text == string(OptionStart) {
				bot.tokens.Set(chatID, &domain.UserSession{State: domain.StateChoosingRole})
				bot.sendStep(chatID, StepWelcome)
			} else {
				bot.reply(chatID, string(PromptUnknown), false)
			}

		case domain.StateChoosingRole:
			switch text {
			case OptionCreator:
				session.Role = domain.Creator
				session.State = domain.StateChoosingAuth
				bot.tokens.Set(chatID, session)
				bot.sendStep(chatID, StepSignInOrSignUp)
			case OptionVoter:
				session.Role = domain.Voter
				session.State = domain.StateChoosingAuth
				bot.tokens.Set(chatID, session)
				bot.sendStep(chatID, StepSignInOrSignUp)
			default:
				bot.reply(chatID, PromptUnknown, false)
			}

		case domain.StateChoosingAuth:
			switch text {
			case OptionSignUp, OptionSignIn:
				session.State = domain.StateAwaitPhone
				bot.tokens.Set(chatID, session)

				if session.Role == OptionCreator && session.Auth == OptionSignUp {
					bot.sendStep(chatID, StepCreatorSignup)
				} else if session.Role == OptionCreator && session.Auth == OptionSignIn {
					bot.sendStep(chatID, StepCreatorSignIn)
				} else if session.Role == OptionVoter && session.Auth == OptionSignUp {
					bot.sendStep(chatID, StepVoterSignUp)
				} else if session.Role == OptionVoter && session.Auth == OptionSignIn {
					bot.sendStep(chatID, StepVoterSignIn)
				}
			default:
				bot.reply(chatID, string(PromptUnknown), false)
			}

		case domain.StateAwaitPhone:
			phone := update.Message.Text
			session.Phone = phone

			if session.Auth == OptionSignUp {
				session.State = domain.StateAwaitPassword
				bot.tokens.Set(chatID, session)
				bot.reply(chatID, PromptAskPasswordSignUp, true)
			} else {
				err := bot.service.SignIn(ctx, session.TelegramID)
				if err != nil {
					bot.reply(chatID, "❌ Sign-in failed: "+err.Error(), false)
				} else {
					bot.reply(chatID, "✅ You are signed in successfully!", false)
				}

				session.State = domain.StateNone
				bot.tokens.Set(chatID, session)
			}

		case domain.StateAwaitPassword:
			password := update.Message.Text
			session.Password = password

			if session.Auth == OptionSignUp {
				err := bot.service.SignUp(ctx, session.Phone, session.Password, session.TelegramID)
				if err != nil {
					bot.reply(chatID, fmt.Sprintf("❌ error in registering. %v", err.Error()), false)
				} else {
					bot.reply(chatID, "You are signed up successfully!", false)
				}
			} else {
				err := bot.service.SignIn(ctx, session.TelegramID)
				if err != nil {
					bot.reply(chatID, "❌ Sign-in failed: "+err.Error(), false)
				} else {
					bot.reply(chatID, "✅ You are signed in successfully!", false)
				}
			}

			session.State = domain.StateNone
			bot.tokens.Set(chatID, session)
		}
	}
}

func (bot TelegramBot) sendStep(chatID int64, step StepMessage) {
	logging := log.Get().With().Str("package", "bot").Str("function", "sendStep").Logger()

	if len(step.Options) > 0 {
		var rows [][]tgbotapi.KeyboardButton
		for _, opt := range step.Options {
			rows = append(rows, tgbotapi.NewKeyboardButtonRow(
				tgbotapi.NewKeyboardButton(string(opt)),
			))
		}
		msg := tgbotapi.NewMessage(chatID, string(step.Text))
		msg.ParseMode = "Markdown"
		msg.ReplyMarkup = tgbotapi.ReplyKeyboardMarkup{
			Keyboard:        rows,
			ResizeKeyboard:  true,
			OneTimeKeyboard: true,
		}
		_, err := bot.telegramBot.Send(msg)
		if err != nil {
			logging.Err(err).
				Str("text", string(step.Text)).
				Int64("chatId", chatID).
				Msg("error in sending message the message")
		}
	} else {
		bot.reply(chatID, string(step.Text), true)
	}
}

func (bot TelegramBot) reply(chatID int64, text string, markdown bool) {
	logging := log.Get().With().Str("package", "bot").Str("function", "reply").Logger()

	msg := tgbotapi.NewMessage(chatID, text)
	if markdown {
		msg.ParseMode = "Markdown"
	}
	_, err := bot.telegramBot.Send(msg)
	if err != nil {
		logging.Err(err).
			Str("text", text).
			Int64("chatId", chatID).
			Msg("error in replying the message")
	}
}

func (bot TelegramBot) Stop() {
	bot.telegramBot.StopReceivingUpdates()
}
