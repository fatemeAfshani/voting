package org.voting.user.adaptor.persistance.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import org.voting.user.adaptor.persistance.entity.Voter

@Repository
interface MongoVoterRepository : MongoRepository<Voter, String> {
    fun findByTelegramId(telegramId: String): Voter?
}
