package com.sekota.features.admin.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ClientInquiry(
    val id: String,
    val name: String,
    val email: String,
    val message: String,
    val timestamp: String = "Baru Saja",
    val status: String = "New", // "New" or "Followed Up"
    /** Topic picked on the contact page (CORE-07-01-02 / #22657); blank for legacy inquiries. */
    val topic: String = ""
)
