package org.voting.user.adaptor.exception

class InternalException(message: String = "internal error") : GeneralException(message, 500)
