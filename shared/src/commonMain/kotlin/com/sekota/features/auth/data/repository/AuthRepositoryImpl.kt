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
import io.ktor.http.isSuccess

class AuthRepositoryImpl(private val tokenStorage: TokenStorage) : AuthRepository {
    override suspend fun login(request: AuthRequest): Result<AuthResponse> {
        return try {
            val httpResponse = NetworkClient.authClient.post("${com.sekota.AUTH_BASE_URL}auth/login") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            if (httpResponse.status.isSuccess()) {
                val response = httpResponse.body<AuthResponse>()
                saveToken(response.token)
                Result.success(response)
            } else {
                val errorMessage = try {
                    val errorObj = httpResponse.body<com.sekota.features.auth.domain.model.ErrorResponse>()
                    errorObj.error
                } catch (_: Exception) {
                    try {
                        httpResponse.body<String>().ifBlank { "HTTP ${httpResponse.status}" }
                    } catch (_: Exception) {
                        "Authentication failed (${httpResponse.status.value})"
                    }
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signup(request: AuthRequest): Result<AuthResponse> {
        return try {
            val httpResponse = NetworkClient.authClient.post("${com.sekota.AUTH_BASE_URL}auth/register") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            if (httpResponse.status.isSuccess()) {
                // If register returns created response with token, or message + userId
                val response = try {
                    httpResponse.body<AuthResponse>()
                } catch (_: Exception) {
                    // Try automatic login if register returns only userId
                    val loginRes = NetworkClient.authClient.post("${com.sekota.AUTH_BASE_URL}auth/login") {
                        contentType(ContentType.Application.Json)
                        setBody(request)
                    }
                    if (loginRes.status.isSuccess()) {
                        loginRes.body<AuthResponse>()
                    } else {
                        throw Exception("Registration succeeded, please log in.")
                    }
                }
                saveToken(response.token)
                Result.success(response)
            } else {
                val errorMessage = try {
                    val errorObj = httpResponse.body<com.sekota.features.auth.domain.model.ErrorResponse>()
                    errorObj.error
                } catch (_: Exception) {
                    try {
                        httpResponse.body<String>().ifBlank { "HTTP ${httpResponse.status}" }
                    } catch (_: Exception) {
                        "Registration failed (${httpResponse.status.value})"
                    }
                }
                Result.failure(Exception(errorMessage))
            }
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
