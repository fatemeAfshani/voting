package ports

import "context"

type UserService interface {
	CreatorSignIn(ctx context.Context, request SignInRequest) (SignInResponse, error)
	//SignUp(ctx context.Context, request SignUpRequest) (SignUpResponse, error)
}

type SignInRequest struct {
	TelegramId string
}

type SignInResponse struct {
	Token string
	Id    string
}

type SignUpRequest struct {
	Phone      string
	Password   string
	TelegramId string
}

type SignUpResponse struct {
	Token string
	Id    string
}
