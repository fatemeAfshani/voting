package main

import (
	"log"
	"os"

	"github.com/joho/godotenv"
	"github.com/yourname/telegram-bot/bot"
)

func main() {
	_ = godotenv.Load()

	token := os.Getenv("TELEGRAM_BOT_TOKEN")
	apiURL := os.Getenv("API_URL")

	b, err := bot.NewTelegramBot(token, apiURL)
	if err != nil {
		log.Fatalf("failed to start bot: %v", err)
	}
	b.Start()
}
