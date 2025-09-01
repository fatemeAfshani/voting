package api

import (
	"bytes"
	"encoding/json"
	"fmt"
	"net/http"
)

type Client struct {
	BaseURL string
}

func NewClient(baseURL string) *Client {
	return &Client{BaseURL: baseURL}
}

func (c *Client) Signup(phone, password, username string, telegramID int64) (string, error) {
	body := map[string]interface{}{
		"phone":       phone,
		"password":    password,
		"username":    username,
		"telegram_id": telegramID,
	}
	return c.postAndExtractToken("/signup", body)
}

func (c *Client) Signin(phone, password string) (string, error) {
	body := map[string]interface{}{
		"phone":    phone,
		"password": password,
	}
	return c.postAndExtractToken("/signin", body)
}

func (c *Client) CreatePoll(token, title, desc string) error {
	body := map[string]interface{}{
		"title":       title,
		"description": desc,
	}
	b, _ := json.Marshal(body)
	req, _ := http.NewRequest("POST", c.BaseURL+"/polls", bytes.NewBuffer(b))
	req.Header.Set("Authorization", "Bearer "+token)
	req.Header.Set("Content-Type", "application/json")

	resp, err := http.DefaultClient.Do(req)
	if err != nil {
		return err
	}
	defer resp.Body.Close()
	if resp.StatusCode != 200 {
		return fmt.Errorf("create poll failed: %d", resp.StatusCode)
	}
	return nil
}

// helper
func (c *Client) postAndExtractToken(path string, body map[string]interface{}) (string, error) {
	b, _ := json.Marshal(body)
	resp, err := http.Post(c.BaseURL+path, "application/json", bytes.NewBuffer(b))
	if err != nil {
		return "", err
	}
	defer resp.Body.Close()

	var result map[string]interface{}
	if err := json.NewDecoder(resp.Body).Decode(&result); err != nil {
		return "", err
	}
	token, ok := result["token"].(string)
	if !ok {
		return "", fmt.Errorf("no token in response")
	}
	return token, nil
}
