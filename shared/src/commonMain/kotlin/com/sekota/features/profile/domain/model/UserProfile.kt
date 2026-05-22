package com.sekota.features.profile.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val id: String,
    val username: String,
    val email: String = "",
    val fullName: String = ""
)
