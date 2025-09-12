package org.voting.user.adaptor.api.grpc

import net.devh.boot.grpc.server.service.GrpcService
import org.voting.user.adaptor.api.JwtUtil
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
        val token = jwtUtil.generateToken(created.id!!, Roles.CREATOR.name)
        return CreatorLoginResponse.newBuilder()
            .setId(created.id)
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
        val token = jwtUtil.generateToken(creator.id!!, Roles.CREATOR.name)
        return CreatorLoginResponse.newBuilder()
            .setId(creator.id)
            .setToken(token)
            .build()
    }

    override suspend fun loginWithTelegram(request: TelegramLoginRequest): CreatorLoginResponse {
        val creator = service.loginWithTelegram(request.telegramId)
        val token = jwtUtil.generateToken(creator.id!!, Roles.CREATOR.name)
        return CreatorLoginResponse.newBuilder()
            .setId(creator.id)
            .setToken(token)
            .build()
    }
}
