package config

import (
	"time"
)

type Config struct {
	TotalShutdownTimeout time.Duration  `koanf:"total_shutdown_timeout"`
	TelegramBotConfig    TelegramConfig `koanf:"telegram_bot"`
	UserGrpcClient       GRPCConfig     `koanf:"user_grpc_client"`
	Logger               LoggerConfig   `koanf:"logger"`
}

type GRPCConfig struct {
	Host string `koanf:"host"`
	Port int    `koanf:"port"`
}

type TelegramConfig struct {
	Token string `koanf:"token"`
}

type LoggerConfig struct {
	FilePath         string `koanf:"file_path"`
	UseLocalTime     bool   `koanf:"use_local_time"`
	MustCompress     bool   `koanf:"compress"`
	LogLevel         int    `koanf:"level"`
	FileMaxSizeInMB  int    `koanf:"file_max_size_in_mb"`
	FileMaxAgeInDays int    `koanf:"file_max_age_in_days"`
	FileMaxBackups   int    `koanf:"file_max_backups"`
}
