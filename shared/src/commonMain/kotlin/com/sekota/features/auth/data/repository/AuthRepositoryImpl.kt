package com.sekota.features.auth.data.repository

import com.sekota.NetworkClient
import com.sekota.core.storage.TokenStorage
import com.sekota.features.auth.domain.model.AuthRequest
import com.sekota.features.auth.domain.model.AuthResponse
import com.sekota.features.auth.domain.repository.AuthRepository
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AuthRepositoryImpl(private val tokenStorage: TokenStorage) : AuthRepository {
    override suspend fun login(request: AuthRequest): Result<AuthResponse> {
        return try {
            val response = NetworkClient.client.post("/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }.body<AuthResponse>()
            saveToken(response.token)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signup(request: AuthRequest): Result<AuthResponse> {
        return try {
            val response = NetworkClient.client.post("/auth/signup") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }.body<AuthResponse>()
            saveToken(response.token)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getToken(): String? {
        return tokenStorage.getToken()
    }

    override fun saveToken(token: String) {
        tokenStorage.saveToken(token)
    }

    override fun clearToken() {
        tokenStorage.clearToken()
    }
}
