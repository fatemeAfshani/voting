package org.voting.usermanagement.ports.inbound

import org.voting.usermanagement.adaptor.persistance.entity.Creator

interface CreatorUseCase {
    fun register()

    fun login(username: String, password: String): Creator
}