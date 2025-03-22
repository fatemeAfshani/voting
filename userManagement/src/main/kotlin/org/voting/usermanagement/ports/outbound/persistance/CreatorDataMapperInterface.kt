package org.voting.usermanagement.ports.outbound.persistance

import org.voting.usermanagement.adaptor.persistance.entity.Creator

interface CreatorDataMapperInterface {
    fun findById(id: String): List<Creator>

    fun save(creator: Creator)
}
