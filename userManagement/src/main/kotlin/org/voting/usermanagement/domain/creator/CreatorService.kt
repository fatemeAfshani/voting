package org.voting.usermanagement.domain.creator

import org.springframework.stereotype.Service
import org.voting.usermanagement.adaptor.persistance.dataMapperImp.CreatorDataMapperImp
import org.voting.usermanagement.domain.creator.dto.RegisterDto
import org.voting.usermanagement.ports.inbound.CreatorUseCase

@Service
class CreatorService(
    private val creatorDataMapperImp: CreatorDataMapperImp
) : CreatorUseCase {
    override fun register(registerDto: RegisterDto) {
        val (phoneNumber, password) = registerDto
        val model = CreatorModel(phone = phoneNumber, password = password)
        creatorDataMapperImp.save(model)
    }

    override fun login(registerDto: RegisterDto): Pair<CreatorModel, String> {
        val (phoneNumber, password) = registerDto
        val creator = phoneNumber?.let { creatorDataMapperImp.findByPhoneNumber(it) }
        if(creator == null || password != creator.password) {
            //throw error
        }
        val token = "random token"
        return creator
    }
}
