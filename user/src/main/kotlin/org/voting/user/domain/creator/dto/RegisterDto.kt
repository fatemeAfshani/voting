package org.voting.user.domain.creator.dto

data class RegisterDto(
    var phone: String? = null,
    var password: String? = null,
    var telegramId: String? = null,
)
