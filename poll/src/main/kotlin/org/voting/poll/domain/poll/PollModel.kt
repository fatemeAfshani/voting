package org.voting.poll.domain.poll

import org.voting.poll.domain.poll.enums.PollStatus
import org.voting.poll.domain.poll.enums.QuestionType
import java.time.Instant
import java.util.*

data class PollModel(
    var id: String? = null,
    var creatorId: String,
    var title: String? = null,
    var description: String? = null,
    var status: PollStatus = PollStatus.DRAFT,
    var price: Int = 0,
    var maxVoters: Int = 0,
    var version: Long = 0,
    var startTime: Instant? = null,
    var endTime: Instant? = null,
    var createDate: Date = Date(),
    var updatedAt: Date = Date(),
    var questions: List<PollQuestion> = mutableListOf(),
    var preferences: Map<String, String>? = null,
    var votesCount: Int = 0

)

data class PollQuestion(
    var questionId: String = UUID.randomUUID().toString(),
    var questionText: String,
    var questionType: QuestionType,
    var options: List<PollOption> = listOf(),
    val shouldAnswer : Boolean = true,

    )

data class PollOption(
    var optionId: String = UUID.randomUUID().toString(),
    var optionText: String
)

