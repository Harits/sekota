package com.sekota.features.admin.domain.usecase

import com.sekota.features.admin.domain.model.ClientInquiry
import com.sekota.features.admin.domain.repository.AdminRepository

class GetAdminInquiriesUseCase(
    private val repository: AdminRepository
) {
    suspend operator fun invoke(): List<ClientInquiry> {
        return repository.getInquiries()
    }
}
