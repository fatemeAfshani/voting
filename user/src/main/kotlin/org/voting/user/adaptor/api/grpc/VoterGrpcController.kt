package org.voting.user.adaptor.api.grpc

import net.devh.boot.grpc.server.service.GrpcService
import org.voting.user.adaptor.api.JwtUtil
import org.voting.user.adaptor.api.interceptors.UserInterceptor
import org.voting.user.adaptor.api.mapper.UpdateVoterProfileMapper
import org.voting.user.domain.ports.inbound.VoterUseCase
import org.voting.user.domain.user.Roles
import user.User
import user.User.EmptyResponse
import user.User.UserIdRequest
import user.User.UserInfo
import user.User.VoterLoginResponse
import user.VoterServiceGrpcKt

@GrpcService
class VoterGrpcController(
    private val voterService: VoterUseCase,
    private val jwtUtil: JwtUtil,
) : VoterServiceGrpcKt.VoterServiceCoroutineImplBase() {

    override suspend fun loginWithTelegram(request: User.TelegramLoginRequest): VoterLoginResponse {
        val voter = voterService.loginWithTelegram(request.telegramId)
        val token = jwtUtil.generateToken(voter.userId!!, Roles.VOTER.name)

        return VoterLoginResponse.newBuilder()
            .setId(voter.userId)
            .setToken(token)
            .build()
    }

    override suspend fun updateProfile(request: User.UpdateVoterProfileRequest): EmptyResponse {
        val userId = UserInterceptor.USER_ID_KEY.get()
        val role = UserInterceptor.ROLE_KEY.get()

        voterService.updateProfile(UpdateVoterProfileMapper.mapper.protoToDto(request, userId, role))
        return EmptyResponse.newBuilder().build()
    }

    override suspend fun getById(request: UserIdRequest): UserInfo {
        val voter = voterService.findByUserId(request.userId)
            ?: return UserInfo.newBuilder().build()

        val preferences = mapOf(
            "city" to voter.city,
            "gender" to voter.gender?.name,
            "age" to voter.age?.toString(),
            "job" to voter.job,
            "educationLevel" to voter.educationLevel?.name,
            "fieldOfStudy" to voter.fieldOfStudy,
            "maritalStatus" to voter.maritalStatus?.name
        ).filterValues { it != null }
            .mapValues { it.value!! }

        return UserInfo.newBuilder()
            .setId(voter.userId)
            .putAllPreferences(preferences)
            .build()
    }
}
