package org.voting.poll.adaptor.api.mapper

import org.voting.poll.adaptor.exception.Errors
import org.voting.poll.adaptor.exception.ForbiddenException
import org.voting.poll.adaptor.exception.InvalidInputException
import org.voting.poll.domain.poll.enums.Roles

interface BaseMapper {
    fun mapRole(role: String): Roles {
        try {
            val convertedRole =  Roles.valueOf(role.uppercase())
            if(convertedRole !== Roles.CREATOR){
                throw ForbiddenException(Errors.ErrorCodes.FORBIDDEN.name)
            }
            return convertedRole
        } catch (e: IllegalArgumentException) {
            throw InvalidInputException(Errors.ErrorCodes.INVALID_ROLE.name)
        }
    }
}
