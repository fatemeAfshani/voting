package org.voting.pollmanagement.adaptor.persistance.dataMapperImp

import com.mongodb.DuplicateKeyException
import org.springframework.stereotype.Component
import org.voting.pollmanagement.adaptor.exception.Error
import org.voting.pollmanagement.adaptor.exception.InvalidInputException
import org.voting.pollmanagement.adaptor.persistance.mapper.PollMapper
import org.voting.pollmanagement.adaptor.persistance.repository.MongoPollRepository
import org.voting.pollmanagement.domain.poll.PollModel
import org.voting.pollmanagement.domain.poll.enums.PollStatus
import org.voting.pollmanagement.domain.ports.outbound.persistance.PollRepository

@Component
class PollRepositoryImp(
    private val mongoPollRepository: MongoPollRepository
) : PollRepository {
    override fun save(poll: PollModel): PollModel? {
        val entity = PollMapper.mapper.modelToEntity(poll)
        try {
            val savedEntity = entity?.let { mongoPollRepository.save(entity) }
            return PollMapper.mapper.entityToModel(savedEntity)
        }catch (ex: DuplicateKeyException) {
            throw InvalidInputException(Error.ErrorCodes.DUPLICATE_POLL_TITLE.name)
        }
    }

    override fun findById(id: String): PollModel? {
        val entity = mongoPollRepository.findOneById(id)
        return PollMapper.mapper.entityToModel(entity)
    }

    override fun findByStatus(status: PollStatus): List<PollModel?> {
        val entities = mongoPollRepository.findByStatus(status)
        return entities.map { PollMapper.mapper.entityToModel(it) }
    }
}
