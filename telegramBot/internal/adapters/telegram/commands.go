package telegram

type StepMessage struct {
	TextID  string
	Options []Option
}

type Option string

const (
	MsgWelcome           = "prompt.welcome"
	MsgAskPhoneSignUp    = "prompt.ask_phone_signup"
	MsgAskPasswordSignUp = "prompt.ask_password_signup"
	MsgCreatorMenu       = "prompt.creator_menu"

	MsgUnknown         = "prompt.unknown"
	MsgRegisterSuccess = "signup.success"
	MsgRegisterError   = "register.error"

	MsgVoterMenu = "prompt.voter_menu"
)

const (
	OptionStart           Option = "/start"
	OptionCreator                = "creator"
	OptionVoter                  = "voter"
	OptionSignUp                 = "signup"
	OptionSignIn                 = "signin"
	OptionCreatePoll             = "createPoll"
	OptionViewPolls              = "viewPolls"
	OptionViewActivePolls        = "viewActivePolls"
	OptionCompleteProfile        = "completeVoterProfile"
	OptionPollHistory            = "viewPollHistory"
)

var StepWelcome = StepMessage{
	TextID:  MsgWelcome,
	Options: []Option{OptionCreator, OptionVoter},
}

var StepCreatorSignup = StepMessage{TextID: MsgAskPhoneSignUp}

var StepCreatorMenu = StepMessage{
	TextID:  MsgCreatorMenu,
	Options: []Option{OptionCreatePoll, OptionViewPolls},
}

var StepVoterMenu = StepMessage{
	TextID:  MsgVoterMenu,
	Options: []Option{OptionViewActivePolls, OptionCompleteProfile, OptionPollHistory},
}
