package org.voting.user.domain.ports.inbound

import org.voting.user.domain.voter.VoterModel

interface VoterUseCase {
    fun loginWithTelegram(telegramId: String): VoterModel
}
