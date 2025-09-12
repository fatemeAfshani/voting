package org.voting.user.adaptor.api

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.voting.user.adaptor.exception.Errors
import org.voting.user.adaptor.exception.ForbiddenException
import java.util.*

@Component
class JwtUtil(
    @Value("\${jwt.secret}") private val secretKey: String,
) {
    companion object {
        const val EXPIRATION_DAYS = 30 * 24 * 60 * 60 * 1000L // 30 days
    }

    private val algorithm: Algorithm = Algorithm.HMAC256(secretKey)
    private val verifier = JWT.require(algorithm).build()

    fun generateToken(userId: String, role: String): String {
        return JWT.create()
            .withSubject(userId)
            .withClaim("role", role)
            .withIssuedAt(Date())
            .withExpiresAt(Date(System.currentTimeMillis() + EXPIRATION_DAYS))
            .sign(Algorithm.HMAC256(secretKey))
    }

    fun validateAndDecode(token: String): DecodedJWT {
        return try {
            verifier.verify(token)
        } catch (ex: JWTVerificationException) {
            throw ForbiddenException(Errors.ErrorCodes.UNAUTHORIZED.name)
        }
    }
}
