package org.voting.usermanagement.adaptor.exception

class ForbiddenException(message: String = "Access forbidden") : GeneralException(message, 403)
