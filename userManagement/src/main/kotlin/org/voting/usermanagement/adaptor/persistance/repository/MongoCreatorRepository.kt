package org.voting.usermanagement.adaptor.persistance.repository

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.voting.usermanagement.adaptor.persistance.entity.Creator

interface MongoCreatorRepository : MongoRepository<Creator, ObjectId> {
    fun findOneById(id: String): Creator

    fun save(creator: Creator)

    fun findFirstByPhone(phoneNumber: String): Creator
}
