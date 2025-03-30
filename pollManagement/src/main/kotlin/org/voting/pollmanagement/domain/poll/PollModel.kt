package org.voting.pollmanagement.domain.poll

import org.voting.pollmanagement.domain.poll.enums.PollStatus
import org.voting.pollmanagement.domain.poll.enums.QuestionType
import java.util.Date

data class PollModel(
    val id: String? = null,
    val creatorId: String,
    val title: String,
    val description: String? = null,
    val status: PollStatus = PollStatus.DRAFT,
    val price: Int = 0,
    val maxVoters: Int = 0,
    val createDate: Date = Date(),
    val updatedAt: Date = Date(),
    val questions: List<PollQuestion> = listOf(),
    val preferences: PollPreferences? = null
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

data class PollPreferences(
    val ageRange: List<String> = listOf(),
    val gender: List<String> = listOf(),
    val educationLevel: List<String> = listOf()
)
