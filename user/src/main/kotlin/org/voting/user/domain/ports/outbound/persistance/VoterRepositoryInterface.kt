package org.voting.user.domain.ports.outbound.persistance

import org.voting.user.domain.voter.VoterModel

interface VoterRepositoryInterface {
    fun findByTelegramId(telegramId: String): VoterModel?
    fun findByUserId(userId: String): VoterModel?

    fun save(voter: VoterModel): VoterModel
}
