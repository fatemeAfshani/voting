package service

import (
	"context"
	"github.com/fatemeAfshani/voting/internal/ports"
)

type Service struct {
	userGrpcClient ports.UserClient
}

func New(uClient ports.UserClient) Service {
	return Service{
		userGrpcClient: uClient,
	}
}

func (s Service) SignUp(ctx context.Context, phone, password, telegramId string) error {
	//todo should we send the plain password? or hash it?

	_, err := s.userGrpcClient.SignUp(ctx, ports.SignUpRequest{
		Phone:      phone,
		Password:   password,
		TelegramId: telegramId,
	})
	if err != nil {
		return err
	}

	return nil
}

func (s Service) SignIn(ctx context.Context, telegramId string) error {
	_, err := s.userGrpcClient.SignIn(ctx, ports.SignInRequest{
		TelegramId: telegramId,
	})
	if err != nil {
		return err
	}

	return nil
}
