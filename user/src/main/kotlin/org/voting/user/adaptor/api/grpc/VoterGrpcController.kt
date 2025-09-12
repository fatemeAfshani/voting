package org.voting.user.adaptor.api.grpc

import net.devh.boot.grpc.server.service.GrpcService
import org.voting.user.adaptor.api.JwtUtil
import org.voting.user.adaptor.api.interceptors.UserInterceptor
import org.voting.user.adaptor.api.mapper.UpdateVoterProfileMapper
import org.voting.user.domain.ports.inbound.VoterUseCase
import user.User
import user.User.EmptyResponse
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

    override suspend fun updateProfile(request: User.UpdateVoterProfileRequest): EmptyResponse {
        val userId = UserInterceptor.USER_ID_KEY.get()
        val role = org.voting.user.domain.user.Roles.VOTER.name

         voterService.updateProfile(UpdateVoterProfileMapper.mapper.protoToDto(request, userId, role))
        return EmptyResponse.newBuilder().build()
    }
}
