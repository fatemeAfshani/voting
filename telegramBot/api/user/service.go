package user_api

import (
	"context"
	"github.com/yourname/telegram-bot/contract/user/golang"
	"google.golang.org/grpc"
)

type UserClient interface {
	SignIn(ctx context.Context, request SignInRequest) (SignInResponse, error)
	SignUp(ctx context.Context, request SignUpRequest) (SignUpResponse, error)
}

type Client struct {
	Conn *grpc.ClientConn
}

func New(conn *grpc.ClientConn) *Client {
	return &Client{
		Conn: conn,
	}
}

func (c *Client) SignIn(ctx context.Context, request SignInRequest) (SignInResponse, error) {
	client := golang.NewUserServiceClient(c.Conn)

	req := &golang.TelegramLoginRequest{
		TelegramId: request.TelegramId,
	}

	res, err := client.LoginWithTelegram(ctx, req)
	if err != nil || res == nil {
		return SignInResponse{}, err
	}

	return SignInResponse{
		Token: res.GetToken(),
		Id:    res.GetId(),
	}, nil
}

func (c *Client) SignUp(ctx context.Context, request SignUpRequest) (SignUpResponse, error) {
	client := golang.NewUserServiceClient(c.Conn)

	req := &golang.RegisterRequest{
		TelegramId: request.TelegramId,
		Phone:      request.Phone,
		Password:   request.Password,
	}

	res, err := client.Register(ctx, req)
	if err != nil || res == nil {
		return SignUpResponse{}, err
	}

	return SignUpResponse{
		Token: res.GetToken(),
		Id:    res.GetId(),
	}, nil
}
