package org.voting.user.adaptor.api.mapper

import org.voting.user.adaptor.exception.Errors
import org.voting.user.adaptor.exception.ForbiddenException
import org.voting.user.adaptor.exception.InvalidException
import org.voting.user.domain.user.Roles

interface BaseMapper {
    fun mapRole(role: String): Roles {
        try {
            val convertedRole = Roles.valueOf(role.uppercase())
            if (convertedRole !== Roles.CREATOR && convertedRole !== Roles.VOTER) {
                throw ForbiddenException(Errors.ErrorCodes.FORBIDDEN.name)
            }
            return convertedRole
        } catch (e: IllegalArgumentException) {
            throw InvalidException(Errors.ErrorCodes.INVALID_ROLE.name)
        }
    }
}
