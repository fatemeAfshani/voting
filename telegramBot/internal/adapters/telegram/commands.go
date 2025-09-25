package telegram

type StepMessage struct {
	Text    PromptText
	Options []Option
}

type Option string
type PromptText string

const (
	PromptWelcome           PromptText = "Welcome! Are you a *Creator* or *Voter*?"
	PromptSignInOrSignUp               = "Do you want to *Sign up* or *Sign in*?"
	PromptAskPhoneSignUp               = "Please send me your phone number (international format) to *Sign up*."
	PromptAskPasswordSignUp            = "Now, please send me a *password* for your account."
	PromptAskPhoneSignIn               = "Please send me your phone number to *Sign in*."
	PromptUnknown                      = "I didn’t understand. Use /start."
)

const (
	OptionStart   Option = "/start"
	OptionCreator        = "creator"
	OptionVoter          = "voter"
	OptionSignUp         = "signup"
	OptionSignIn         = "signin"
)

var StepWelcome = StepMessage{
	Text:    PromptWelcome,
	Options: []Option{OptionCreator, OptionVoter},
}

var StepSignInOrSignUp = StepMessage{
	Text:    PromptSignInOrSignUp,
	Options: []Option{OptionSignUp, OptionSignIn},
}

var StepCreatorSignup = StepMessage{
	Text:    PromptAskPhoneSignUp,
	Options: nil,
}

var StepCreatorSignIn = StepMessage{
	Text:    PromptAskPhoneSignIn,
	Options: nil,
}

var StepVoterSignUp = StepMessage{
	Text:    PromptAskPhoneSignUp,
	Options: nil,
}

var StepVoterSignIn = StepMessage{
	Text:    PromptAskPhoneSignIn,
	Options: nil,
}
