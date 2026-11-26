package id.sekota.app.platform

actual object WebKeyboard {
    actual fun install(handler: (String) -> Unit) = Unit
}
