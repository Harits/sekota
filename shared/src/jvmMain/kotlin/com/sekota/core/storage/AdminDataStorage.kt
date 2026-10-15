package com.sekota.core.storage

import java.io.File

actual class AdminDataStorage actual constructor() {
    private val storageDir = File(System.getProperty("user.home"), ".sekota").apply {
        if (!exists()) mkdirs()
    }

    private fun getFile(key: String): File = File(storageDir, "$key.json")

    private fun write(key: String, content: String) {
        try {
            val file = getFile(key)
            val tempFile = File(storageDir, "$key.json.tmp")
            tempFile.writeText(content, Charsets.UTF_8)
            if (!tempFile.renameTo(file)) {
                tempFile.copyTo(file, overwrite = true)
                tempFile.delete()
            }
        } catch (e: Exception) {
            System.err.println("AdminDataStorage write error: ${e.message}")
        }
    }

    private fun read(key: String): String? {
        val file = getFile(key)
        return if (file.exists() && file.isFile) {
            try {
                file.readText(Charsets.UTF_8)
            } catch (e: Exception) {
                null
            }
        } else null
    }

    actual fun saveBooksJson(json: String) {
        write("books_json", json)
    }

    actual fun getBooksJson(): String? {
        return read("books_json")
    }

    actual fun saveProductsJson(json: String) {
        write("products_json", json)
    }

    actual fun getProductsJson(): String? {
        return read("products_json")
    }

    actual fun saveMerchJson(json: String) {
        write("merch_json", json)
    }

    actual fun getMerchJson(): String? {
        return read("merch_json")
    }

    actual fun saveMetricsJson(json: String) {
        write("metrics_json", json)
    }

    actual fun getMetricsJson(): String? {
        return read("metrics_json")
    }

    actual fun saveInquiriesJson(json: String) {
        write("inquiries_json", json)
    }

    actual fun getInquiriesJson(): String? {
        return read("inquiries_json")
    }

    actual fun saveValueLoopJson(json: String) {
        write("valueloop_json", json)
    }

    actual fun getValueLoopJson(): String? {
        return read("valueloop_json")
    }

    actual fun saveCategoriesJson(json: String) {
        write("categories_json", json)
    }

    actual fun getCategoriesJson(): String? {
        return read("categories_json")
    }

    actual fun saveWhySekotaJson(json: String) {
        write("whysekota_json", json)
    }

    actual fun getWhySekotaJson(): String? {
        return read("whysekota_json")
    }

    actual fun saveContactConfigJson(json: String) {
        write("contact_config_json", json)
    }

    actual fun getContactConfigJson(): String? {
        return read("contact_config_json")
    }

    actual fun saveFooterConfigJson(json: String) {
        write("footer_config_json", json)
    }

    actual fun getFooterConfigJson(): String? {
        return read("footer_config_json")
    }

    actual fun saveNavigationItemsJson(json: String) {
        write("navigation_items_json", json)
    }

    actual fun getNavigationItemsJson(): String? {
        return read("navigation_items_json")
    }

    actual fun saveReadingProgressJson(bookId: String, json: String) {
        write("reading_progress_$bookId", json)
    }

    actual fun getReadingProgressJson(bookId: String): String? {
        return read("reading_progress_$bookId")
    }
}
