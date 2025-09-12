package bot

import (
	"context"
	"github.com/fatemeAfshani/voting/pkg/log"
	"github.com/fatemeAfshani/voting/service"
	"strings"

	tgbotapi "github.com/go-telegram-bot-api/telegram-bot-api/v5"
)

type Config struct {
	Token string `koanf:"token"`
}

type TelegramBot struct {
	service     service.Service
	config      Config
	tokens      *TokenStore
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
		tokens:      NewTokenStore(),
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
	for update := range updates {
		if update.Message == nil {
			continue
		}
		chatID := update.Message.Chat.ID
		text := strings.ToLower(update.Message.Text)

		logger.Trace().Str("actualText", update.Message.Text).Str("processingText", text).Msg("new message received")

		switch text {
		case string(OptionStart):
			bot.sendStep(chatID, StepWelcome)

		case string(OptionCreator):
			bot.sendStep(chatID, StepSignInOrSignUp)

		case string(OptionVoter):
			bot.sendStep(chatID, StepSignInOrSignUp)

		case string(OptionSignUp):
			bot.sendStep(chatID, StepCreatorSignup)
			// TODO: save user state -> next input is phone

		case string(OptionSignIn):
			bot.sendStep(chatID, StepCreatorSignIn)

		default:
			bot.reply(chatID, string(PromptUnknown), false)
		}
	}
}

func (bot TelegramBot) sendStep(chatID int64, step StepMessage) {
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
		bot.telegramBot.Send(msg)
	} else {
		bot.reply(chatID, string(step.Text), true)
	}
}

func (bot TelegramBot) reply(chatID int64, text string, markdown bool) {
	msg := tgbotapi.NewMessage(chatID, text)
	if markdown {
		msg.ParseMode = "Markdown"
	}
	bot.telegramBot.Send(msg)
}

func (bot TelegramBot) Stop() {
	bot.telegramBot.StopReceivingUpdates()
}
