package com.sekota.features.admin.domain.usecase

import com.sekota.features.admin.domain.repository.AdminRepository

class DeleteAdminInquiryUseCase(
    private val repository: AdminRepository
) {
    suspend operator fun invoke(id: String): Result<Boolean> {
        return repository.deleteInquiry(id)
    }
}
