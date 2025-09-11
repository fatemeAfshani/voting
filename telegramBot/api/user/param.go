package userapi

type SignInRequest struct {
	TelegramId string
}

type SignInResponse struct {
	Token string
	Id    string
}

type SignUpRequest struct {
	Phone      string
	Password   string
	TelegramId string
}

type SignUpResponse struct {
	Token string
	Id    string
}
