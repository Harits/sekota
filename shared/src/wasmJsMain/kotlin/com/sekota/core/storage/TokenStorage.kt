package com.sekota.core.storage

actual class TokenStorage actual constructor() {
    actual fun saveToken(token: String) {
        setStorageItem("jwt_token", token)
    }

    actual fun getToken(): String? {
        val t = getStorageItem("jwt_token")
        return if (t == null) null else t
    }

    actual fun clearToken() {
        removeStorageItem("jwt_token")
    }
}

fun setStorageItem(key: String, value: String): Unit =
    js("window.localStorage.setItem(key, value)")

fun getStorageItem(key: String): String? =
    js("window.localStorage.getItem(key)")

fun removeStorageItem(key: String): Unit =
    js("window.localStorage.removeItem(key)")
