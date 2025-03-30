package org.voting.pollmanagement.adaptor.persistance.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.mapping.Document
import org.voting.pollmanagement.domain.poll.enums.PollStatus
import org.voting.pollmanagement.domain.poll.enums.QuestionType
import java.util.Date


@Document(collection = "polls")
data class Poll(
    @Id
    val id: String? = null,
    val creatorId: String,
    val title: String,
    val description: String? = null,
    val status: PollStatus = PollStatus.DRAFT,
    val price: Int = 0,
    val maxVoters: Int = 0,
    @CreatedDate
    var createDate: Date? = null,
    @LastModifiedDate
    var updatedAt: Date? = null,
    @Version
    var version: Long? = null,
    val questions: List<PollQuestion> = listOf(),
    val preferences: Map<String, String>? = null,
)

data class PollQuestion(
    val questionId: String = java.util.UUID.randomUUID().toString(),
    val questionText: String,
    val questionType: QuestionType,
    val options: List<PollOption> = listOf()
)

data class PollOption(
    val optionId: String = java.util.UUID.randomUUID().toString(),
    val optionText: String
)

