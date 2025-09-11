package app

import (
	"context"
	"fmt"
	userapi "github.com/fatemeAfshani/voting/api/user"
	"github.com/fatemeAfshani/voting/bot"
	"github.com/fatemeAfshani/voting/config"
	"github.com/fatemeAfshani/voting/pkg/log"
	"github.com/fatemeAfshani/voting/service"
	"google.golang.org/grpc"
	"os"
	"os/signal"
	"sync"
	"syscall"
)

type Application struct {
	Config      config.Config
	Service     service.Service
	TelegramBot bot.TelegramBot
}

func Setup(Conn *grpc.ClientConn, cfg config.Config) (Application, error) {
	logger := log.Get()

	userAdapter := userapi.New(Conn)
	srv := service.New(userAdapter)

	app := Application{
		Config:  cfg,
		Service: srv,
	}

	telegramBot, err := bot.NewTelegramBot(cfg.TelegramBotConfig, srv)
	if err != nil {
		logger.Error().Err(err).Msg("failed to setup telegram bot")
		return app, err
	}

	app.TelegramBot = telegramBot

	return app, nil
}

func (app Application) Start() error {
	logger := log.Get()
	logger.Info().Msg("Starting application...")

	var wg sync.WaitGroup

	ctx, stop := signal.NotifyContext(context.Background(), os.Interrupt, syscall.SIGTERM)
	defer stop()

	app.startServer(&wg)

	<-ctx.Done()
	logger.Info().Msg("Shutdown signal received...")

	shutdownTimeoutCtx, cancel := context.WithTimeout(context.Background(), app.Config.TotalShutdownTimeout)
	defer cancel()

	if err := app.shutdownServers(shutdownTimeoutCtx, &wg); err != nil {
		logger.Warn().Err(err).Msg("Shutdown timed out, exiting application")
		return err
	}

	wg.Wait()
	logger.Info().Msg("All services stopped gracefully.")
	return nil
}

func (app Application) startServer(wg *sync.WaitGroup) {
	logger := log.Get()
	wg.Add(1)

	go func() {
		defer wg.Done()
		logger.Info().Msg(fmt.Sprintf("starting telegram bot..."))
		app.TelegramBot.Start()
	}()
}

func (app Application) shutdownServers(ctx context.Context, wg *sync.WaitGroup) error {
	logger := log.Get()
	done := make(chan struct{})

	go func() {
		defer close(done)
		logger.Info().Msg("Stopping telegram bot...")
		app.TelegramBot.Stop()
	}()

	select {
	case <-done:
		wg.Done()
		return nil
	case <-ctx.Done():
		return ctx.Err()
	}
}
