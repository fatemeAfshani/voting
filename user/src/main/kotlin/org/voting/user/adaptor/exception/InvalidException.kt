package org.voting.user.adaptor.exception

class InvalidException(message: String = "Resource not found") : GeneralException(message, 401)
