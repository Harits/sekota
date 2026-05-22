package com.sekota.features.profile.domain.usecase

import com.sekota.features.profile.domain.model.UserProfile
import com.sekota.features.profile.domain.repository.ProfileRepository

class GetProfileUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(): Result<UserProfile> {
        return repository.getProfile()
    }
}

class UpdateProfileUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(profile: UserProfile): Result<UserProfile> {
        return repository.updateProfile(profile)
    }
}
