package com.sekota.features.auth.domain.repository

import com.sekota.features.auth.domain.model.AuthRequest
import com.sekota.features.auth.domain.model.AuthResponse

interface AuthRepository {
    suspend fun login(request: AuthRequest): Result<AuthResponse>
    suspend fun signup(request: AuthRequest): Result<AuthResponse>
    fun getToken(): String?
    fun saveToken(token: String)
    fun clearToken()
}
