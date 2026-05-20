package com.sekota.auth

import com.sekota.features.auth.domain.model.AuthRequest
import com.sekota.features.auth.domain.model.AuthResponse
import com.sekota.features.auth.domain.model.User
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date

const val JWT_SECRET = "sekota-secret" // In a real app, this should be in config
const val JWT_ISSUER = "http://0.0.0.0:8080/"
const val JWT_AUDIENCE = "http://0.0.0.0:8080/auth"
const val JWT_REALM = "Access to 'sekota'"

// In-memory mock DB for users
val users = mutableMapOf<String, String>() // username -> password

fun Application.configureAuth() {
    install(Authentication) {
        jwt("auth-jwt") {
            realm = JWT_REALM
            verifier(
                JWT.require(Algorithm.HMAC256(JWT_SECRET))
                    .withAudience(JWT_AUDIENCE)
                    .withIssuer(JWT_ISSUER)
                    .build()
            )
            validate { credential ->
                if (credential.payload.getClaim("username").asString() != "") {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }
}

fun Application.configureAuthRouting() {
    routing {
        route("/auth") {
            post("/signup") {
                val request = call.receive<AuthRequest>()
                if (users.containsKey(request.username)) {
                    call.respond(HttpStatusCode.Conflict, "User already exists")
                    return@post
                }
                users[request.username] = request.password
                val token = generateJwt(request.username)
                call.respond(AuthResponse(token, User(request.username)))
            }

            post("/login") {
                val request = call.receive<AuthRequest>()
                if (users[request.username] != request.password) {
                    call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
                    return@post
                }
                val token = generateJwt(request.username)
                call.respond(AuthResponse(token, User(request.username)))
            }
        }

        authenticate("auth-jwt") {
            get("/protected") {
                val principal = call.principal<JWTPrincipal>()
                val username = principal!!.payload.getClaim("username").asString()
                val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
                call.respondText("Hello, $username! Token is expired at $expiresAt ms.")
            }
        }
    }
}

fun generateJwt(username: String): String {
    return JWT.create()
        .withAudience(JWT_AUDIENCE)
        .withIssuer(JWT_ISSUER)
        .withClaim("username", username)
        .withExpiresAt(Date(System.currentTimeMillis() + 60000 * 60)) // 1 hour
        .sign(Algorithm.HMAC256(JWT_SECRET))
}
