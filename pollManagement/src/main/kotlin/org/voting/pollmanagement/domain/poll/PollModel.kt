package org.voting.pollmanagement.domain.poll

import org.voting.pollmanagement.domain.poll.enums.PollStatus
import org.voting.pollmanagement.domain.poll.enums.QuestionType
import java.util.Date

data class PollModel(
    var id: String? = null,
    var creatorId: String,
    var title: String,
    var description: String? = null,
    var status: PollStatus = PollStatus.DRAFT,
    var price: Int = 0,
    var maxVoters: Int = 0,
    var version: Long = 0,
    var createDate: Date = Date(),
    var updatedAt: Date = Date(),
    var questions: List<PollQuestion> = listOf(),
    var preferences: Map<String, String>? = null
)

data class PollQuestion(
    var questionId: String = java.util.UUID.randomUUID().toString(),
    var questionText: String,
    var questionType: QuestionType,
    var options: List<PollOption> = listOf()
)

data class PollOption(
    var optionId: String = java.util.UUID.randomUUID().toString(),
    var optionText: String
)

