package org.voting.pollmanagement.adaptor.exception


class ForbiddenException(message: String = "Access forbidden") : GeneralException(message, 403)
