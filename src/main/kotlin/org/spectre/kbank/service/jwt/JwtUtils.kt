package org.spectre.kbank.service.jwt

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.stereotype.Service

@Service
class JwtUtils @Autowired constructor(
    private val decoder: JwtDecoder,
) {
    fun extractUsernameFromToken(token: String): String {
        return try {
            decoder.decode(token).claims["preferred_username"]?.toString() ?: ""
        } catch (ex: Exception) {
            throw IllegalArgumentException("Invalid token", ex)
        }
    }

    fun extractClaim(token: String, claim: String): String? =
        try {
            decoder.decode(token).claims[claim]?.toString()
        } catch (ex: Exception) {
            null
        }

}