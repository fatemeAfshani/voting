package org.voting.pollmanagement.adaptor.api.interceptors

import io.grpc.ServerCall
import io.grpc.ServerCallHandler
import io.grpc.ServerInterceptor
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor

@GrpcGlobalServerInterceptor
class LoggingInterceptor : ServerInterceptor {

    override fun <ReqT : Any?, RespT : Any?> interceptCall(
        call: ServerCall<ReqT, RespT>,
        headers: io.grpc.Metadata,
        next: ServerCallHandler<ReqT, RespT>
    ): ServerCall.Listener<ReqT> {
            println("Received call to ${call.methodDescriptor.fullMethodName}")
            return next.startCall(call, headers)
    }
}