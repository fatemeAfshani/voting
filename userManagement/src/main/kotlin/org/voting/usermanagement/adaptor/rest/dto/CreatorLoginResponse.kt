package org.voting.usermanagement.adaptor.rest.dto

data class CreatorLoginResponse(
    val phoneNumber: String? = null,
    val id: String? = null,
    val token: String? = null
)