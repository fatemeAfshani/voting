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

func (s Service) SignUp(phone, password, telegramId string) error {
	//todo we should get this from previous layer...
	ctx := context.Background()

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

func (s Service) SignIn(telegramId string) error {
	//todo we should get this from previous layer...
	ctx := context.Background()

	_, err := s.userGrpcClient.SignIn(ctx, userapi.SignInRequest{
		TelegramId: telegramId,
	})
	if err != nil {
		return err
	}

	return nil
}
