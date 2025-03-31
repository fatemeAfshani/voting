package org.voting.usermanagement.adaptor.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.voting.usermanagement.adaptor.api.dto.CreatorLoginResponse
import org.voting.usermanagement.adaptor.api.mapper.CreatorResponseMapper
import org.voting.usermanagement.domain.creator.CreatorService
import org.voting.usermanagement.domain.creator.dto.RegisterDto
import org.voting.usermanagement.domain.user.Roles

@RestController
@RequestMapping("/api/v1/creator")
class CreatorController(
    private val service: CreatorService,
    private val jwtUtil: JwtUtil
) {
    @PostMapping("/register")
    fun registerCreator(@RequestBody request: RegisterDto): ResponseEntity<String> {
        service.register(request)
        return ResponseEntity
            .ok(
                "user has been registered successfully.",
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
}
