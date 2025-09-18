package org.voting.poll.domain.ports.outbound.services

interface UserServiceInterface {
    fun getUserPreferences(userId: String): Map<String, String>
}
