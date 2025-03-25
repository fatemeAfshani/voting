package org.voting.usermanagement.domain.creator.dto

data class RegisterDto(
    var phone: String? = null,
    var password: String? = null,
    var userName: String? = null,
)
