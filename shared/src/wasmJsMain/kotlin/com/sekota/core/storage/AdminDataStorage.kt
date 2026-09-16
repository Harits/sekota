package com.sekota.core.storage

actual class AdminDataStorage actual constructor() {
    actual fun saveBooksJson(json: String) {
        setStorageItem("admin_books_json", json)
    }

    actual fun getBooksJson(): String? {
        val data = getStorageItem("admin_books_json")
        return if (data == null) null else data
    }

    actual fun saveProductsJson(json: String) {
        setStorageItem("admin_products_json", json)
    }

    actual fun getProductsJson(): String? {
        val data = getStorageItem("admin_products_json")
        return if (data == null) null else data
    }

    actual fun saveMerchJson(json: String) {
        setStorageItem("admin_merch_json", json)
    }

    actual fun getMerchJson(): String? {
        val data = getStorageItem("admin_merch_json")
        return if (data == null) null else data
    }

    actual fun saveMetricsJson(json: String) {
        setStorageItem("admin_metrics_json", json)
    }

    actual fun getMetricsJson(): String? {
        val data = getStorageItem("admin_metrics_json")
        return if (data == null) null else data
    }
}
