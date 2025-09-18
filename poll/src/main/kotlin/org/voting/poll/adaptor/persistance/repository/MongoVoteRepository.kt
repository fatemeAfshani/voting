package org.voting.poll.adaptor.persistance.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.voting.poll.adaptor.persistance.entity.Vote

interface MongoVoteRepository : MongoRepository<Vote, String> {
    fun findOneByPollIdAndUserId(pollId: String, userId: String): Vote?
}


