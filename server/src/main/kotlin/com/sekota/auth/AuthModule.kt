package com.sekota.auth

import com.sekota.features.auth.domain.model.AuthRequest
import com.sekota.features.auth.domain.model.AuthResponse
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
import java.util.UUID

const val JWT_SECRET = "sekota-secret" // In a real app, this should be in config
const val JWT_ISSUER = "http://0.0.0.0:8080/"
const val JWT_AUDIENCE = "http://0.0.0.0:8080/auth"
const val JWT_REALM = "Access to 'sekota'"

// In-memory mock DB for users: email -> Triple(password, userId, role)
val users = mutableMapOf<String, Triple<String, String, String>>()

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
                val email = credential.payload.getClaim("email").asString()
                val username = credential.payload.getClaim("username").asString()
                if (!email.isNullOrEmpty() || !username.isNullOrEmpty()) {
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
            suspend fun handleRegister(call: ApplicationCall) {
                val request = call.receive<AuthRequest>()
                if (users.containsKey(request.email)) {
                    call.respond(HttpStatusCode.Conflict, "User already exists")
                    return
                }
                val userId = UUID.randomUUID().toString()
                val role = request.role ?: "READER"
                users[request.email] = Triple(request.password, userId, role)
                val token = generateJwt(request.email, userId, role)
                call.respond(AuthResponse(token = token, userId = userId, role = role))
            }

            post("/register") {
                handleRegister(call)
            }

            post("/signup") {
                handleRegister(call)
            }

            post("/login") {
                val request = call.receive<AuthRequest>()
                val stored = users[request.email]
                if (stored == null || stored.first != request.password) {
                    call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
                    return@post
                }
                val (_, userId, role) = stored
                val token = generateJwt(request.email, userId, role)
                call.respond(AuthResponse(token = token, userId = userId, role = role))
            }
        }

        authenticate("auth-jwt") {
            get("/protected") {
                val principal = call.principal<JWTPrincipal>()
                val email = principal?.payload?.getClaim("email")?.asString()
                    ?: principal?.payload?.getClaim("username")?.asString()
                val userId = principal?.payload?.getClaim("sub")?.asString()
                val role = principal?.payload?.getClaim("role")?.asString()
                val expiresAt = principal?.expiresAt?.time?.minus(System.currentTimeMillis())
                call.respondText("Hello, $email ($userId, role: $role)! Token is expired at $expiresAt ms.")
            }
        }
    }
}

fun generateJwt(email: String, userId: String, role: String): String {
    return JWT.create()
        .withAudience(JWT_AUDIENCE)
        .withIssuer(JWT_ISSUER)
        .withClaim("email", email)
        .withClaim("username", email)
        .withClaim("sub", userId)
        .withClaim("role", role)
        .withExpiresAt(Date(System.currentTimeMillis() + 60000 * 60)) // 1 hour
        .sign(Algorithm.HMAC256(JWT_SECRET))
}
