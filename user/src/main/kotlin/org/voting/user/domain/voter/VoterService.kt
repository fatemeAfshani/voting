package org.voting.user.domain.voter

import org.springframework.stereotype.Service
import org.voting.user.adaptor.exception.Errors
import org.voting.user.adaptor.exception.ForbiddenException
import org.voting.user.adaptor.exception.NotFoundException
import org.voting.user.domain.ports.inbound.VoterUseCase
import org.voting.user.domain.ports.outbound.persistance.VoterRepositoryInterface
import org.voting.user.domain.user.Roles
import org.voting.user.domain.voter.dto.UpdateProfileDto
import java.util.*

@Service
class VoterService(
    private val voterRepository: VoterRepositoryInterface
) : VoterUseCase {

    override fun loginWithTelegram(telegramId: String): VoterModel {
        val existingVoter = voterRepository.findByTelegramId(telegramId)
        return if (existingVoter != null) {
            existingVoter
        } else {
            val newVoter = VoterModel(telegramId = telegramId, userId = UUID.randomUUID().toString())
            voterRepository.save(newVoter)
        }
    }

    override fun updateProfile(
        data: UpdateProfileDto
    ): VoterModel {
        if (data.userRole != Roles.VOTER || data.userId == null) {
            throw ForbiddenException(Errors.ErrorCodes.FORBIDDEN.name)
        }

        val user = voterRepository.findByUserId(data.userId)
            ?: throw NotFoundException(Errors.ErrorCodes.USER_NOT_FOUND.name)

        user.updateUserProfile(data)
        user.validate()

        return voterRepository.save(user)
    }

    override fun findByUserId(userId: String): VoterModel? {
        return voterRepository.findByUserId(userId)
    }
}
