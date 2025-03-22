package org.voting.usermanagement.ports.outbound.persistance

import org.voting.usermanagement.domain.creator.CreatorModel

interface CreatorDataMapperInterface {
    fun findOneById(id: String): CreatorModel?

    fun save(creator: CreatorModel)
}
