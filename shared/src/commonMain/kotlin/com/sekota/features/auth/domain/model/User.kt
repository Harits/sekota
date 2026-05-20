package com.sekota.features.auth.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val username: String,
    val id: String = ""
)
