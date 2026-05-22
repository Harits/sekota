package com.sekota.profile

import com.sekota.features.profile.domain.model.UserProfile
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

val userProfiles = mutableMapOf<String, UserProfile>()

fun Application.configureProfileRouting() {
    routing {
        authenticate("auth-jwt") {
            route("/profile") {
                get {
                    val principal = call.principal<JWTPrincipal>()
                    val username = principal?.payload?.getClaim("username")?.asString()
                    if (username != null) {
                        val profile = userProfiles[username] ?: UserProfile(id = username, username = username)
                        call.respond(profile)
                    } else {
                        call.respond(io.ktor.http.HttpStatusCode.Unauthorized)
                    }
                }
                
                post {
                    val principal = call.principal<JWTPrincipal>()
                    val username = principal?.payload?.getClaim("username")?.asString()
                    if (username != null) {
                        val profile = call.receive<UserProfile>()
                        userProfiles[username] = profile
                        call.respond(profile)
                    } else {
                        call.respond(io.ktor.http.HttpStatusCode.Unauthorized)
                    }
                }
            }
        }
    }
}
