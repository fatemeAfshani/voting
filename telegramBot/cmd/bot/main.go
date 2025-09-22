package main

import (
	"github.com/fatemeAfshani/voting/internal/app"
	"github.com/fatemeAfshani/voting/internal/config"
	configMapper "github.com/fatemeAfshani/voting/internal/infra/config"
	grpcClient "github.com/fatemeAfshani/voting/internal/infra/grpc"
	logging "github.com/fatemeAfshani/voting/internal/infra/logger"
	goLog "log"
	"os"
	"path/filepath"
)

func main() {
	var cfg config.Config
	workingDir, err := os.Getwd()
	if err != nil {
		goLog.Fatalf("Error getting current working directory: %v", err)
	}

	options := configMapper.Option{
		Prefix:       "BOT_",
		Delimiter:    ".",
		Separator:    "__",
		YamlFilePath: filepath.Join(workingDir, "config", "config.dev.yaml"),
		CallbackEnv:  nil,
	}
	if err := configMapper.Load(options, &cfg); err != nil {
		goLog.Fatalf("Failed to load Bot config: %v", err)
	}

	err = logging.Init(cfg.Logger)
	if err != nil {
		goLog.Fatalf("Failed to init logger: %v", err)
	}

	logger := logging.Get()
	logger.Info().Msg("Starting Bot App...")

	rpcClientConnection, err := grpcClient.NewClient(cfg.GrpcClient)
	if err != nil {
		goLog.Fatal(err)
	}

	application, err := app.Setup(rpcClientConnection, cfg)
	if err != nil {
		goLog.Fatalf("Failed to setup the application: %v", err)
	}

	err = application.Start()
	if err != nil {
		goLog.Fatalf("Failed to start the application: %v", err)
	}
}
