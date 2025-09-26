package main

import (
	"github.com/fatemeAfshani/voting/internal/app"
	"go.uber.org/fx"
)

func main() {
	fx.New(app.Module).Run()
}
