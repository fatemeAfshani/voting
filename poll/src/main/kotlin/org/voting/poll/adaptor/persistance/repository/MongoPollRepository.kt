package org.voting.poll.adaptor.persistance.repository

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.voting.poll.adaptor.persistance.entity.Poll
import org.voting.poll.domain.poll.enums.PollStatus

interface MongoPollRepository : MongoRepository<Poll, ObjectId> {

    fun findOneById(id: String): Poll?

    fun findByStatus(status: PollStatus): List<Poll?>
}
