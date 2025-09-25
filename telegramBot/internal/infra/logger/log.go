package logger

import (
	"context"
	"github.com/rs/zerolog"
	"github.com/rs/zerolog/pkgerrors"
	"gopkg.in/natefinch/lumberjack.v2"
	"io"
	"os"
	"runtime/debug"
	"sync"
	"time"
)

type Logger = zerolog.Logger

var (
	once     sync.Once
	log      zerolog.Logger
	initErr  error
	initDone bool
)

type Config struct {
	FilePath         string `koanf:"file_path"`
	UseLocalTime     bool   `koanf:"use_local_time"`
	MustCompress     bool   `koanf:"compress"`
	LogLevel         int    `koanf:"level"`
	FileMaxSizeInMB  int    `koanf:"file_max_size_in_mb"`
	FileMaxAgeInDays int    `koanf:"file_max_age_in_days"`
	FileMaxBackups   int    `koanf:"file_max_backups"`
}

func Init(config Config) error {
	once.Do(func() {
		zerolog.ErrorStackMarshaler = pkgerrors.MarshalStack
		if config.UseLocalTime {
			zerolog.TimeFieldFormat = time.RFC3339
		} else {
			zerolog.TimeFieldFormat = time.RFC3339Nano
		}

		fileLogger := &lumberjack.Logger{
			Filename:   config.FilePath,
			MaxSize:    config.FileMaxSizeInMB,
			MaxBackups: config.FileMaxBackups,
			MaxAge:     config.FileMaxAgeInDays,
			Compress:   config.MustCompress,
		}

		var output io.Writer = zerolog.MultiLevelWriter(os.Stderr, fileLogger)

		var gitRevision, goVersion string
		if buildInfo, ok := debug.ReadBuildInfo(); ok {
			goVersion = buildInfo.GoVersion
			for _, v := range buildInfo.Settings {
				if v.Key == "vcs.revision" {
					gitRevision = v.Value
					break
				}
			}
		}

		log = zerolog.New(output).
			Level(zerolog.Level(config.LogLevel)).
			With().
			Timestamp().
			Str("git_revision", gitRevision).
			Str("go_version", goVersion).
			Logger()

		initDone = true
	})

	return initErr
}

func Get() Logger {
	if !initDone {
		panic("logger not initialized: call log.Init(config) before using Get()")
	}
	return log
}

func Ctx(ctx context.Context) *Logger {
	return zerolog.Ctx(ctx)
}
