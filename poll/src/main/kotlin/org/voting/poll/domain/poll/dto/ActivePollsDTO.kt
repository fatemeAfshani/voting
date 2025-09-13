package org.voting.poll.domain.poll.dto

data class ActivePollsDTO(
    var title: String?,
    var description: String?,
    var preferences: Map<String, String>?,

)
