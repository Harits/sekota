package com.sekota.features.admin.domain.repository

import com.sekota.features.admin.domain.model.AdminBook
import com.sekota.features.admin.domain.model.AdminLiveMetrics
import com.sekota.features.admin.domain.model.AdminMerch
import com.sekota.features.admin.domain.model.AdminProduct

import com.sekota.features.admin.domain.model.ClientInquiry
import com.sekota.features.admin.domain.model.StrategicConversationConfig
import com.sekota.features.admin.domain.model.WhySekotaConfig

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

    suspend fun getValueLoop(): com.sekota.features.admin.domain.model.ValueLoopConfig
    suspend fun saveValueLoop(config: com.sekota.features.admin.domain.model.ValueLoopConfig): Result<com.sekota.features.admin.domain.model.ValueLoopConfig>

    suspend fun getCategories(): List<String>
    suspend fun addCategory(category: String): Result<List<String>>
    suspend fun deleteCategory(category: String): Result<List<String>>

    suspend fun getWhySekota(): WhySekotaConfig
    suspend fun saveWhySekota(config: WhySekotaConfig): Result<WhySekotaConfig>

    suspend fun getStrategicConversation(): StrategicConversationConfig
    suspend fun saveStrategicConversation(config: StrategicConversationConfig): Result<StrategicConversationConfig>

    suspend fun getFooter(): com.sekota.features.admin.domain.model.FooterConfig
    suspend fun saveFooter(config: com.sekota.features.admin.domain.model.FooterConfig): Result<com.sekota.features.admin.domain.model.FooterConfig>

    suspend fun getNavigationItems(): List<com.sekota.features.navigation.domain.model.NavigationItem>
    suspend fun saveNavigationItems(items: List<com.sekota.features.navigation.domain.model.NavigationItem>): Result<List<com.sekota.features.navigation.domain.model.NavigationItem>>

    suspend fun saveReadingProgress(progress: com.sekota.features.reader.domain.model.BookReadingProgress): Result<Unit>
    suspend fun getReadingProgress(bookId: String): com.sekota.features.reader.domain.model.BookReadingProgress?
    suspend fun recordReadingTelemetry(bookId: String, chapterIndex: Int, chapterTitle: String): Result<Boolean>
}

