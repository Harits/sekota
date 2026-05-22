package com.sekota

import com.sekota.auth.configureAuth
import com.sekota.auth.configureAuthRouting
import com.sekota.profile.configureProfileRouting
import com.sekota.plugins.configureDatabases
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlin.time.Duration.Companion.seconds
import java.util.Collections
import kotlinx.serialization.json.Json

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }
    
    install(CORS) {
        anyHost()
        allowHeader(io.ktor.http.HttpHeaders.ContentType)
        allowHeader(io.ktor.http.HttpHeaders.Authorization)
    }
    
    install(WebSockets) {
        pingPeriod = 15.seconds
        timeout = 15.seconds
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
    
    configureAuth()
    configureAuthRouting()
    configureProfileRouting()
    configureDatabases()
    
    routing {
        swaggerUI(path = "swagger", swaggerFile = "openapi/documentation.yaml") {
            version = "4.15.5"
        }
        openAPI(path = "openapi", swaggerFile = "openapi/documentation.yaml")
        
        get("/") {
            call.respondText("Ktor: ${Greeting().greet()}")
        }
        
        val connections = Collections.synchronizedSet<WebSocketSession>(LinkedHashSet())
        
        webSocket("/ws/sync") {
            connections += this
            try {
                send("Connected to Sekota Sync Server")
                for (frame in incoming) {
                    if (frame is Frame.Text) {
                        val text = frame.readText()
                        // Broadcast received sync message to all other connected clients
                        connections.forEach {
                            if (it != this) {
                                it.send(Frame.Text("Sync update: $text"))
                            }
                        }
                    }
                }
            } finally {
                connections -= this
            }
        }
    }
}
