package id.sekota.app.platform

import kotlinx.browser.document

actual fun setDocumentLanguage(code: String) {
    document.documentElement?.setAttribute("lang", code)
}
