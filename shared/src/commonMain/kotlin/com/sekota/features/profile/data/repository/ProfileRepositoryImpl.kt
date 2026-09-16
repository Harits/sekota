package com.sekota.features.profile.data.repository

import com.sekota.NetworkClient
import com.sekota.core.storage.TokenStorage
import com.sekota.features.profile.domain.model.UserProfile
import com.sekota.features.profile.domain.repository.ProfileRepository
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.isSuccess

class ProfileRepositoryImpl(private val tokenStorage: TokenStorage) : ProfileRepository {
    override suspend fun getProfile(): Result<UserProfile> {
        return try {
            val token = tokenStorage.getToken()
            if (token == null) {
                return Result.failure(Exception("Not logged in"))
            }
            val httpResponse = NetworkClient.authClient.get("${com.sekota.AUTH_BASE_URL}auth/profile") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }
            if (httpResponse.status.isSuccess()) {
                val profileMap = httpResponse.body<Map<String, String?>>()
                val email = profileMap["email"] ?: ""
                val role = profileMap["role"] ?: "READER"
                val profileDesc = profileMap["profile"] ?: ""
                val profile = UserProfile(
                    id = email,
                    username = email.substringBefore("@"),
                    email = email,
                    fullName = profileDesc.ifBlank { email.substringBefore("@") },
                    role = role
                )
                Result.success(profile)
            } else {
                Result.failure(Exception("Failed to fetch profile (${httpResponse.status.value})"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateProfile(profile: UserProfile): Result<UserProfile> {
        return try {
            val token = tokenStorage.getToken()
            if (token == null) {
                return Result.failure(Exception("Not logged in"))
            }
            val httpResponse = NetworkClient.authClient.put("${com.sekota.AUTH_BASE_URL}auth/profile") {
                header(HttpHeaders.Authorization, "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(mapOf(
                    "profile" to profile.fullName,
                    "email" to profile.email
                ))
            }
            if (httpResponse.status.isSuccess()) {
                Result.success(profile)
            } else {
                Result.failure(Exception("Failed to update profile (${httpResponse.status.value})"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
