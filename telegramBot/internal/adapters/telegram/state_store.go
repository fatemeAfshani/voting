package telegram

import (
	"strconv"
	"sync"
)

type Role string

const (
	Creator Role = "creator"
	Voter        = "voter"
)

type UserState string

const (
	StateNone          UserState = ""
	StateChoosingRole            = "choosing_role"
	StateChoosingAuth            = "choosing_auth"
	StateAwaitPhone              = "await_phone"
	StateAwaitPassword           = "await_password"
)

type UserSession struct {
	Role       Role
	Auth       Option
	State      UserState
	Phone      string
	Password   string
	TelegramID string
}

type TokenStore struct {
	mu       sync.Mutex
	sessions map[int64]*UserSession
}

func NewTokenStore() *TokenStore {
	return &TokenStore{
		sessions: make(map[int64]*UserSession),
	}
}

func (ts *TokenStore) GetOrCreate(chatID int64) *UserSession {
	defer ts.mu.Unlock()

	ts.mu.Lock()
	if s, ok := ts.sessions[chatID]; ok {
		return s
	}
	s := &UserSession{TelegramID: strconv.FormatInt(chatID, 10), State: StateNone}
	ts.sessions[chatID] = s
	return s
}

func (ts *TokenStore) Set(chatID int64, session *UserSession) {
	defer ts.mu.Unlock()

	ts.mu.Lock()
	ts.sessions[chatID] = session
}
