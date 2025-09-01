package bot

import "sync"

type TokenStore struct {
	mu     sync.Mutex
	tokens map[int64]string
}

func NewTokenStore() *TokenStore {
	return &TokenStore{tokens: make(map[int64]string)}
}

func (s *TokenStore) SaveToken(telegramID int64, token string) {
	s.mu.Lock()
	defer s.mu.Unlock()
	s.tokens[telegramID] = token
}

func (s *TokenStore) GetToken(telegramID int64) (string, bool) {
	s.mu.Lock()
	defer s.mu.Unlock()
	t, ok := s.tokens[telegramID]
	return t, ok
}
