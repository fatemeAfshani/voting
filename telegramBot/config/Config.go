package config

import (
	"github.com/fatemeAfshani/voting/bot"
	"github.com/fatemeAfshani/voting/pkg/grpc"
	"github.com/fatemeAfshani/voting/pkg/log"
	"time"
)

type Config struct {
	TotalShutdownTimeout time.Duration `koanf:"total_shutdown_timeout"`
	TelegramBotConfig    bot.Config    `koanf:"telegram_bot"`
	GrpcClient           grpc.Client   `koanf:"grpc_client"`
	Logger               log.Config    `koanf:"logger"`
}
