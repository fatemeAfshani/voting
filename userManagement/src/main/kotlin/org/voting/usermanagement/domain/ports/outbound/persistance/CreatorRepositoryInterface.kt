package org.voting.usermanagement.domain.ports.outbound.persistance

import org.voting.usermanagement.domain.creator.CreatorModel

interface CreatorRepositoryInterface {
    fun findOneById(id: String): CreatorModel?

    fun save(creator: CreatorModel)
    fun findByPhoneNumber(phoneNumber: String): CreatorModel?
}
