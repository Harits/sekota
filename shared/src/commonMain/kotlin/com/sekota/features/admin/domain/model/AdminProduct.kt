package com.sekota.features.admin.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AdminProduct(
    val id: String,
    val code: String,
    val name: String,
    val categoryEyebrow: String,
    val description: String,
    val features: List<String>
)
