package id.sekota.app.platform

import kotlinx.browser.window
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.KeyboardEvent

private var installed = false
private var keyHandler: ((String) -> Unit)? = null

actual object WebKeyboard {
    actual fun install(handler: (String) -> Unit) {
        keyHandler = handler
        if (installed) return
        installed = true
        window.addEventListener("keydown", { event ->
            val keyboard = event as? KeyboardEvent ?: return@addEventListener
            val target = keyboard.target as? HTMLElement
            val tag = target?.tagName?.lowercase()
            if (tag == "input" || tag == "textarea" || target?.isContentEditable == true) {
                return@addEventListener
            }
            val action = when (keyboard.key) {
                "PageDown", " " -> "pagedown"
                "PageUp" -> "pageup"
                "Home" -> "home"
                "End" -> "end"
                "ArrowDown" -> "down"
                "ArrowUp" -> "up"
                else -> null
            } ?: return@addEventListener
            keyboard.preventDefault()
            keyboard.stopPropagation()
            keyHandler?.invoke(action)
        }, true)
    }
}
