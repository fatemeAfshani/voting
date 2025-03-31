package org.voting.pollmanagement

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PollManagementApplication

fun main(args: Array<String>) {
    runApplication<PollManagementApplication>(*args)
}
