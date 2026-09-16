package com.sekota.core.storage

import java.util.prefs.Preferences

actual class AdminDataStorage actual constructor() {
    private val prefs = Preferences.userRoot().node("com.sekota.admin.data")

    actual fun saveBooksJson(json: String) {
        prefs.put("books_json", json)
    }

    actual fun getBooksJson(): String? {
        return prefs.get("books_json", null)
    }

    actual fun saveProductsJson(json: String) {
        prefs.put("products_json", json)
    }

    actual fun getProductsJson(): String? {
        return prefs.get("products_json", null)
    }

    actual fun saveMerchJson(json: String) {
        prefs.put("merch_json", json)
    }

    actual fun getMerchJson(): String? {
        return prefs.get("merch_json", null)
    }

    actual fun saveMetricsJson(json: String) {
        prefs.put("metrics_json", json)
    }

    actual fun getMetricsJson(): String? {
        return prefs.get("metrics_json", null)
    }
}
