package config

import (
	goLog "log"
	"strings"

	"github.com/knadh/koanf/parsers/yaml"
	"github.com/knadh/koanf/providers/env"
	"github.com/knadh/koanf/providers/file"
	"github.com/knadh/koanf/v2"
)

type Option struct {
	Prefix       string
	Delimiter    string
	Separator    string
	YamlFilePath string
	CallbackEnv  func(string) string
}

func defaultCallbackEnv(source, prefix, separator string) string {
	base := strings.ToLower(strings.TrimPrefix(source, prefix))
	return strings.ReplaceAll(base, separator, ".")
}

func Load(options Option, config any) error {
	k := koanf.New(options.Delimiter)

	if options.YamlFilePath != "" {
		if err := k.Load(file.Provider(options.YamlFilePath), yaml.Parser()); err != nil {
			goLog.Fatalf("Error loading config file")
			return err
		}
	}

	callback := options.CallbackEnv
	if callback == nil {
		callback = func(source string) string {
			return defaultCallbackEnv(source, options.Prefix, options.Separator)
		}
	}

	if err := k.Load(env.Provider(options.Prefix, options.Delimiter, callback), nil); err != nil {
		goLog.Fatalf("Error loading environment variables")
		return err
	}

	if err := k.Unmarshal("", &config); err != nil {
		goLog.Fatalf("Error unmarshaling config: %v", err)
		return err
	}

	return nil
}
