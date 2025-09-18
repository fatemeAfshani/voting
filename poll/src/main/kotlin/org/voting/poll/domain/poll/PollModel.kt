package org.voting.poll.domain.poll

import org.voting.poll.adaptor.exception.Errors
import org.voting.poll.adaptor.exception.InvalidInputException
import org.voting.poll.domain.poll.enums.PollStatus
import org.voting.poll.domain.poll.enums.QuestionType
import java.time.Instant
import java.util.*

data class PollModel(
    var id: String? = null,
    var creatorId: String,
    var title: String? = null,
    var description: String? = null,
    var status: PollStatus = PollStatus.ACTIVE,
    var price: Int = 0,
    var maxVoters: Int = 0,
    var version: Long = 0,
    var startTime: Instant? = null,
    var endTime: Instant? = null,
    var createDate: Date? = Date(),
    var updatedAt: Date? = Date(),
    var questions: List<PollQuestion> = mutableListOf(),
    var preferences: Map<String, String>? = null,
    var votesCount: Int = 0
) {
    fun updateWithNewData(
        title: String?,
        description: String?,
        maxVoters: Int?,
        preferences: Map<String, String>?
    ) {
        this.title = title ?: this.title
        this.description = description ?: this.description
        this.maxVoters = maxVoters ?: this.maxVoters
        this.preferences = preferences ?: this.preferences
    }

    fun findQuestionIndexById(questionId: String): Int? {
        val index = questions.indexOfFirst { it.questionId == questionId }
        return if (index >= 0) index else null
    }

    fun getQuestionById(questionId: String): PollQuestion? {
        return questions.firstOrNull { it.questionId == questionId }
    }
}

data class PollQuestion(
    var questionId: String = UUID.randomUUID().toString(),
    var questionText: String,
    var questionType: QuestionType,
    var options: List<PollOption> = listOf(),
    val shouldAnswer: Boolean = true,
)

fun PollQuestion.validateAnswerOrThrow(answer: String) {
    if (questionType == QuestionType.EXPLAIN) return
    val optionIds = options.map { it.optionId }.toSet()
    if (!optionIds.contains(answer)) {
        throw InvalidInputException(Errors.ErrorCodes.SELECTED_OPTION_NOT_EXIST.name)
    }
}

data class PollOption(
    var optionId: String = UUID.randomUUID().toString(),
    var optionText: String
)
