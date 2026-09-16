package com.sekota.features.admin.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AdminLiveMetrics(
    val dataAccuracy: String = "99.8%",
    val totalClients: String = "100+",
    val establishedYear: String = "2021"
)
