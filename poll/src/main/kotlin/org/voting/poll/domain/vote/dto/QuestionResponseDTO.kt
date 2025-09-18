package org.voting.poll.domain.vote.dto

import org.voting.poll.domain.poll.PollQuestion

class QuestionResponseDTO(
    var nextQuestion: PollQuestion,
    var nextQuestionAnswer: String?,
    var previousQuestion: PollQuestion,
    var previousQuestionAnswer: String?
)
