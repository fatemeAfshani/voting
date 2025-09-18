package org.voting.user.adaptor.persistance.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.voting.user.domain.user.EducationLevels
import org.voting.user.domain.user.Genders
import org.voting.user.domain.user.MaritalStatuses
import java.util.*

@Document(collection = "voters")
data class Voter(
    @Id
    val id: String? = null,
    @Indexed
    val telegramId: String,
    @Indexed
    val userId: String? = null,
    val city: String? = null,
    val gender: Genders? = null,
    val age: Int? = null,
    var job: String? = null,
    var educationLevel: EducationLevels? = null,
    var fieldOfStudy: String? = null,
    val maritalStatus: MaritalStatuses? = null,
    @CreatedDate
    var createDate: Date? = null,
    @LastModifiedDate
    var updatedAt: Date? = null,
    @Version
    var version: Long? = null,
)
