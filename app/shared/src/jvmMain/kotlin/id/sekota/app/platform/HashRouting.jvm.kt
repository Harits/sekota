package id.sekota.app.platform

actual object HashRouting {
    actual fun read(): String? = null
    actual fun write(hash: String) = Unit
    actual fun listen(onChange: (String?) -> Unit) = Unit
}
