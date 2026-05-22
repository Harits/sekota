package com.sekota.features.profile.data.repository

import com.sekota.NetworkClient
import com.sekota.core.storage.TokenStorage
import com.sekota.features.profile.domain.model.UserProfile
import com.sekota.features.profile.domain.repository.ProfileRepository
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType

class ProfileRepositoryImpl(private val tokenStorage: TokenStorage) : ProfileRepository {
    override suspend fun getProfile(): Result<UserProfile> {
        return try {
            val token = tokenStorage.getToken()
            if (token == null) {
                return Result.failure(Exception("Not logged in"))
            }
            val response = NetworkClient.client.get("/profile") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }.body<UserProfile>()
            Result.success(response)
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
            val response = NetworkClient.client.post("/profile") {
                header(HttpHeaders.Authorization, "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(profile)
            }.body<UserProfile>()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
