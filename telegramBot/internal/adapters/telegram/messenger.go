package telegram

import (
	log "github.com/fatemeAfshani/voting/internal/infra/logger"
	tgbotapi "github.com/go-telegram-bot-api/telegram-bot-api/v5"
)

type Messenger struct {
	bot    *tgbotapi.BotAPI
	logger log.Logger
}

func NewMessenger(bot *tgbotapi.BotAPI, logger log.Logger) Messenger {
	return Messenger{bot: bot, logger: logger}
}

func (m Messenger) Reply(chatID int64, text string, markdown bool) {
	logging := m.logger.With().Str("package", "bot").Str("function", "reply").Logger()

	msg := tgbotapi.NewMessage(chatID, text)
	if markdown {
		msg.ParseMode = "Markdown"
	}
	_, err := m.bot.Send(msg)
	if err != nil {
		logging.Err(err).
			Str("text", text).
			Int64("chatId", chatID).
			Msg("error in replying the message")
	}
}

func (m Messenger) SendStep(chatID int64, step StepMessage) {
	logging := m.logger.With().Str("package", "bot").Str("function", "sendStep").Logger()

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
		_, err := m.bot.Send(msg)
		if err != nil {
			logging.Err(err).
				Str("text", string(step.Text)).
				Int64("chatId", chatID).
				Msg("error in sending message the message")
		}
		return
	}

	m.Reply(chatID, string(step.Text), true)
}
