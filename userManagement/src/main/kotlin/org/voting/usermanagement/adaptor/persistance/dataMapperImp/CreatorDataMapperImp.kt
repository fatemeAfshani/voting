package org.voting.usermanagement.adaptor.persistance.dataMapperImp

import org.voting.usermanagement.adaptor.persistance.entity.Creator
import org.voting.usermanagement.adaptor.persistance.repository.CreatorRepository
import org.voting.usermanagement.ports.outbound.persistance.CreatorDataMapperInterface

class CreatorDataMapperImp(
    private val creatorRepository: CreatorRepository
) : CreatorDataMapperInterface {
    override fun findById(id: String): List<Creator> {
        TODO("Not yet implemented")
    }

    override fun save(creator: Creator) {
        TODO("Not yet implemented")
    }
}
