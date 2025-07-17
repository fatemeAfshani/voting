package org.voting.usermanagement.adaptor.api

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.voting.pollmanagement.adaptor.exception.Error
import org.voting.pollmanagement.adaptor.exception.ForbiddenException

@Component
class JwtUtil(
    @Value("\${jwt.secret}") private val secretKey: String,
) {

    private val algorithm: Algorithm = Algorithm.HMAC256(secretKey)
    private val verifier = JWT.require(algorithm).build()
    fun validateAndDecode(token: String): DecodedJWT {
        return try {
            verifier.verify(token)
        } catch (ex: JWTVerificationException) {
            throw ForbiddenException(Error.ErrorCodes.UNAUTHORIZED.name)
        }
    }
}
