package com.sekota

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object NetworkClient {
    private val jsonConfig = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
    }

    // Client for https://sekota.id/api/v1 (Auth, Profile, Master Specs)
    val authClient = HttpClient {
        install(ContentNegotiation) {
            json(jsonConfig)
        }
        defaultRequest {
            url(AUTH_BASE_URL)
        }
    }

    // Client for Local Sekota Server (WebSockets, Internal APIs, Local Services)
    val localClient = HttpClient {
        install(ContentNegotiation) {
            json(jsonConfig)
        }
        defaultRequest {
            url(LOCAL_API_BASE_URL)
        }
    }

    // Default client pointing to primary gateway
    val client = authClient
}
