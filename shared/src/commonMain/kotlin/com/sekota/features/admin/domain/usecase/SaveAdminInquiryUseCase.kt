package com.sekota.features.admin.domain.usecase

import com.sekota.features.admin.domain.model.ClientInquiry
import com.sekota.features.admin.domain.repository.AdminRepository

class SaveAdminInquiryUseCase(
    private val repository: AdminRepository
) {
    suspend operator fun invoke(inquiry: ClientInquiry): Result<ClientInquiry> {
        if (inquiry.name.isBlank()) {
            return Result.failure(IllegalArgumentException("Nama lengkap tidak boleh kosong"))
        }
        if (inquiry.email.isBlank() || !inquiry.email.contains("@")) {
            return Result.failure(IllegalArgumentException("Email bisnis tidak valid"))
        }
        if (inquiry.message.isBlank()) {
            return Result.failure(IllegalArgumentException("Pesan kebutuhan tidak boleh kosong"))
        }
        return repository.saveInquiry(inquiry)
    }
}
