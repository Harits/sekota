package id.sekota.app.platform

/**
 * Browser hash bridge. Non-web targets are no-ops, so the same App() works
 * everywhere. On web the current route is reflected in `window.location.hash`
 * and `data-route` (a small hook used by the visual verification scripts).
 */
expect object HashRouting {
    fun read(): String?
    fun write(hash: String)
    fun listen(onChange: (String?) -> Unit)
}
