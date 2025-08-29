package org.voting.user.adaptor.exception

import org.slf4j.Logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler(
    private val logger: Logger,
) {
    @ExceptionHandler(GeneralException::class)
    fun handleGeneralException(ex: GeneralException): ResponseEntity<Map<String, Any>> {
        logger.error("ApiException occurred: ${ex.message}", ex)
        return ResponseEntity(
            mapOf(
                "error" to (ex.message ?: "An unexpected error occurred"),
                "faMessage" to (Error.ErrorMessages[ex.message] ?: "An unexpected error occurred"),
                "status" to ex.statusCode
            ),
            HttpStatus.valueOf(ex.statusCode)
        )
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(ex: RuntimeException): ResponseEntity<Map<String, Any>> {
        logger.error("RuntimeException occurred: ${ex.message}", ex)
        return ResponseEntity(
            mapOf(
                "error" to "Internal Server Error",
                "status" to 500,
                "faMessage" to (Error.ErrorMessages[Error.ErrorCodes.UNKNOWN_ERROR.name] ?: "خطای ناشناخته"),
            ),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }
}
