package org.voting.user.domain.ports.inbound

import org.voting.user.domain.voter.VoterModel
import org.voting.user.domain.voter.dto.UpdateProfileDto

interface VoterUseCase {
    fun loginWithTelegram(telegramId: String): VoterModel
    fun updateProfile(data: UpdateProfileDto): VoterModel
    fun findByUserId(userId: String): VoterModel?
}
