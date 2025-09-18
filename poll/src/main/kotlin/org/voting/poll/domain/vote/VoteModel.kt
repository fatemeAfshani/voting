package org.voting.poll.domain.vote

import java.util.Date

data class VoteModel(
    val id: String? = null,
    val pollId: String,
    val userId: String,
    val answers: List<AnswerModel>?,
    val createdAt: Date? = null,
    val isFinished: Boolean = false
)

data class AnswerModel(
    val questionId: String,
    val response: String
)


