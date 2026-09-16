package com.sekota.features.auth.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val email: String,
    val password: String,
    val role: String? = "READER",
    val profile: String? = null,
    val phoneNumber: String? = null,
    val socialMedia: String? = null,
    val telegram: String? = null,
    val instagram: String? = null,
    val linkedin: String? = null,
    val xTwitter: String? = null,
    val facebook: String? = null
)
