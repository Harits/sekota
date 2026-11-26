package id.sekota.app.platform

/**
 * Web keyboard bridge: Compose's canvas focus is not reliable across browsers,
 * so page/arrow keys are captured at the DOM level and forwarded to the active
 * list. Non-web targets are no-ops (their Compose key handler is used instead).
 */
expect object WebKeyboard {
    fun install(handler: (String) -> Unit)
}
