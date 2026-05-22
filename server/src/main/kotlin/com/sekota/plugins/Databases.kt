package com.sekota.plugins

import com.sekota.data.database.DatabaseFactory
import io.ktor.server.application.*

fun Application.configureDatabases() {
    DatabaseFactory.init()
}
