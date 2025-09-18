package org.voting.poll.domain.vote.dto

import org.voting.poll.domain.poll.PollQuestion

class StartVoteResponse(
    var firstQuestion: PollQuestion,
    var pollId: String? = null,
    var currentAnswer: String? = null,
)
