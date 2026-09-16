package com.sekota.core.storage

import kotlinx.browser.window

actual class AdminDataStorage actual constructor() {
    actual fun saveBooksJson(json: String) {
        window.localStorage.setItem("admin_books_json", json)
    }

    actual fun getBooksJson(): String? {
        return window.localStorage.getItem("admin_books_json")
    }

    actual fun saveProductsJson(json: String) {
        window.localStorage.setItem("admin_products_json", json)
    }

    actual fun getProductsJson(): String? {
        return window.localStorage.getItem("admin_products_json")
    }

    actual fun saveMerchJson(json: String) {
        window.localStorage.setItem("admin_merch_json", json)
    }

    actual fun getMerchJson(): String? {
        return window.localStorage.getItem("admin_merch_json")
    }
}
