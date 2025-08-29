package org.voting.user.adaptor.exception

class NotFoundException(message: String = "Resource not found") : GeneralException(message, 404)
