package org.voting.poll.domain.vote

import java.util.Date

data class VoteModel(
    val id: String? = null,
    val pollId: String,
    val userId: String,
    var answers: MutableList<AnswerModel> = mutableListOf(),
    val createdAt: Date? = null,
    var isFinished: Boolean = false
)

data class AnswerModel(
    var questionId: String,
    var response: String
)


fun VoteModel.findAnswer(questionId: String): AnswerModel? {
    return answers.find { it.questionId == questionId }
}

fun VoteModel.upsertAnswer(questionId: String, response: String) {
    val existing = findAnswer(questionId)
    if (existing != null) {
        existing.response = response
    } else {
        answers.add(AnswerModel(questionId = questionId, response = response))
    }
}
