package org.voting.user.adaptor.api.grpc

import net.devh.boot.grpc.server.service.GrpcService
import org.voting.user.adaptor.api.JwtUtil
import org.voting.user.adaptor.exception.Errors
import org.voting.user.adaptor.exception.InternalException
import org.voting.user.domain.creator.dto.RegisterDto
import org.voting.user.domain.ports.inbound.CreatorUseCase
import org.voting.user.domain.user.Roles
import user.CreatorServiceGrpcKt
import user.User.CreatorLoginResponse
import user.User.RegisterRequest
import user.User.TelegramLoginRequest

@GrpcService
class CreatorGrpcController(
    private val service: CreatorUseCase,
    private val jwtUtil: JwtUtil,
) : CreatorServiceGrpcKt.CreatorServiceCoroutineImplBase() {

    override suspend fun register(request: RegisterRequest): CreatorLoginResponse {
        val created = service.register(
            RegisterDto(
                phone = request.phone,
                password = request.password,
                telegramId = request.telegramId
            )
        )
        val userId = created.userId ?: throw InternalException(Errors.ErrorCodes.INTERNAL_ERROR.name)

        val token = jwtUtil.generateToken(userId, Roles.CREATOR.name)
        return CreatorLoginResponse.newBuilder()
            .setId(userId)
            .setToken(token)
            .build()
    }

    override suspend fun login(request: RegisterRequest): CreatorLoginResponse {
        val creator = service.login(
            RegisterDto(
                phone = request.phone,
                password = request.password,
                telegramId = request.telegramId
            )
        )
        val userId = creator.userId ?: throw InternalException(Errors.ErrorCodes.INTERNAL_ERROR.name)

        val token = jwtUtil.generateToken(userId, Roles.CREATOR.name)
        return CreatorLoginResponse.newBuilder()
            .setId(userId)
            .setToken(token)
            .build()
    }

    override suspend fun loginWithTelegram(request: TelegramLoginRequest): CreatorLoginResponse {
        val creator = service.loginWithTelegram(request.telegramId)
        val userId = creator.userId ?: throw InternalException(Errors.ErrorCodes.INTERNAL_ERROR.name)

        val token = jwtUtil.generateToken(userId, Roles.CREATOR.name)
        return CreatorLoginResponse.newBuilder()
            .setId(userId)
            .setToken(token)
            .build()
    }
}
