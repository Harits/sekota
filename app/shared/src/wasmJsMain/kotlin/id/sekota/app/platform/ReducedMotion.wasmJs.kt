package id.sekota.app.platform

import kotlinx.browser.window

actual fun prefersReducedMotion(): Boolean =
    runCatching {
        window.matchMedia("(prefers-reduced-motion: reduce)").matches
    }.getOrDefault(false)
