package org.voting.poll.domain.poll.dto

import org.voting.poll.domain.poll.enums.Roles

data class PollReportDTO(
    val pollId: String,
    val userId: String?,
    val role: Roles?
)
