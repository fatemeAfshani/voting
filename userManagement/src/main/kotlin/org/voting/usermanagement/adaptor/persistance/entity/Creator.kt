package org.voting.usermanagement.adaptor.persistance.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.mapping.Document
import java.util.Date

@Document("creators")
data class Creator(
    @Id
    var id: String? = null,
    @CreatedDate
    var createDate: Date? = null,
    @LastModifiedDate
    var updatedAt: Date? = null,
    @Version
    var version: Long? = null,
    var email: String? = null,
    var phone: String? = null,
    var password: String? = null,
    var company: String? = null,
)
