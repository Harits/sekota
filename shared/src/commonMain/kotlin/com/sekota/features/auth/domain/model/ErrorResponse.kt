package com.sekota.features.auth.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val error: String
)
