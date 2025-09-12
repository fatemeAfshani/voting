package org.voting.user.adaptor.persistance.repository

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.voting.user.adaptor.persistance.entity.Creator

interface MongoCreatorRepository : MongoRepository<Creator, ObjectId> {
    fun findOneById(id: String): Creator

    fun save(creator: Creator): Creator

    fun findFirstByPhone(phoneNumber: String): Creator?
    fun findFirstByTelegramId(telegramId: String): Creator?
}
