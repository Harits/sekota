package com.sekota.core.storage

import java.util.prefs.Preferences

actual class TokenStorage actual constructor() {
    private val prefs = Preferences.userRoot().node("com.sekota.auth")

    actual fun saveToken(token: String) {
        prefs.put("jwt_token", token)
    }

    actual fun getToken(): String? {
        return prefs.get("jwt_token", null)
    }

    actual fun clearToken() {
        prefs.remove("jwt_token")
    }
}
