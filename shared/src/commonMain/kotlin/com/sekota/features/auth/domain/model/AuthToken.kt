package com.sekota.features.auth.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthToken(val token: String)
