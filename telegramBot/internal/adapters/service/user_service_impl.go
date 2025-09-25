package service

import (
	"context"
	"fmt"
	"github.com/fatemeAfshani/voting/internal/config"
	"github.com/fatemeAfshani/voting/internal/ports"
	"github.com/fatemeAfshani/voting/proto/user/golang"
	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials/insecure"
)

type UserServiceImp struct {
	creatorClient golang.CreatorServiceClient
	voterClient   golang.VoterServiceClient
	conn          *grpc.ClientConn // keep connection to Close() later
}

func NewUserService(cfg config.GRPCConfig) (*UserServiceImp, error) {
	address := fmt.Sprintf("%s:%d", cfg.Host, cfg.Port)

	conn, err := grpc.NewClient(address, grpc.WithTransportCredentials(insecure.NewCredentials()))
	if err != nil {
		return nil, fmt.Errorf("failed to connect to gRPC server at %s: %w", address, err)
	}

	fmt.Printf("Connected gRPC client: %T\n", golang.NewCreatorServiceClient(conn))

	return &UserServiceImp{
		creatorClient: golang.NewCreatorServiceClient(conn),
		voterClient:   golang.NewVoterServiceClient(conn),
		conn:          conn,
	}, nil
}

func (c *UserServiceImp) CreatorSignIn(ctx context.Context, request ports.SignInRequest) (ports.SignInResponse, error) {
	req := &golang.TelegramLoginRequest{
		TelegramId: request.TelegramId,
	}

	res, err := c.creatorClient.LoginWithTelegram(ctx, req)
	if err != nil || res == nil {
		return ports.SignInResponse{}, err
	}

	return ports.SignInResponse{
		Token: res.GetToken(),
		Id:    res.GetId(),
	}, nil
}

func (c *UserServiceImp) VoterSignIn(ctx context.Context, request ports.SignInRequest) (ports.SignInResponse, error) {
	req := &golang.TelegramLoginRequest{
		TelegramId: request.TelegramId,
	}

	res, err := c.voterClient.LoginWithTelegram(ctx, req, []grpc.CallOption{}...)
	if err != nil || res == nil {
		return ports.SignInResponse{}, err
	}

	return ports.SignInResponse{
		Token: res.GetToken(),
		Id:    res.GetId(),
	}, nil
}
