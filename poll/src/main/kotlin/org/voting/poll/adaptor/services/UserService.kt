package org.voting.poll.adaptor.services

import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component
import org.voting.poll.domain.ports.outbound.services.UserServiceInterface
import user.User
import user.VoterServiceGrpcKt

@Component
class UserService(
    private val voterServiceStub: VoterServiceGrpcKt.VoterServiceCoroutineStub

) : UserServiceInterface {
    override fun getUserPreferences(userId: String): Map<String, String> {
        val userPrefs: Map<String, String> = runBlocking {
            val resp = voterServiceStub.getById(User.UserIdRequest.newBuilder().setUserId(userId).build())
            resp.preferencesMap
        }

        return userPrefs
    }
}
