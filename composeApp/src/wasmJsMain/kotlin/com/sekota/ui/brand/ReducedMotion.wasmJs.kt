package com.sekota.ui.brand

import kotlinx.browser.window

actual fun platformPrefersReducedMotion(): Boolean =
    runCatching { window.matchMedia("(prefers-reduced-motion: reduce)").matches }.getOrDefault(false)
