package org.voting.pollmanagement.adaptor.persistance.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.voting.pollmanagement.domain.poll.enums.PollStatus
import org.voting.pollmanagement.domain.poll.enums.QuestionType
import java.time.Instant
import java.util.*


@Document(collection = "polls")
data class Poll(
    @Id
    val id: String? = null,
    @Indexed(unique = false)
    val creatorId: String,
    val title: String? = null,
    val description: String? = null,
    val status: PollStatus = PollStatus.DRAFT,
    val price: Int = 0,
    val maxVoters: Int = 0,
    var startTime: Instant? = null,
    var endTime: Instant? = null,
    @CreatedDate
    var createDate: Date? = null,
    @LastModifiedDate
    var updatedAt: Date? = null,
    @Version
    var version: Long? = null,
    val questions: List<PollQuestion> = mutableListOf(),
    val preferences: Map<String, String>? = null,
    var votesCount: Int = 0

)

data class PollQuestion(
    val questionId: String = UUID.randomUUID().toString(),
    val questionText: String,
    val questionType: QuestionType,
    val shouldAnswer : Boolean = true,
    val options: List<QuestionOption> = listOf()
)

data class QuestionOption(
    val optionId: String = UUID.randomUUID().toString(),
    val optionText: String
)

