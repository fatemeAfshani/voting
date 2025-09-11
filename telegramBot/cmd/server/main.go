package main

import (
	"github.com/fatemeAfshani/voting"
	"github.com/fatemeAfshani/voting/config"
	cfgloader "github.com/fatemeAfshani/voting/pkg/cfg_loader"
	rpcClient "github.com/fatemeAfshani/voting/pkg/grpc"
	"github.com/fatemeAfshani/voting/pkg/log"
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

	options := cfgloader.Option{
		Prefix:       "BOT_",
		Delimiter:    ".",
		Separator:    "__",
		YamlFilePath: filepath.Join(workingDir, "cmd", "deploy", "development", "config.yaml"),
		CallbackEnv:  nil,
	}
	if err := cfgloader.Load(options, &cfg); err != nil {
		goLog.Fatalf("Failed to load Bot config: %v", err)
	}

	err = log.Init(cfg.Logger)
	if err != nil {
		goLog.Fatalf("Failed to init logger: %v", err)
	}

	logger := log.Get()
	logger.Info().Msg("Starting Bot App...")

	rpcClientConnection, err := rpcClient.NewClient(cfg.GrpcClient)
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
