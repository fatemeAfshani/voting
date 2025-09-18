package org.voting.poll.domain.vote.dto

import org.voting.poll.domain.poll.enums.Roles

class StartVoteDTO(
    val pollId: String,
    val userId: String?,
    val role: Roles?
)
