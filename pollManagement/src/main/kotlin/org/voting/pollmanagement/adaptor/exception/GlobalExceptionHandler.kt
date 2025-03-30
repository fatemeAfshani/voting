package org.voting.pollmanagement.adaptor.exception

import io.grpc.Status
import io.grpc.StatusRuntimeException
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler
import org.slf4j.Logger
import org.springframework.stereotype.Component

@Component
class GrpcGlobalExceptionHandler(
    private val logger: Logger
) {

    @GrpcExceptionHandler(GeneralException::class)
    fun handleGeneralException(ex: GeneralException): StatusRuntimeException {
        logger.error("GeneralException occurred: ${ex.message}", ex)
        val status = Status.fromCodeValue(ex.statusCode).withDescription(ex.message)
        return status.asRuntimeException()
    }

    @GrpcExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(ex: RuntimeException): StatusRuntimeException {
        logger.error("RuntimeException occurred: ${ex.message}", ex)
        return Status.INTERNAL
            .withDescription("Internal Server Error")
            .augmentDescription(ex.message)
            .asRuntimeException()
    }
}
