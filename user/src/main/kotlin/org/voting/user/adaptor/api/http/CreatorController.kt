package org.voting.user.adaptor.api.http

import io.micrometer.core.instrument.MeterRegistry
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.voting.user.adaptor.api.JwtUtil
import org.voting.user.adaptor.api.dto.CreatorLoginResponse
import org.voting.user.adaptor.api.mapper.CreatorResponseMapper
import org.voting.user.domain.creator.CreatorService
import org.voting.user.domain.creator.dto.RegisterDto
import org.voting.user.domain.creator.dto.TelegramLoginRequest
import org.voting.user.domain.user.Roles

@RestController
@RequestMapping("/api/v1/creator")
class CreatorController(
    private val service: CreatorService,
    private val jwtUtil: JwtUtil,
    private val meterRegistry: MeterRegistry
) {
    @PostMapping("/register")
    fun registerCreator(@RequestBody request: RegisterDto): ResponseEntity<CreatorLoginResponse> {
        val creator = service.register(request)
        meterRegistry.counter("register_counter", "register", "/api/v1/creator/register").increment()
        val token = jwtUtil.generateToken(creator.id!!, Roles.CREATOR.name)
        return ResponseEntity
            .ok(
                CreatorResponseMapper.mapper.modelToDto(creator, token)
            )
    }

    @PostMapping("/login")
    fun loginCreator(@RequestBody request: RegisterDto): ResponseEntity<CreatorLoginResponse> {
        val creator = service.login(request)

        val token = jwtUtil.generateToken(creator.id!!, Roles.CREATOR.name)
        return ResponseEntity
            .ok(
                CreatorResponseMapper.mapper.modelToDto(creator, token)
            )

    }

    @PostMapping("/login-telegram")
    fun loginWithTelegram(@RequestBody request: TelegramLoginRequest): ResponseEntity<CreatorLoginResponse> {
        val creator = service.loginWithTelegram(request.telegramId)
        val token = jwtUtil.generateToken(creator.id!!, Roles.CREATOR.name)
        return ResponseEntity.ok(
            CreatorResponseMapper.mapper.modelToDto(creator, token)
        )
    }

}

