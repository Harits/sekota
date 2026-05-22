package com.sekota.plugins

import com.sekota.data.database.DatabaseFactory
import io.ktor.server.application.*

fun Application.configureDatabases() {
    try {
        DatabaseFactory.init()
    } catch (e: Exception) {
        log.error("Failed to initialize database: ${e.message}")
    }
}
