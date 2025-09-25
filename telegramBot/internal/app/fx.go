package app

import (
	"context"
	"fmt"
	"path/filepath"

	"os"

	grpcAdapter "github.com/fatemeAfshani/voting/internal/adapters/grpc"
	"github.com/fatemeAfshani/voting/internal/adapters/telegram"
	"github.com/fatemeAfshani/voting/internal/config"
	"github.com/fatemeAfshani/voting/internal/domain"
	configMapper "github.com/fatemeAfshani/voting/internal/infra/config"
	grpcClient "github.com/fatemeAfshani/voting/internal/infra/grpc"
	log "github.com/fatemeAfshani/voting/internal/infra/logger"
	"github.com/fatemeAfshani/voting/internal/ports"
	"github.com/fatemeAfshani/voting/internal/service"
	tgbotapi "github.com/go-telegram-bot-api/telegram-bot-api/v5"
	"go.uber.org/fx"
	"google.golang.org/grpc"
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

func ProvideGRPCConn(cfg config.Config) (*grpc.ClientConn, error) {
	return grpcClient.NewClient(cfg.GrpcClient)
}

func ProvideUserClient(conn *grpc.ClientConn) ports.UserClient {
	return grpcAdapter.New(conn)
}

func ProvideService(u ports.UserClient) service.Service {
	return service.New(u)
}

func ProvideTelegramBot(cfg config.Config, s service.Service, botApi *tgbotapi.BotAPI, tokens *domain.TokenStore, messenger telegram.Messenger, logger log.Logger) (telegram.TelegramBot, error) {
	bot := telegram.NewTelegramBot(cfg.TelegramBotConfig, s, botApi, tokens, messenger, logger)
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

func ProvideMessenger(bot *tgbotapi.BotAPI, logger log.Logger) telegram.Messenger {
	return telegram.NewMessenger(bot, logger)
}

func RunTelegramBot(lc fx.Lifecycle, bot telegram.TelegramBot, logger log.Logger, cfg config.Config) {
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
		ProvideGRPCConn,
		ProvideUserClient,
		ProvideService,
		ProvideTGBotAPI,
		ProvideTokenStore,
		ProvideMessenger,
		ProvideTelegramBot,
	),
	fx.Invoke(RunTelegramBot),
)
