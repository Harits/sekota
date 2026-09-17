package com.sekota.features.admin.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ClientInquiry(
    val id: String,
    val name: String,
    val email: String,
    val message: String,
    val timestamp: String = "Baru Saja",
    val status: String = "New" // "New" or "Followed Up"
)
