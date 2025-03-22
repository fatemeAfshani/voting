package org.voting.usermanagement.adaptor.persistance.repository

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.voting.usermanagement.adaptor.persistance.entity.Creator


interface CreatorRepository : MongoRepository<Creator, ObjectId> {
    fun findById(id: String): List<Creator>

    fun save(creator: Creator)
}