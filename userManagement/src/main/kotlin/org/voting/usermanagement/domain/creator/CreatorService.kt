package org.voting.usermanagement.domain.creator

import org.voting.usermanagement.adaptor.persistance.entity.Creator
import org.voting.usermanagement.ports.inbound.CreatorUseCase

class CreatorService() : CreatorUseCase {
    override fun register() {
        TODO("Not yet implemented")
    }

    override fun login(username: String, password: String): Creator {
        TODO("Not yet implemented")
    }
}
