package org.voting.user.adaptor.exception

import com.google.protobuf.Any
import com.google.rpc.Code
import com.google.rpc.LocalizedMessage
import io.grpc.Metadata
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.grpc.protobuf.StatusProto
import net.devh.boot.grpc.server.advice.GrpcAdvice
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler
import com.google.rpc.Status as RpcStatus
@GrpcAdvice
class GlobalGRPCExceptionHandler {

    private val faMessageKey: Metadata.Key<String> =
        Metadata.Key.of("fa-message", Metadata.ASCII_STRING_MARSHALLER)

    private fun statusWithFaDetails(code: Code, desc: String?, fa: String): StatusRuntimeException {
        val details = LocalizedMessage.newBuilder().setLocale("fa-IR").setMessage(fa).build()

        val rpcStatus = RpcStatus.newBuilder()
            .setCode(code.number)
            .setMessage(desc ?: "")
            .addDetails(Any.pack(details))
            .build()

        val trailers = Metadata().apply { put(faMessageKey, fa) }
        return StatusProto.toStatusRuntimeException(rpcStatus, trailers)
    }

    private fun mapHttpLikeToGrpc(code: Int): Status = when (code) {
        400 -> Status.INVALID_ARGUMENT
        401 -> Status.UNAUTHENTICATED
        403 -> Status.PERMISSION_DENIED
        404 -> Status.NOT_FOUND
        409 -> Status.ALREADY_EXISTS
        429 -> Status.RESOURCE_EXHAUSTED
        499 -> Status.CANCELLED
        500 -> Status.INTERNAL
        501 -> Status.UNIMPLEMENTED
        503 -> Status.UNAVAILABLE
        504 -> Status.DEADLINE_EXCEEDED
        else -> Status.UNKNOWN
    }

    @GrpcExceptionHandler(GeneralException::class)
    fun handleGeneral(ex: GeneralException): StatusRuntimeException {
        val grpcStatus = mapHttpLikeToGrpc(ex.statusCode)
        val code = Code.forNumber(grpcStatus.code.value()) ?: Code.UNKNOWN
        val faText = Errors.ErrorMessages[ex.message] ?: (ex.message ?: "خطای ناشناخته")
        return statusWithFaDetails(code, ex.message, faText)
    }

    @GrpcExceptionHandler
    fun handleUnknown(ex: Throwable): StatusRuntimeException {
        val faText = Errors.ErrorMessages[Errors.ErrorCodes.UNKNOWN_ERROR.name] ?: "خطای ناشناخته"
        return statusWithFaDetails(Code.INTERNAL, "Unexpected error", faText)
    }
}
