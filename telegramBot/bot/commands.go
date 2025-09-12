package bot

type StepMessage struct {
	Text    PromptText
	Options []Option
}

type Option string
type PromptText string

const (
	PromptWelcome        PromptText = "Welcome! Are you a *Creator* or *Voter*?"
	PromptSignInOrSignUp PromptText = "Do you want to *Sign up* or *Sign in*?"
	PromptAskPhoneSignUp PromptText = "Please send me your phone number (international format) to *Sign up*."
	PromptAskPhoneSignIn PromptText = "Please send me your phone number to *Sign in*."
	PromptUnknown        PromptText = "I didn’t understand. Use /start."
)

const (
	OptionStart   Option = "/start"
	OptionCreator Option = "creator"
	OptionVoter   Option = "voter"
	OptionSignUp  Option = "signup"
	OptionSignIn  Option = "signin"
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
