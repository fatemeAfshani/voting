package org.voting.user.domain.creator

import org.springframework.stereotype.Service
import org.voting.user.adaptor.exception.Error
import org.voting.user.adaptor.exception.NotFoundException
import org.voting.user.adaptor.persistance.dataMapperImp.CreatorRepositoryImp
import org.voting.user.domain.creator.dto.RegisterDto
import org.voting.user.domain.ports.inbound.CreatorUseCase

@Service
class CreatorService(
    private val creatorRepositoryImp: CreatorRepositoryImp
) : CreatorUseCase {
    override fun register(registerDto: RegisterDto) {
        val (phone, password, userName) = registerDto
        val model = CreatorModel(phone = phone, password = password, userName = userName)
        creatorRepositoryImp.save(model)
    }

    override fun login(registerDto: RegisterDto): CreatorModel {
        val (phone, password) = registerDto
        val creator = phone?.let { creatorRepositoryImp.findByPhoneNumber(it) }
        if (creator == null || password != creator.password) {
            throw NotFoundException(Error.ErrorCodes.USER_NOT_FOUND.name)
        }
        return creator
    }
}
