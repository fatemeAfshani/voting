package org.voting.poll.adaptor.persistance.dataMapperImp

import com.mongodb.DuplicateKeyException
import org.springframework.stereotype.Component
import org.voting.poll.adaptor.exception.Errors
import org.voting.poll.adaptor.exception.InvalidInputException
import org.voting.poll.adaptor.persistance.mapper.PollMapper
import org.voting.poll.adaptor.persistance.repository.MongoPollRepository
import org.voting.poll.domain.poll.PollModel
import org.voting.poll.domain.poll.enums.PollStatus
import org.voting.poll.domain.ports.outbound.persistance.PollRepository

@Component
class PollRepositoryImp(
    private val mongoPollRepository: MongoPollRepository
) : PollRepository {
    override fun insert(poll: PollModel): PollModel? {
        val entity = PollMapper.mapper.modelToEntity(poll)
        try {
            val savedEntity = entity?.let { mongoPollRepository.insert(entity) }
            return PollMapper.mapper.entityToModel(savedEntity)
        }catch (ex: DuplicateKeyException) {
            throw InvalidInputException(Errors.ErrorCodes.DUPLICATE_POLL_TITLE.name)
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
