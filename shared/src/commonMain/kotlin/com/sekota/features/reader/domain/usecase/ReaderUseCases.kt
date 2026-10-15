package com.sekota.features.reader.domain.usecase

import com.sekota.features.admin.domain.repository.AdminRepository
import com.sekota.features.reader.domain.model.BookReadingProgress

class SaveReadingProgressUseCase(private val repository: AdminRepository) {
    suspend operator fun invoke(progress: BookReadingProgress): Result<Unit> {
        return repository.saveReadingProgress(progress)
    }
}

class GetReadingProgressUseCase(private val repository: AdminRepository) {
    suspend operator fun invoke(bookId: String): BookReadingProgress? {
        return repository.getReadingProgress(bookId)
    }
}

class RecordReadingTelemetryUseCase(private val repository: AdminRepository) {
    suspend operator fun invoke(bookId: String, chapterIndex: Int, chapterTitle: String): Result<Boolean> {
        return repository.recordReadingTelemetry(bookId, chapterIndex, chapterTitle)
    }
}
