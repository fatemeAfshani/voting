package org.voting.poll.adaptor.persistance.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.CompoundIndexes
import org.springframework.data.mongodb.core.mapping.Document
import java.util.Date

@Document("votes")
@CompoundIndexes(
    CompoundIndex(name = "poll_user_idx", def = "{ 'pollId': 1, 'userId': 1 }", unique = true)
)
data class Vote(
    @Id val id: String,
    val pollId: String,
    val userId: String,
    val answers: List<Answer>?,
    val createdAt: Date? = null,
    val isFinished: Boolean = false
)

data class Answer(
    val questionId: String,
    val response: String? = null
)
