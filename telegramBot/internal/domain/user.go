package domain

import (
	"context"
	"github.com/fatemeAfshani/voting/internal/adapters/service"
	log "github.com/fatemeAfshani/voting/internal/infra/logger"
	"github.com/fatemeAfshani/voting/internal/ports"
)

type UserDomain struct {
	userService *service.UserServiceImp
	logger      log.Logger
}

func NewUserDomain(userService *service.UserServiceImp, logger log.Logger) *UserDomain {
	return &UserDomain{
		userService: userService,
		logger:      logger,
	}
}

func (d *UserDomain) AutoSignIn(ctx context.Context, session *UserSession) (bool, error) {
	if session.TelegramID != "" {
		response, err := d.userService.CreatorSignIn(ctx, ports.SignInRequest{TelegramId: session.TelegramID})
		if err != nil {
			d.logger.Error().Msg("unable to signIn automatically")
			return false, err
		}
		session.UserToken = response.Token
		return true, err
	}
	return false, nil
}
