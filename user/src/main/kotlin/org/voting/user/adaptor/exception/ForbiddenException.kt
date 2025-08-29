package org.voting.user.adaptor.exception

class ForbiddenException(message: String = "Access forbidden") : GeneralException(message, 403)
