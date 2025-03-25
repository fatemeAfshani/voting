package org.voting.usermanagement.ports.inbound

import org.voting.usermanagement.domain.creator.CreatorModel
import org.voting.usermanagement.domain.creator.dto.RegisterDto

interface CreatorUseCase {
    fun register(registerDto: RegisterDto)
    fun login(registerDto: RegisterDto): CreatorModel
}
