package org.voting.usermanagement.adaptor.persistance.dataMapperImp

import org.springframework.stereotype.Component
import org.voting.usermanagement.adaptor.persistance.mapper.CreatorMapper
import org.voting.usermanagement.adaptor.persistance.repository.MongoCreatorRepository
import org.voting.usermanagement.domain.creator.CreatorModel
import org.voting.usermanagement.domain.ports.outbound.persistance.CreatorRepositoryInterface

@Component
class CreatorRepositoryImp(
    private val mongoCreatorRepository: MongoCreatorRepository
) : CreatorRepositoryInterface {
    override fun findOneById(id: String): CreatorModel? {
        val entity = mongoCreatorRepository.findOneById(id)
        return CreatorMapper.mapper.entityToModel(entity)
    }

    override fun save(creator: CreatorModel) {
        val entity = CreatorMapper.mapper.modelToEntity(creator)
        entity?.let { mongoCreatorRepository.save(it) }
    }

    override fun findByPhoneNumber(phoneNumber: String): CreatorModel? {
        val entity = mongoCreatorRepository.findFirstByPhone(phoneNumber)
        return CreatorMapper.mapper.entityToModel(entity)
    }
}
