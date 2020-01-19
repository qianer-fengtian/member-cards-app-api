package jp.co.anyplus.anyplab.webapp.membercards

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm

class SimpleJWT(val secret: String) {
    private val algorithm = Algorithm.HMAC256(secret)
    val verifier = JWT.require(algorithm).build()
    fun sign(code: String): String = JWT.create().withClaim("code", code).sign(algorithm)
}