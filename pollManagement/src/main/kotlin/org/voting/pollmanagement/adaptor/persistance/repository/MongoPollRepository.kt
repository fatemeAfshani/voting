package org.voting.pollmanagement.adaptor.persistance.repository

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.voting.pollmanagement.adaptor.persistance.entity.Poll
import org.voting.pollmanagement.domain.poll.enums.PollStatus

interface MongoPollRepository : MongoRepository<Poll, ObjectId> {

    fun findOneById(id: String): Poll?

    fun findByStatus(status: PollStatus): List<Poll?>
}
