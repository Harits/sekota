package com.sekota.features.sync.domain.model

import com.sekota.features.admin.domain.model.AdminBook
import com.sekota.features.admin.domain.model.AdminLiveMetrics
import com.sekota.features.admin.domain.model.AdminMerch
import com.sekota.features.admin.domain.model.AdminProduct
import com.sekota.features.admin.domain.model.ClientInquiry
import kotlinx.serialization.Serializable

@Serializable
data class SyncDataPayload(
    val books: List<AdminBook> = emptyList(),
    val products: List<AdminProduct> = emptyList(),
    val merch: List<AdminMerch> = emptyList(),
    val metrics: AdminLiveMetrics = AdminLiveMetrics(),
    val inquiries: List<ClientInquiry> = emptyList(),
    val version: Long = 0L
)
