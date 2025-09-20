package service

import (
	"context"
	"github.com/fatemeAfshani/voting/api/user"
)

type Service struct {
	userGrpcClient userapi.UserClient
}

func New(uClient userapi.UserClient) Service {
	return Service{
		userGrpcClient: uClient,
	}
}

func (s Service) SignUp(ctx context.Context, phone, password, telegramId string) error {
	//todo should we send the plain password? or hash it?

	_, err := s.userGrpcClient.SignUp(ctx, userapi.SignUpRequest{
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
	_, err := s.userGrpcClient.SignIn(ctx, userapi.SignInRequest{
		TelegramId: telegramId,
	})
	if err != nil {
		return err
	}

	return nil
}
