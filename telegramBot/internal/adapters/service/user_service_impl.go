package service

import (
	"context"
	"fmt"
	"github.com/fatemeAfshani/voting/internal/config"
	log "github.com/fatemeAfshani/voting/internal/infra/logger"
	"github.com/fatemeAfshani/voting/internal/ports"
	"github.com/fatemeAfshani/voting/proto/user/golang"
	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials/insecure"
	"time"
)

type UserServiceImp struct {
	creatorClient golang.CreatorServiceClient
	voterClient   golang.VoterServiceClient
	conn          *grpc.ClientConn
	logger        log.Logger
}

func NewUserService(cfg config.GRPCConfig, logger log.Logger) (*UserServiceImp, error) {
	address := fmt.Sprintf("%s:%d", cfg.Host, cfg.Port)

	conn, err := grpc.NewClient(address, grpc.WithTransportCredentials(insecure.NewCredentials()))
	if err != nil {
		logger.Error().Msgf("failed to connect to gRPC server at %s: %w", address, err)
		return nil, err
	}

	return &UserServiceImp{
		creatorClient: golang.NewCreatorServiceClient(conn),
		voterClient:   golang.NewVoterServiceClient(conn),
		conn:          conn,
		logger:        logger,
	}, nil
}

func (c *UserServiceImp) CreatorSignIn(ctx context.Context, request ports.SignInRequest) (ports.SignInResponse, error) {
	rpcCtx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	req := &golang.TelegramLoginRequest{
		TelegramId: request.TelegramId,
	}

	res, err := c.creatorClient.LoginWithTelegram(rpcCtx, req)
	if err != nil {
		return ports.SignInResponse{}, err
	}
	if res == nil {
		return ports.SignInResponse{}, fmt.Errorf("nil LoginWithTelegram response")
	}
	return ports.SignInResponse{
		Token: res.GetToken(),
		Id:    res.GetId(),
	}, nil
}

func (c *UserServiceImp) CreatorRegister(ctx context.Context, request ports.SignUpRequest) (ports.SignUpResponse, error) {
	rpcCtx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	req := &golang.RegisterRequest{
		Phone:      request.Phone,
		Password:   request.Password,
		TelegramId: request.TelegramId,
	}

	res, err := c.creatorClient.Register(rpcCtx, req)
	if err != nil {
		return ports.SignUpResponse{}, err
	}
	if res == nil {
		return ports.SignUpResponse{}, fmt.Errorf("nil Register response")
	}

	return ports.SignUpResponse{
		Token: res.GetToken(),
		Id:    res.GetId(),
	}, nil
}
