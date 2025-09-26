package telegram

import (
	"strings"

	log "github.com/fatemeAfshani/voting/internal/infra/logger"
	tgbotapi "github.com/go-telegram-bot-api/telegram-bot-api/v5"
	"github.com/nicksnyder/go-i18n/v2/i18n"
)

type Messenger struct {
	bot       *tgbotapi.BotAPI
	logger    log.Logger
	localizer *i18n.Localizer
}

func NewMessenger(bot *tgbotapi.BotAPI, logger log.Logger, localizer *i18n.Localizer) Messenger {
	return Messenger{bot: bot, logger: logger, localizer: localizer}
}

func (m Messenger) Reply(chatID int64, text string, markdown bool) {
	logging := m.logger.With().Str("package", "bot").Str("function", "reply").Logger()

	msg := tgbotapi.NewMessage(chatID, text)
	if markdown {
		msg.ParseMode = tgbotapi.ModeMarkdownV2
		msg.Text = tgbotapi.EscapeText(tgbotapi.ModeMarkdownV2, text)
	}
	_, err := m.bot.Send(msg)
	if err != nil {
		logging.Err(err).
			Str("text", text).
			Int64("chatId", chatID).
			Msg("error in replying the message")
	}
}

func (m Messenger) ReplyID(chatID int64, messageID string, markdown bool, data map[string]any) {
	msg := m.localizeWithData(messageID, data)
	m.Reply(chatID, msg, markdown)
}

func (m Messenger) SendStep(chatID int64, step StepMessage) {
	logging := m.logger.With().Str("package", "bot").Str("function", "sendStep").Logger()

	if len(step.Options) > 0 {
		var rows [][]tgbotapi.KeyboardButton
		for _, opt := range step.Options {
			label := m.localize(string(opt))
			rows = append(rows, tgbotapi.NewKeyboardButtonRow(
				tgbotapi.NewKeyboardButton(label),
			))
		}
		msg := tgbotapi.NewMessage(chatID, m.localize(step.TextID))
		msg.ParseMode = tgbotapi.ModeMarkdownV2
		msg.Text = tgbotapi.EscapeText(tgbotapi.ModeMarkdownV2, msg.Text)
		msg.ReplyMarkup = tgbotapi.ReplyKeyboardMarkup{
			Keyboard:        rows,
			ResizeKeyboard:  true,
			OneTimeKeyboard: true,
		}
		_, err := m.bot.Send(msg)
		if err != nil {
			logging.Err(err).
				Str("text", step.TextID).
				Int64("chatId", chatID).
				Msg("error in sending message the message")
		}
		return
	}

	m.Reply(chatID, m.localize(step.TextID), true)
}

func (m Messenger) localize(messageID string) string {
	if m.localizer == nil {
		return messageID
	}
	msg, err := m.localizer.Localize(&i18n.LocalizeConfig{MessageID: messageID})
	if err != nil || msg == "" {
		return messageID
	}
	return msg
}

func (m Messenger) localizeWithData(messageID string, data map[string]any) string {
	if m.localizer == nil {
		return messageID
	}
	msg, err := m.localizer.Localize(&i18n.LocalizeConfig{MessageID: messageID, TemplateData: data})
	if err != nil || msg == "" {
		return messageID
	}
	return msg
}

func (m Messenger) MatchOption(input string) (Option, bool) {
	normalized := strings.TrimSpace(strings.ToLower(input))
	candidates := []Option{OptionCreator, OptionVoter, OptionSignUp, OptionSignIn}
	for _, opt := range candidates {
		label := strings.ToLower(m.localize(string(opt)))
		if normalized == label {
			return opt, true
		}
	}
	return "", false
}
