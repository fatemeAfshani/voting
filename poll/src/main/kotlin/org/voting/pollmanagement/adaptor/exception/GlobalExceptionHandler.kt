package org.voting.pollmanagement.adaptor.exception

import io.grpc.ForwardingServerCallListener
import io.grpc.ServerCall
import io.grpc.Metadata
import io.grpc.ServerCallHandler
import io.grpc.ServerInterceptor
import io.grpc.Status
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor


@GrpcGlobalServerInterceptor
class GlobalExceptionHandler : ServerInterceptor {

    private fun mapExceptionToStatus(ex: Exception): Status {
        return when (ex) {
            is NotFoundException -> Status.NOT_FOUND
            is InvalidInputException -> Status.INVALID_ARGUMENT
            is ForbiddenException -> Status.PERMISSION_DENIED
            else -> Status.INTERNAL
        }.withDescription(ex.message)
    }

    override fun <ReqT : Any?, RespT : Any?> interceptCall(
        call: ServerCall<ReqT, RespT>,
        headers: Metadata,
        next: ServerCallHandler<ReqT, RespT>
    ): ServerCall.Listener<ReqT> {
        val listener = next.startCall(call, headers)

        return object : ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT>(listener) {
            override fun onHalfClose() {
                try {
                    super.onHalfClose()
                } catch (ex: GeneralException) {
                    val metadata = Metadata()
                    metadata.put(
                        Metadata.Key.of("faMessage", Metadata.ASCII_STRING_MARSHALLER),
                        Errors.ErrorMessages[ex.message] ?: "خطای ناشناخته"
                    )
                    val status = mapExceptionToStatus(ex)
                    call.close(status, metadata)
                } catch (ex: Throwable) {
                    val metadata = Metadata()
                    metadata.put(
                        Metadata.Key.of("faMessage", Metadata.ASCII_STRING_MARSHALLER),
                        Errors.ErrorMessages[Errors.ErrorCodes.UNKNOWN_ERROR.name] ?: "خطای ناشناخته"
                    )
                    call.close(
                        Status.INTERNAL.withDescription("Unexpected error"), metadata)
                }
            }
        }
    }
}
