package org.voting.usermanagement.ports.inbound

import org.voting.usermanagement.adaptor.persistance.entity.Creator
import org.voting.usermanagement.domain.creator.dto.RegisterDto

interface CreatorUseCase {
    fun register(registerDto: RegisterDto)
    fun login(username: String, password: String): Creator
}