package com.sekota.features.admin.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AdminMerch(
    val id: String,
    val title: String,
    val category: String,
    val seriesName: String,
    val price: Double,
    val rating: Double
)
