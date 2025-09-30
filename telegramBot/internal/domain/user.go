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

func (d *UserDomain) RegisterCreator(ctx context.Context, session *UserSession) (bool, error) {
	if session.Phone != "" && session.Password != "" {
		response, err := d.userService.CreatorRegister(ctx, ports.SignUpRequest{Phone: session.Phone, Password: session.Password, TelegramId: session.TelegramID})
		if err != nil {
			d.logger.Error().Msg("unable to signIn automatically")
			return false, err
		}
		session.UserToken = response.Token
		return true, err
	}
	return false, nil
}

func (d *UserDomain) VoterSignIn(ctx context.Context, session *UserSession) (bool, error) {
	if session.TelegramID != "" {
		response, err := d.userService.VoterSignIn(ctx, ports.SignInRequest{TelegramId: session.TelegramID})
		if err != nil {
			d.logger.Error().Msg("unable to signIn voter automatically")
			return false, err
		}
		session.UserToken = response.Token
		return true, err
	}
	return false, nil
}
