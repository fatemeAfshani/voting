package org.voting.user.domain.creator

import org.springframework.stereotype.Service
import org.voting.user.adaptor.exception.Errors
import org.voting.user.adaptor.exception.NotFoundException
import org.voting.user.adaptor.persistance.dataMapperImp.CreatorRepositoryImp
import org.voting.user.domain.creator.dto.RegisterDto
import org.voting.user.domain.ports.inbound.CreatorUseCase

@Service
class CreatorService(
    private val creatorRepositoryImp: CreatorRepositoryImp
) : CreatorUseCase {
    override fun register(registerDto: RegisterDto): CreatorModel {
        val (phone, password, telegramId) = registerDto
        val model = CreatorModel(phone = phone, password = password, telegramId = telegramId)
        val creator = creatorRepositoryImp.save(model)
        return creator
    }

    override fun login(registerDto: RegisterDto): CreatorModel {
        val (phone, password) = registerDto
        val creator = phone?.let { creatorRepositoryImp.findByPhoneNumber(it) }
        if (creator == null || password != creator.password) {
            throw NotFoundException(Errors.ErrorCodes.USER_NOT_FOUND.name)
        }
        return creator
    }

    override fun loginWithTelegram(telegramId: String): CreatorModel {
        val creator = creatorRepositoryImp.findByTelegramId(telegramId)
            ?: throw NotFoundException(Errors.ErrorCodes.USER_NOT_FOUND.name)
        return creator
    }

}
