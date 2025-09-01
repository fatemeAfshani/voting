package main

import (
	"context"
	"encoding/json"
	"fmt"
	"io"
	"log"
	"net/http"
	"net/url"
	"os"
	"os/signal"
	"strings"
	"syscall"
	"time"

	tgbotapi "github.com/go-telegram-bot-api/telegram-bot-api/v5"
)

func main() {
	ctx, stop := signal.NotifyContext(context.Background(), os.Interrupt, syscall.SIGTERM)
	defer stop()

	token := mustGetEnv("TELEGRAM_BOT_TOKEN")
	localAPIBase := getEnv("LOCAL_API_BASE", "http://localhost:8080")
	webhookURL := os.Getenv("WEBHOOK_URL") // e.g. https://your-domain.com
	port := getEnv("PORT", "8081")         // only used in webhook mode
	debug := os.Getenv("BOT_DEBUG") == "1"

	bot, err := tgbotapi.NewBotAPI(token)
	if err != nil {
		log.Fatalf("failed to create bot: %v", err)
	}
	bot.Debug = debug
	log.Printf("Authorized as @%s (ID: %d). Webhook mode: %t", bot.Self.UserName, bot.Self.ID, webhookURL != "")

	// Advertise commands to Telegram clients (nice UX)
	_, _ = bot.Request(tgbotapi.NewSetMyCommands(
		tgbotapi.BotCommand{Command: "start", Description: "Start / help"},
		tgbotapi.BotCommand{Command: "echo", Description: "Echo back some text"},
		tgbotapi.BotCommand{Command: "hello", Description: "Call local API: /hello?name="},
		tgbotapi.BotCommand{Command: "ping", Description: "Health check"},
	))

	var updates tgbotapi.UpdatesChannel
	var srv *http.Server

	if webhookURL != "" {
		// Webhook mode (requires public HTTPS). We include the bot token in the path as a simple secret.
		path := "/webhook/" + bot.Token
		cfg, err := tgbotapi.NewWebhook(webhookURL + path)
		if err != nil {
			log.Fatalf("webhook config error: %v", err)
		}
		if _, err := bot.Request(cfg); err != nil {
			log.Fatalf("failed to set webhook: %v", err)
		}
		info, _ := bot.GetWebhookInfo()
		log.Printf("Webhook set: URL=%s pending=%d", info.URL, info.PendingUpdateCount)

		updates = bot.ListenForWebhook(path)
		srv = &http.Server{Addr: ":" + port}
		go func() {
			log.Printf("Listening for webhooks on %s%s", srv.Addr, path)
			if err := srv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
				log.Fatalf("http server error: %v", err)
			}
		}()
	} else {
		// Long-polling (great for local dev)
		u := tgbotapi.NewUpdate(0)
		u.Timeout = 30
		updates = bot.GetUpdatesChan(u)
	}

	// Main loop
	for {
		select {
		case <-ctx.Done():
			log.Println("Shutting down…")
			if srv != nil {
				_ = srv.Shutdown(context.Background())
			}
			return
		case update := <-updates:
			if update.Message == nil { // ignore non-message updates
				continue
			}
			go handleUpdate(ctx, bot, update, localAPIBase)
		}
	}
}

func handleUpdate(ctx context.Context, bot *tgbotapi.BotAPI, update tgbotapi.Update, localAPIBase string) {
	msg := update.Message
	text := strings.TrimSpace(msg.Text)
	if text == "" {
		return
	}
	fields := strings.Fields(text)
	cmd := strings.ToLower(fields[0])
	args := ""
	if len(fields) > 1 {
		args = strings.Join(fields[1:], " ")
	}

	switch cmd {
	case "/start", "/help":
		reply := "👋 Hi! I can do a few things:\n\n" +
			"/echo <text> — echo your text\n" +
			"/hello <name> — calls your local API GET /hello?name=<name>\n" +
			"/ping — quick health check\n"
		send(bot, msg.Chat.ID, reply)

	case "/ping":
		send(bot, msg.Chat.ID, "pong")

	case "/echo":
		if args == "" {
			send(bot, msg.Chat.ID, "Usage: /echo <text>")
			return
		}
		send(bot, msg.Chat.ID, args)

	case "/hello":
		name := strings.TrimSpace(args)
		if name == "" {
			name = firstNonEmpty(msg.From.FirstName, msg.From.UserName, "there")
		}
		ctx2, cancel := context.WithTimeout(ctx, 4*time.Second)
		defer cancel()
		res, err := callLocalGET(ctx2, localAPIBase, "/hello", map[string]string{"name": name})
		if err != nil {
			send(bot, msg.Chat.ID, fmt.Sprintf("❌ Local API error: %v", err))
			return
		}
		send(bot, msg.Chat.ID, res)

	default:
		send(bot, msg.Chat.ID, "I don't know that one 🤔. Try /help")
	}
}

func send(bot *tgbotapi.BotAPI, chatID int64, text string) {
	m := tgbotapi.NewMessage(chatID, text)
	m.ParseMode = "Markdown"
	if _, err := bot.Send(m); err != nil {
		log.Printf("send error: %v", err)
	}
}

func callLocalGET(ctx context.Context, base, path string, query map[string]string) (string, error) {
	u, err := url.Parse(base)
	if err != nil {
		return "", fmt.Errorf("bad LOCAL_API_BASE: %w", err)
	}
	u.Path = strings.TrimRight(u.Path, "/") + path
	q := u.Query()
	for k, v := range query {
		q.Set(k, v)
	}
	u.RawQuery = q.Encode()

	req, err := http.NewRequestWithContext(ctx, http.MethodGet, u.String(), nil)
	if err != nil {
		return "", err
	}
	resp, err := http.DefaultClient.Do(req)
	if err != nil {
		return "", err
	}
	defer resp.Body.Close()
	body, _ := io.ReadAll(resp.Body)
	if resp.StatusCode >= 300 {
		return "", fmt.Errorf("status %d: %s", resp.StatusCode, string(body))
	}
	// Try to parse {"message": "…"}, otherwise return raw body
	var m struct {
		Message string `json:"message"`
	}
	if json.Unmarshal(body, &m) == nil && m.Message != "" {
		return m.Message, nil
	}
	return string(body), nil
}

func mustGetEnv(key string) string {
	v := os.Getenv(key)
	if v == "" {
		log.Fatalf("missing required env var: %s", key)
	}
	return v
}

func getEnv(key, def string) string {
	v := os.Getenv(key)
	if v == "" {
		return def
	}
	return v
}

func firstNonEmpty(values ...string) string {
	for _, v := range values {
		if strings.TrimSpace(v) != "" {
			return v
		}
	}
	return ""
}
