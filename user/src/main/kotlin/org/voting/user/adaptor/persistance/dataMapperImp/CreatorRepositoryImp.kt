package org.voting.user.adaptor.persistance.dataMapperImp

import org.springframework.stereotype.Component
import org.voting.user.adaptor.persistance.mapper.CreatorMapper
import org.voting.user.adaptor.persistance.repository.MongoCreatorRepository
import org.voting.user.domain.creator.CreatorModel
import org.voting.user.domain.ports.outbound.persistance.CreatorRepositoryInterface

@Component
class CreatorRepositoryImp(
    private val mongoCreatorRepository: MongoCreatorRepository
) : CreatorRepositoryInterface {
    override fun findOneById(id: String): CreatorModel? {
        val entity = mongoCreatorRepository.findOneById(id)
        return CreatorMapper.mapper.entityToModel(entity)
    }

    override fun save(creator: CreatorModel): CreatorModel {
        val entity = CreatorMapper.mapper.modelToEntity(creator)
        val savedEntity = mongoCreatorRepository.save(entity)
        return CreatorMapper.mapper.entityToModel(savedEntity)
    }

    override fun findByPhoneNumber(phoneNumber: String): CreatorModel? {
        val entity = mongoCreatorRepository.findFirstByPhone(phoneNumber)
        return CreatorMapper.mapper.entityToModel(entity)
    }
}
