package logger

import (
	"context"
	"github.com/fatemeAfshani/voting/internal/config"
	"github.com/rs/zerolog"
	"github.com/rs/zerolog/pkgerrors"
	"gopkg.in/natefinch/lumberjack.v2"
	"io"
	"os"
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

func Init(config config.LoggerConfig) error {
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

		log = zerolog.New(output).
			Level(zerolog.Level(config.LogLevel)).
			With().
			Timestamp().
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
