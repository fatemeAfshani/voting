package org.voting.user.adaptor.api.grpc

import net.devh.boot.grpc.server.service.GrpcService
import org.voting.user.adaptor.api.JwtUtil
import org.voting.user.domain.ports.inbound.VoterUseCase
import user.User
import user.User.VoterLoginResponse
import user.VoterServiceGrpcKt

@GrpcService
class VoterGrpcController(
    private val voterService: VoterUseCase,
    private val jwtUtil: JwtUtil,
) : VoterServiceGrpcKt.VoterServiceCoroutineImplBase() {

    override suspend fun loginWithTelegram(request: User.TelegramLoginRequest): VoterLoginResponse {
        val voter = voterService.loginWithTelegram(request.telegramId)
        val token = jwtUtil.generateToken(voter.id!!, org.voting.user.domain.user.Roles.VOTER.name)

        return VoterLoginResponse.newBuilder()
            .setId(voter.id)
            .setToken(token)
            .build()
    }
}
