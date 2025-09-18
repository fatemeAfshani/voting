package org.voting.poll.domain.ports.outbound.persistance

import org.voting.poll.domain.vote.VoteModel

interface VoteRepository {
    fun findByPollIdAndUserId(pollId: String, userId: String): VoteModel?
    fun save(vote: VoteModel): VoteModel
    fun findByPollId(pollId: String): List<VoteModel>
}


