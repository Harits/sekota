package com.sekota.features.auth.domain.usecase

import com.sekota.features.auth.domain.model.AuthRequest
import com.sekota.features.auth.domain.model.AuthResponse
import com.sekota.features.auth.domain.repository.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(request: AuthRequest): Result<AuthResponse> {
        return repository.login(request)
    }
}

class SignupUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(request: AuthRequest): Result<AuthResponse> {
        return repository.signup(request)
    }
}

class GetTokenUseCase(private val repository: AuthRepository) {
    operator fun invoke(): String? {
        return repository.getToken()
    }
}

class ClearTokenUseCase(private val repository: AuthRepository) {
    operator fun invoke() {
        repository.clearToken()
    }
}
