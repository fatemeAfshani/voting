package bot

import (
	"log"
	"strings"

	tgbotapi "github.com/go-telegram-bot-api/telegram-bot-api/v5"
	"github.com/yourname/telegram-bot/api"
)

type TelegramBot struct {
	api    *api.Client
	tokens *TokenStore
	bot    *tgbotapi.BotAPI
}

func NewTelegramBot(botToken, apiURL string) (*TelegramBot, error) {
	b, err := tgbotapi.NewBotAPI(botToken)
	if err != nil {
		return nil, err
	}
	return &TelegramBot{
		api:    api.NewClient(apiURL),
		tokens: NewTokenStore(),
		bot:    b,
	}, nil
}

func (t *TelegramBot) Start() {
	u := tgbotapi.NewUpdate(0)
	u.Timeout = 60

	updates := t.bot.GetUpdatesChan(u)
	log.Println("Bot started.")

	for update := range updates {
		if update.Message == nil {
			continue
		}
		chatID := update.Message.Chat.ID
		text := strings.ToLower(update.Message.Text)

		switch text {
		case "/start":
			t.reply(chatID, "Welcome! Are you a *creator* or *voter*?", true)
		case "creator":
			t.reply(chatID, "Do you want to *signup* or *signin*?", true)
		case "signup":
			t.reply(chatID, "Please send me your phone number (type it in international format).", false)
			// next step: you’d need a state machine (flows.go)
		case "signin":
			t.reply(chatID, "Please send me your phone number.", false)
		default:
			t.reply(chatID, "I didn’t understand. Use /start.", false)
		}
	}
}

func (t *TelegramBot) reply(chatID int64, text string, markdown bool) {
	msg := tgbotapi.NewMessage(chatID, text)
	if markdown {
		msg.ParseMode = "Markdown"
	}
	t.bot.Send(msg)
}
