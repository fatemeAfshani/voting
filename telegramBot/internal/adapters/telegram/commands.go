package telegram

type StepMessage struct {
	TextID  string
	Options []Option
}

type Option string

const (
	MsgWelcome           = "prompt.welcome"
	MsgSignInOrSignUp    = "prompt.signin_or_signup"
	MsgAskPhoneSignUp    = "prompt.ask_phone_signup"
	MsgAskPasswordSignUp = "prompt.ask_password_signup"
	MsgAskPhoneSignIn    = "prompt.ask_phone_signin"
	MsgCreatorMenu       = "prompt.creator_menu"
	MsgUnknown           = "prompt.unknown"
	MsgRegisterSuccess   = "signup.success"
	MsgRegisterError     = "register.error"
)

const (
	OptionStart      Option = "/start"
	OptionCreator           = "creator"
	OptionVoter             = "voter"
	OptionSignUp            = "signup"
	OptionSignIn            = "signin"
	OptionCreatePoll        = "createPoll"
	OptionViewPolls         = "viewPolls"
)

var StepWelcome = StepMessage{
	TextID:  MsgWelcome,
	Options: []Option{OptionCreator, OptionVoter},
}

var StepCreatorMenu = StepMessage{
	TextID:  MsgCreatorMenu,
	Options: []Option{OptionCreatePoll, OptionViewPolls},
}

var StepCreatorSignup = StepMessage{TextID: MsgAskPhoneSignUp}
var StepVoterSignIn = StepMessage{TextID: MsgAskPhoneSignIn}
