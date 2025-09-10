package org.voting.user.domain.creator

import java.util.Date

data class CreatorModel(
    var id: String? = null,
    var createDate: Date? = null,
    var updatedAt: Date? = null,
    var version: Long? = null,
    var email: String? = null,
    var phone: String? = null,
    val userName: String? = null,
    var password: String? = null,
    var company: String? = null,
    var telegramId: String? = null
)
