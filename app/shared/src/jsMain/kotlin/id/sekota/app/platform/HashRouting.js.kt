package id.sekota.app.platform

import kotlinx.browser.document
import kotlinx.browser.window

actual object HashRouting {
    actual fun read(): String? = window.location.hash.takeIf { it.isNotBlank() }

    actual fun write(hash: String) {
        if (window.location.hash != hash) {
            window.location.hash = hash
        }
        document.documentElement?.setAttribute("data-route", hash)
    }

    actual fun listen(onChange: (String?) -> Unit) {
        window.addEventListener("hashchange", { onChange(read()) })
    }
}
