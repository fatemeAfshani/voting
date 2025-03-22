package org.voting.usermanagement.adaptor.rest

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.voting.usermanagement.domain.creator.CreatorService
import org.voting.usermanagement.domain.creator.dto.RegisterDto

@RestController
@RequestMapping("/api/v1/creator")
class CreatorController(
    private val service: CreatorService
) {
    @PostMapping
    fun registerCreator(@RequestBody request: RegisterDto): ResponseEntity<String> {
        service.register(request)
        return ResponseEntity
            .ok(
                "user has been registered successfully.",
            )
    }
}