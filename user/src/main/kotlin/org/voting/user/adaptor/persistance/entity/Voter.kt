package org.voting.user.adaptor.persistance.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "voters")
data class Voter(
    @Id
    val id: String? = null,
    @Indexed
    val telegramId: String,
    @CreatedDate
    var createDate: Date? = null,
    @LastModifiedDate
    var updatedAt: Date? = null,
    @Version
    var version: Long? = null,
)
