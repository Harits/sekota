package com.sekota.core.storage

expect class AdminDataStorage() {
    fun saveBooksJson(json: String)
    fun getBooksJson(): String?

    fun saveProductsJson(json: String)
    fun getProductsJson(): String?

    fun saveMerchJson(json: String)
    fun getMerchJson(): String?

    fun saveMetricsJson(json: String)
    fun getMetricsJson(): String?
}
