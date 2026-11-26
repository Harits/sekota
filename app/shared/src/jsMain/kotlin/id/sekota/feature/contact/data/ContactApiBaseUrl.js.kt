package id.sekota.feature.contact.data

import kotlinx.browser.window

/**
 * The static web build and the API can be served from different ports during
 * development. On localhost we point at the Ktor server (`:8080`); when the
 * site is served by the Ktor server itself the API is same-origin.
 */
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
