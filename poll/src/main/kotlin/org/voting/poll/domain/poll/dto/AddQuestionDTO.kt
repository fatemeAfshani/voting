package org.voting.poll.domain.poll.dto

import org.voting.poll.domain.poll.enums.QuestionType
import org.voting.poll.domain.poll.enums.Roles

data class AddQuestionDTO(
    val pollId: String,
    val questionText: String,
    val questionType: QuestionType,
    val options: List<String> = listOf(),
    val shouldAnswer: Boolean = true,
    val userId: String,
    val role: Roles
)



