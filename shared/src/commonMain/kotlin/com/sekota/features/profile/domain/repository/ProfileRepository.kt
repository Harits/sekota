package com.sekota.features.profile.domain.repository

import com.sekota.features.profile.domain.model.UserProfile

interface ProfileRepository {
    suspend fun getProfile(): Result<UserProfile>
    suspend fun updateProfile(profile: UserProfile): Result<UserProfile>
}
