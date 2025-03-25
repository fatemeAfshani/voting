package org.voting.usermanagement.adaptor.persistance.dataMapperImp

import org.springframework.stereotype.Component
import org.voting.usermanagement.adaptor.persistance.mapper.CreatorMapper
import org.voting.usermanagement.adaptor.persistance.repository.CreatorRepository
import org.voting.usermanagement.domain.creator.CreatorModel
import org.voting.usermanagement.ports.outbound.persistance.CreatorDataMapperInterface

@Component
class CreatorDataMapperImp(
    private val creatorRepository: CreatorRepository
) : CreatorDataMapperInterface {
    override fun findOneById(id: String): CreatorModel? {
        val entity = creatorRepository.findOneById(id)
        return CreatorMapper.mapper.entityToModel(entity)
    }

    override fun save(creator: CreatorModel) {
        val entity = CreatorMapper.mapper.modelToEntity(creator)
        entity?.let { creatorRepository.save(it) }
    }

    override fun findByPhoneNumber(phoneNumber: String): CreatorModel? {
        val entity = creatorRepository.findFirstByPhone(phoneNumber)
        return CreatorMapper.mapper.entityToModel(entity)
    }
}
