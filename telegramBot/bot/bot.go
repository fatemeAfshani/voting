package bot

import (
	"github.com/fatemeAfshani/voting/service"
	"log"
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

func (bot TelegramBot) Start() {
	//todo due to documentation we should put offset = UpdateId + 1
	u := tgbotapi.NewUpdate(0)
	u.Timeout = 60

	updates := bot.telegramBot.GetUpdatesChan(u)
	log.Println("Bot started.")

	//todo write this data into queue for scalability
	for update := range updates {
		if update.Message == nil {
			continue
		}
		chatID := update.Message.Chat.ID
		text := strings.ToLower(update.Message.Text)

		switch text {
		case "/start":
			bot.reply(chatID, "Welcome! Are you a *creator* or *voter*?", true)
		case "creator":
			bot.reply(chatID, "Do you want to *signup* or *signin*?", true)
		case "signup":
			bot.reply(chatID, "Please send me your phone number (type it in international format).", false)
			// next step: you’d need a state machine (flows.go)
		case "signin":
			bot.reply(chatID, "Please send me your phone number.", false)
		default:
			bot.reply(chatID, "I didn’bot understand. Use /start.", false)
		}
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
