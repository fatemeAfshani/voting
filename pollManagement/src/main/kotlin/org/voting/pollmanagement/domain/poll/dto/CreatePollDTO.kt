package org.voting.pollmanagement.domain.poll.dto

import org.voting.pollmanagement.domain.poll.enums.Roles

data class CreatePollDTO (
    var title: String,
    var description: String?,
    var userId: String,
    var role: Roles
)
