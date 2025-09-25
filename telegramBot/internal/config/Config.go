package config

import (
	"time"

	"github.com/fatemeAfshani/voting/internal/adapters/telegram"
	"github.com/fatemeAfshani/voting/internal/infra/grpc"
	"github.com/fatemeAfshani/voting/internal/infra/logger"
)

type Config struct {
	TotalShutdownTimeout time.Duration   `koanf:"total_shutdown_timeout"`
	TelegramBotConfig    telegram.Config `koanf:"telegram_bot"`
	GrpcClient           grpc.Client     `koanf:"grpc_client"`
	Logger               logger.Config   `koanf:"logger"`
}
