package org.voting.user.adaptor.persistance.dataMapperImp

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.voting.user.adaptor.persistance.mapper.VoterMapper
import org.voting.user.adaptor.persistance.repository.MongoVoterRepository
import org.voting.user.domain.ports.outbound.persistance.VoterRepositoryInterface
import org.voting.user.domain.voter.VoterModel

@Component
class VoterRepositoryImp(
    private val mongoVoterRepository: MongoVoterRepository,
    private val voterMapper: VoterMapper
) : VoterRepositoryInterface {

    override fun findByTelegramId(telegramId: String): VoterModel? {
        val voter = mongoVoterRepository.findByTelegramId(telegramId)
        return voter?.let { voterMapper.entityToModel(it) }
    }

    override fun findByUserId(userId: String): VoterModel? {
        val voter = mongoVoterRepository.findByIdOrNull(userId)
        return voter?.let { voterMapper.entityToModel(it) }
    }

    override fun save(voter: VoterModel): VoterModel {
        val entity = voterMapper.modelToEntity(voter)
        val savedEntity = mongoVoterRepository.save(entity)
        return voterMapper.entityToModel(savedEntity)
    }
}
