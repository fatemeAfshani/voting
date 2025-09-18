package org.voting.user.adaptor.exception

object Errors {
    enum class ErrorCodes {
        UNAUTHORIZED,
        FORBIDDEN,
        UNKNOWN_ERROR,
        USER_NOT_FOUND,
        JOB_IS_INVALID,
        AGE_IS_INVALID,
        GENDER_IS_INVALID,
        EDUCATION_LEVEL_IS_INVALID,
        FIELD_OF_STUDY_IS_INVALID,
        MARITAL_STATUS_IS_INVALID,
        CITY_IS_INVALID,
        INVALID_ROLE,
        INTERNAL_ERROR
    }

    val ErrorMessages = mapOf(
        ErrorCodes.FORBIDDEN.name to "شما دسترسی لازم را ندارید",
        ErrorCodes.UNAUTHORIZED.name to "شما دسترسی لازم را ندارید",
        ErrorCodes.UNKNOWN_ERROR.name to "خطای ناشناخته",
        ErrorCodes.INTERNAL_ERROR.name to "خطای داخلی پیش آمده است",
        ErrorCodes.USER_NOT_FOUND.name to "کاربر یافت نشد",
        ErrorCodes.JOB_IS_INVALID.name to "شغل وارد شده معتبر نیست",
        ErrorCodes.AGE_IS_INVALID.name to "سن باید بین ۰ تا ۱۰۰ باشد",
        ErrorCodes.GENDER_IS_INVALID.name to "جنسیت باید مرد یا زن باشد",
        ErrorCodes.EDUCATION_LEVEL_IS_INVALID.name to "سطح تحصیلات معتبر نیست",
        ErrorCodes.FIELD_OF_STUDY_IS_INVALID.name to "رشته تحصیلی معتبر نیست",
        ErrorCodes.MARITAL_STATUS_IS_INVALID.name to "وضعیت تأهل معتبر نیست",
        ErrorCodes.CITY_IS_INVALID.name to "نام شهر معتبر نیست",
        ErrorCodes.INVALID_ROLE.name to "نوع کاربری شما تعریف نشد است",    )
}
