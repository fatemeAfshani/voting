package org.voting.pollmanagement.adaptor.exception


abstract class GeneralException(
    message: String,
    val statusCode: Int
) : RuntimeException(message)
