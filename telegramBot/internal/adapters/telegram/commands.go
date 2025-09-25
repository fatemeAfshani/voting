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
	MsgUnknown           = "prompt.unknown"
	MsgSignInSuccess     = "signin.success"
	MsgSignInFailed      = "signin.failed"
	MsgSignUpSuccess     = "signup.success"
	MsgRegisterError     = "register.error"
)

const (
	OptionStart   Option = "/start"
	OptionCreator        = "option.creator"
	OptionVoter          = "option.voter"
	OptionSignUp         = "option.signup"
	OptionSignIn         = "option.signin"
)

var StepWelcome = StepMessage{
	TextID:  MsgWelcome,
	Options: []Option{OptionCreator, OptionVoter},
}

var StepSignInOrSignUp = StepMessage{
	TextID:  MsgSignInOrSignUp,
	Options: []Option{OptionSignUp, OptionSignIn},
}

var StepCreatorSignup = StepMessage{TextID: MsgAskPhoneSignUp}
var StepCreatorSignIn = StepMessage{TextID: MsgAskPhoneSignIn}
var StepVoterSignUp = StepMessage{TextID: MsgAskPhoneSignUp}
var StepVoterSignIn = StepMessage{TextID: MsgAskPhoneSignIn}
