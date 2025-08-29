package org.voting.user.adaptor.api

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtUtil(
    @Value("\${jwt.secret}") private val secretKey: String,
) {
    companion object {
        const val EXPIRATION_DAYS = 30 * 24 * 60 * 60 * 1000L // 30 days
    }
    fun generateToken(userId: String, role: String): String {
        return JWT.create()
            .withSubject(userId)
            .withClaim("role", role)
            .withIssuedAt(Date())
            .withExpiresAt(Date(System.currentTimeMillis() + EXPIRATION_DAYS))
            .sign(Algorithm.HMAC256(secretKey))
    }
}
