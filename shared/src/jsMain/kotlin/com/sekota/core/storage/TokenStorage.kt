package com.sekota.core.storage

import kotlinx.browser.window

actual class TokenStorage actual constructor() {
    actual fun saveToken(token: String) {
        window.localStorage.setItem("jwt_token", token)
    }

    actual fun getToken(): String? {
        return window.localStorage.getItem("jwt_token")
    }

    actual fun clearToken() {
        window.localStorage.removeItem("jwt_token")
    }
}
