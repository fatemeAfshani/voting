package service

import userapi "github.com/fatemeAfshani/voting/api/user"

type Service struct {
	userGrpcClient userapi.UserClient
}

func New(uClient userapi.UserClient) Service {
	return Service{
		userGrpcClient: uClient,
	}
}
