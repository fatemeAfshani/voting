package org.voting.user.domain.voter

import org.springframework.stereotype.Service
import org.voting.user.domain.ports.inbound.VoterUseCase
import org.voting.user.domain.ports.outbound.persistance.VoterRepositoryInterface

@Service
class VoterService(
    private val voterRepository: VoterRepositoryInterface
) : VoterUseCase {

    override fun loginWithTelegram(telegramId: String): VoterModel {
        val existingVoter = voterRepository.findByTelegramId(telegramId)
        return if (existingVoter != null) {
            existingVoter
        } else {
            val newVoter = VoterModel(telegramId = telegramId)
            voterRepository.save(newVoter)
        }
    }
}
