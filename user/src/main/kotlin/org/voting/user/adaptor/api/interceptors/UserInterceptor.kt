package org.voting.user.adaptor.api.interceptors

import io.grpc.Context
import io.grpc.Contexts
import io.grpc.Metadata
import io.grpc.ServerCall
import io.grpc.ServerCallHandler
import io.grpc.ServerInterceptor
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor
import org.voting.user.adaptor.api.JwtUtil
import org.voting.user.adaptor.exception.Errors
import org.voting.user.adaptor.exception.ForbiddenException

@GrpcGlobalServerInterceptor
class UserInterceptor(
    private val jwtUtil: JwtUtil
) : ServerInterceptor {
    companion object {
        val USER_ID_KEY: Context.Key<String> = Context.key("userId")
        val ROLE_KEY: Context.Key<String> = Context.key("role")
    }
    override fun <ReqT : Any?, RespT : Any?> interceptCall(
        call: ServerCall<ReqT, RespT>,
        headers: Metadata,
        next: ServerCallHandler<ReqT, RespT>
    ): ServerCall.Listener<ReqT> {
        val authHeader = headers.get(Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER))

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val token = authHeader.removePrefix("Bearer ").trim()

            try {
                val decoded = jwtUtil.validateAndDecode(token)

                val userId = decoded.subject
                val role = decoded.getClaim("role").asString()

                val ctx = Context.current()
                    .withValue(USER_ID_KEY, userId)
                    .withValue(ROLE_KEY, role)

                return Contexts.interceptCall(ctx, call, headers, next)
            } catch (e: Exception) {
                throw ForbiddenException(Errors.ErrorCodes.UNAUTHORIZED.name)
            }
        }
        val ctx = Context.current()
            .withValue(USER_ID_KEY, "")
            .withValue(ROLE_KEY, "")

        return Contexts.interceptCall(ctx, call, headers, next)
    }
}


