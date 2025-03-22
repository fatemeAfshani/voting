package org.voting.usermanagement.domain.creator

import org.springframework.stereotype.Service
import org.voting.usermanagement.adaptor.persistance.dataMapperImp.CreatorDataMapperImp
import org.voting.usermanagement.adaptor.persistance.entity.Creator
import org.voting.usermanagement.domain.creator.dto.RegisterDto
import org.voting.usermanagement.ports.inbound.CreatorUseCase

@Service
class CreatorService(
    private val creatorDataMapperImp: CreatorDataMapperImp
) : CreatorUseCase {
    override fun register(registerDto: RegisterDto) {
        val (phoneNumber, password) = registerDto
        val creatorModel = CreatorModel(phone = phoneNumber, password = password)
        creatorDataMapperImp.save(creatorModel)
    }

    override fun login(username: String, password: String): Creator {
        TODO("Not yet implemented")
    }
}
