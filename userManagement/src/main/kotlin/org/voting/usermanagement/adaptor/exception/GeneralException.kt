package org.voting.usermanagement.adaptor.exception


abstract class GeneralException(
    message: String,
    val statusCode: Int
) : RuntimeException(message)
