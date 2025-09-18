package org.voting.poll.adaptor.persistance.dataMapperImp

import org.springframework.stereotype.Component
import org.voting.poll.adaptor.persistance.mapper.VoteMapper
import org.voting.poll.adaptor.persistance.repository.MongoVoteRepository
import org.voting.poll.domain.vote.VoteModel
import org.voting.poll.domain.ports.outbound.persistance.VoteRepository

@Component
class VoteRepositoryImp(
    private val mongoVoteRepository: MongoVoteRepository
) : VoteRepository {
    override fun findByPollIdAndUserId(pollId: String, userId: String): VoteModel? {
        val entity = mongoVoteRepository.findOneByPollIdAndUserId(pollId, userId)
        return entity?.let { VoteMapper.mapper.entityToModel(it) }
    }

    override fun save(vote: VoteModel): VoteModel {
        val saved = VoteMapper.mapper.modelToEntity(vote).let { mongoVoteRepository.save(it) }
        return VoteMapper.mapper.entityToModel(saved)
    }
}


