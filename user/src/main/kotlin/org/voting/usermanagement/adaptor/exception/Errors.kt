package org.voting.usermanagement.adaptor.exception

object Error {
    enum class ErrorCodes {
        UNAUTHORIZED,
        FORBIDDEN,
        UNKNOWN_ERROR,
        USER_NOT_FOUND,
    }


    val ErrorMessages = mapOf(
        ErrorCodes.FORBIDDEN.name to "شما دسترسی لازم را ندارید",
        ErrorCodes.UNAUTHORIZED.name to "شما دسترسی لازم را ندارید",
        ErrorCodes.UNKNOWN_ERROR.name to "خطای ناشناخته",
        ErrorCodes.USER_NOT_FOUND.name to "کاربر یافت نشد"
    )
}
