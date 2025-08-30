package org.voting.poll.domain.poll.dto

import org.voting.poll.domain.poll.enums.Roles

data class UpdatePollDTO (
    var pollId: String,
    var maxVoters: Int?,
    var title: String?,
    var description: String?,
    var preferences: Map<String, String>?,
    var userId: String,
    var role: Roles
)
