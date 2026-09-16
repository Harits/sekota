package com.sekota.features.admin.domain.model

data class AdminMerch(
    val id: String,
    val title: String,
    val category: String,
    val seriesName: String,
    val price: Double,
    val rating: Double
)
