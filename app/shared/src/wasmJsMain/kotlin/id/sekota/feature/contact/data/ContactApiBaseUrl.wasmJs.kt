package id.sekota.feature.contact.data

import kotlinx.browser.window

internal actual fun contactApiBaseUrl(): String {
    val location = window.location
    val host = location.hostname
    val local = host == "localhost" || host == "127.0.0.1" || host == "::1"
    if (!local) return ""
    return if (location.port == "8080" || location.port == "8090") {
        ""
    } else {
        "http://127.0.0.1:8090"
    }
}
