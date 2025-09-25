package grpc

import (
	"context"
	"github.com/fatemeAfshani/voting/internal/ports"
	"github.com/fatemeAfshani/voting/protos/user/golang"
	"google.golang.org/grpc"
)

type Client struct {
	Conn *grpc.ClientConn
}

func New(conn *grpc.ClientConn) *Client {
	return &Client{
		Conn: conn,
	}
}

func (c *Client) SignIn(ctx context.Context, request ports.SignInRequest) (ports.SignInResponse, error) {
	client := golang.NewUserServiceClient(c.Conn)

	req := &golang.TelegramLoginRequest{
		TelegramId: request.TelegramId,
	}

	res, err := client.LoginWithTelegram(ctx, req)
	if err != nil || res == nil {
		return ports.SignInResponse{}, err
	}

	return ports.SignInResponse{
		Token: res.GetToken(),
		Id:    res.GetId(),
	}, nil
}

func (c *Client) SignUp(ctx context.Context, request ports.SignUpRequest) (ports.SignUpResponse, error) {
	client := golang.NewUserServiceClient(c.Conn)

	req := &golang.RegisterRequest{
		TelegramId: request.TelegramId,
		Phone:      request.Phone,
		Password:   request.Password,
	}

	res, err := client.Register(ctx, req)
	if err != nil || res == nil {
		return ports.SignUpResponse{}, err
	}

	return ports.SignUpResponse{
		Token: res.GetToken(),
		Id:    res.GetId(),
	}, nil
}
