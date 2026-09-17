package com.sekota.features.admin.domain.repository

import com.sekota.features.admin.domain.model.AdminBook
import com.sekota.features.admin.domain.model.AdminLiveMetrics
import com.sekota.features.admin.domain.model.AdminMerch
import com.sekota.features.admin.domain.model.AdminProduct

import com.sekota.features.admin.domain.model.ClientInquiry

interface AdminRepository {
    suspend fun getBooks(): List<AdminBook>
    suspend fun getBookById(id: String): AdminBook?
    suspend fun saveBook(book: AdminBook): Result<AdminBook>
    suspend fun deleteBook(id: String): Result<Boolean>

    suspend fun getProducts(): List<AdminProduct>
    suspend fun saveProduct(product: AdminProduct): Result<AdminProduct>
    suspend fun deleteProduct(id: String): Result<Boolean>

    suspend fun getMerchandise(): List<AdminMerch>
    suspend fun saveMerchandise(merch: AdminMerch): Result<AdminMerch>
    suspend fun deleteMerchandise(id: String): Result<Boolean>

    suspend fun getLiveMetrics(): AdminLiveMetrics
    suspend fun saveLiveMetrics(metrics: AdminLiveMetrics): Result<AdminLiveMetrics>

    suspend fun getInquiries(): List<ClientInquiry>
    suspend fun saveInquiry(inquiry: ClientInquiry): Result<ClientInquiry>
    suspend fun deleteInquiry(id: String): Result<Boolean>
}

