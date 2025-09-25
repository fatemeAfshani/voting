package app

import (
	"context"
	"fmt"
	"os"
	"path/filepath"

	"github.com/fatemeAfshani/voting/internal/adapters/service"
	"github.com/fatemeAfshani/voting/internal/adapters/telegram"
	"github.com/fatemeAfshani/voting/internal/config"
	"github.com/fatemeAfshani/voting/internal/domain"
	configMapper "github.com/fatemeAfshani/voting/internal/infra/config"
	log "github.com/fatemeAfshani/voting/internal/infra/logger"
	tgbotapi "github.com/go-telegram-bot-api/telegram-bot-api/v5"
	"github.com/nicksnyder/go-i18n/v2/i18n"
	"go.uber.org/fx"
	"golang.org/x/text/language"
)

func ProvideConfig() (config.Config, error) {
	var cfg config.Config
	workingDir, err := os.Getwd()
	if err != nil {
		return cfg, err
	}

	options := configMapper.Option{
		Prefix:       "BOT_",
		Delimiter:    ".",
		Separator:    "__",
		YamlFilePath: filepath.Join(workingDir, "config", "config.dev.yaml"),
		CallbackEnv:  nil,
	}
	if err := configMapper.Load(options, &cfg); err != nil {
		return cfg, err
	}
	return cfg, nil
}

func ProvideLogger(cfg config.Config) (log.Logger, error) {
	if err := log.Init(cfg.Logger); err != nil {
		return log.Logger{}, err
	}
	return log.Get(), nil
}

func ProvideUserService(cfg config.Config) (*service.UserServiceImp, error) {
	return service.NewUserService(cfg.UserGrpcClient)
}

func ProvideUserDomain(userService *service.UserServiceImp, logger log.Logger) *domain.UserDomain {
	return domain.NewUserDomain(userService, logger)
}

func ProvideTelegramBot(cfg config.Config, botApi *tgbotapi.BotAPI, tokens *domain.TokenStore, messenger telegram.Messenger, logger log.Logger, userDomain *domain.UserDomain) (*telegram.Bot, error) {
	bot := telegram.NewTelegramBot(cfg.TelegramBotConfig, botApi, tokens, userDomain, messenger, logger)
	return bot, nil
}

func ProvideTGBotAPI(cfg config.Config) (*tgbotapi.BotAPI, error) {
	token := cfg.TelegramBotConfig.Token
	if token == "" {
		return nil, fmt.Errorf("telegram bot token is missing: set BOT_TELEGRAM_BOT__TOKEN or config.telegram_bot.token")
	}
	bot, err := tgbotapi.NewBotAPI(token)
	if err != nil {
		return nil, fmt.Errorf("failed to create Telegram BotAPI: %w", err)
	}
	return bot, nil
}

func ProvideTokenStore() *domain.TokenStore {
	return domain.NewTokenStore()
}

func ProvideMessenger(bot *tgbotapi.BotAPI, logger log.Logger, loc *i18n.Localizer) telegram.Messenger {
	return telegram.NewMessenger(bot, logger, loc)
}

func ProvideBundle() (*i18n.Bundle, error) {
	b := i18n.NewBundle(language.Persian)
	wd, err := os.Getwd()
	if err != nil {
		return nil, fmt.Errorf("i18n: getwd failed: %w", err)
	}
	files := []string{
		filepath.Join(wd, "internal", "adapters", "locales", "fa.json"),
		filepath.Join(wd, "internal", "adapters", "locales", "en.json"),
	}
	for _, f := range files {
		if _, err := b.LoadMessageFile(f); err != nil {
			return nil, fmt.Errorf("i18n: failed to load locale file %s: %w", f, err)
		}
	}
	return b, nil
}

func ProvideLocalizer(bundle *i18n.Bundle) *i18n.Localizer {
	return i18n.NewLocalizer(bundle, language.Persian.String(), language.English.String())
}

func RunTelegramBot(lc fx.Lifecycle, bot *telegram.Bot, logger log.Logger, cfg config.Config) {
	lc.Append(fx.Hook{
		OnStart: func(ctx context.Context) error {
			logger.Info().Msg("Starting Telegram Bot...")
			go bot.Start(ctx)
			return nil
		},
		OnStop: func(ctx context.Context) error {
			logger.Info().Msg("Stopping Telegram Bot...")
			bot.Stop()
			return nil
		},
	})
}

var Module = fx.Options(
	fx.Provide(
		ProvideConfig,
		ProvideLogger,
		ProvideUserService,
		ProvideUserDomain,

		ProvideTGBotAPI,
		ProvideTokenStore,
		ProvideBundle,
		ProvideLocalizer,
		ProvideMessenger,
		ProvideTelegramBot,
	),
	fx.Invoke(RunTelegramBot),
)
