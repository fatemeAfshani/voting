package org.voting.pollmanagement.adaptor.exception

object Errors {
    enum class ErrorCodes {
        UNAUTHORIZED,
        FORBIDDEN,
        UNKNOWN_ERROR,
        USER_NOT_FOUND,
        INVALID_ROLE,
        DUPLICATE_POLL_TITLE
    }


    val ErrorMessages = mapOf(
        ErrorCodes.FORBIDDEN.name to "شما دسترسی لازم را ندارید",
        ErrorCodes.UNAUTHORIZED.name to "شما دسترسی لازم را ندارید",
        ErrorCodes.UNKNOWN_ERROR.name to "خطای ناشناخته",
        ErrorCodes.USER_NOT_FOUND.name to "کاربر یافت نشد",
        ErrorCodes.INVALID_ROLE.name to "نوع کاربری شما تعریف نشد است",
        ErrorCodes.DUPLICATE_POLL_TITLE.name to "نظرسنجی با این نام وجود دارد، نام دیگری انتخاب کنید"
    )
}
