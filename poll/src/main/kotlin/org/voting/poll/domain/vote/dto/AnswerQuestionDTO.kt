package org.voting.poll.domain.vote.dto

import org.voting.poll.domain.poll.enums.Roles

class AnswerQuestionDTO(
    val pollId: String,
    val questionId: String,
    val answer: String,
    val userId: String?,
    val role: Roles?
)


