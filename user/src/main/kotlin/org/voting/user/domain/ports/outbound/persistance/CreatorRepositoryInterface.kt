package org.voting.user.domain.ports.outbound.persistance

import org.voting.user.domain.creator.CreatorModel

interface CreatorRepositoryInterface {
    fun findOneById(id: String): CreatorModel?

    fun save(creator: CreatorModel): CreatorModel
    fun findByPhoneNumber(phoneNumber: String): CreatorModel?
}
