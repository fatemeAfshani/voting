package org.voting.pollmanagement.adaptor.infrastructure

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class LoggingConfiguration {

    @Bean
    fun logger(): Logger {
        return LoggerFactory.getLogger("pollManagement.logging")
    }
}