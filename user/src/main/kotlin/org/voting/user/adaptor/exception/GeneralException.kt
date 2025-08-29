package org.voting.user.adaptor.exception


abstract class GeneralException(
    message: String,
    val statusCode: Int
) : RuntimeException(message)
