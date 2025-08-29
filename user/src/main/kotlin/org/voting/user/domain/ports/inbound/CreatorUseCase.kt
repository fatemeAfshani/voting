package org.voting.user.domain.ports.inbound

import org.voting.user.domain.creator.CreatorModel
import org.voting.user.domain.creator.dto.RegisterDto

interface CreatorUseCase {
    fun register(registerDto: RegisterDto)
    fun login(registerDto: RegisterDto): CreatorModel
}
