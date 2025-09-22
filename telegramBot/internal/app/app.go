package app

import (
	"context"
	"fmt"
	grpcClient "github.com/fatemeAfshani/voting/internal/adapters/grpc"
	"github.com/fatemeAfshani/voting/internal/adapters/telegram"
	"github.com/fatemeAfshani/voting/internal/config"
	log "github.com/fatemeAfshani/voting/internal/infra/logger"
	"github.com/fatemeAfshani/voting/internal/service"
	"google.golang.org/grpc"
	"os"
	"os/signal"
	"sync"
	"syscall"
)

type Application struct {
	Config      config.Config
	Service     service.Service
	TelegramBot telegram.TelegramBot
	Logger      log.Logger
}

func Setup(Conn *grpc.ClientConn, cfg config.Config) (*Application, error) {
	userAdapter := grpcClient.New(Conn)
	srv := service.New(userAdapter)

	app := &Application{
		Config:  cfg,
		Service: srv,
		Logger:  log.Get(),
	}

	telegramBot, err := telegram.NewTelegramBot(cfg.TelegramBotConfig, srv)
	if err != nil {
		app.Logger.Error().Err(err).Msg("failed to setup telegram bot")
		return app, err
	}

	app.TelegramBot = telegramBot

	return app, nil
}

func (app *Application) Start() error {
	ctx, ctxCancel := context.WithCancel(context.Background())

	app.Logger.Info().Msg("Starting application...")

	var wg sync.WaitGroup

	ctx, stop := signal.NotifyContext(context.Background(), os.Interrupt, syscall.SIGTERM)
	defer stop()

	app.startServer(ctx, &wg)

	<-ctx.Done()
	app.Logger.Info().Msg("Shutdown signal received...")
	ctxCancel()

	shutdownTimeoutCtx, cancel := context.WithTimeout(context.Background(), app.Config.TotalShutdownTimeout)
	defer cancel()

	if err := app.shutdownServers(shutdownTimeoutCtx, &wg); err != nil {
		app.Logger.Warn().Err(err).Msg("Shutdown timed out, exiting application")
		return err
	}

	wg.Wait()
	app.Logger.Info().Msg("All services stopped gracefully.")
	return nil
}

func (app *Application) startServer(ctx context.Context, wg *sync.WaitGroup) {
	wg.Add(1)

	go func() {
		defer wg.Done()
		app.Logger.Info().Msg(fmt.Sprintf("starting telegram bot..."))
		app.TelegramBot.Start(ctx)
	}()
}

func (app *Application) shutdownServers(ctx context.Context, wg *sync.WaitGroup) error {
	done := make(chan struct{})

	go func() {
		defer close(done)
		app.Logger.Info().Msg("Stopping telegram bot...")
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
