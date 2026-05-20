package com.sekota.features.sync.data.repository

import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class SyncService {
    private val client = HttpClient {
        install(WebSockets) {
            pingIntervalMillis = 20_000
        }
    }

    private val _syncState = MutableStateFlow<String>("Disconnected")
    val syncState: StateFlow<String> = _syncState.asStateFlow()

    private var session: DefaultClientWebSocketSession? = null

    fun connect(scope: CoroutineScope) {
        scope.launch {
            try {
                _syncState.value = "Connecting..."
                client.webSocket(method = io.ktor.http.HttpMethod.Get, host = "localhost", port = 8080, path = "/ws/sync") {
                    session = this
                    _syncState.value = "Connected"
                    for (frame in incoming) {
                        if (frame is Frame.Text) {
                            val text = frame.readText()
                            _syncState.value = text
                        }
                    }
                }
            } catch (e: Exception) {
                _syncState.value = "Error: ${e.message}"
            } finally {
                _syncState.value = "Disconnected"
                session = null
            }
        }
    }

    suspend fun sendMessage(message: String) {
        session?.send(Frame.Text(message))
    }

    fun disconnect() {
        // Handled by scope cancellation typically, but can add explicit close
    }
}
