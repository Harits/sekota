package id.sekota.feature.contact.data

internal actual fun contactApiBaseUrl(): String =
    System.getenv("SEKOTA_API_BASE")?.trim()?.trimEnd('/')?.takeIf { it.isNotEmpty() }
        ?: "http://localhost:8080"
