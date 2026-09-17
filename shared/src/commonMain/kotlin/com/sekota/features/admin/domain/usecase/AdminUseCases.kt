package com.sekota.features.admin.domain.usecase

import com.sekota.features.admin.domain.model.AdminBook
import com.sekota.features.admin.domain.model.AdminLiveMetrics
import com.sekota.features.admin.domain.model.AdminMerch
import com.sekota.features.admin.domain.model.AdminProduct
import com.sekota.features.admin.domain.repository.AdminRepository

class GetAdminBooksUseCase(private val repository: AdminRepository) {
    suspend operator fun invoke(): List<AdminBook> {
        return repository.getBooks()
    }
}

class GetBookByIdUseCase(private val repository: AdminRepository) {
    suspend operator fun invoke(id: String): AdminBook? {
        return repository.getBookById(id)
    }
}

class SaveAdminBookUseCase(private val repository: AdminRepository) {
    suspend operator fun invoke(book: AdminBook): Result<AdminBook> {
        if (book.title.isBlank()) {
            return Result.failure(IllegalArgumentException("Judul buku tidak boleh kosong"))
        }
        if (book.author.isBlank()) {
            return Result.failure(IllegalArgumentException("Penulis buku tidak boleh kosong"))
        }
        return repository.saveBook(book)
    }
}

class DeleteAdminBookUseCase(private val repository: AdminRepository) {
    suspend operator fun invoke(id: String): Result<Boolean> {
        if (id.isBlank()) {
            return Result.failure(IllegalArgumentException("ID buku tidak valid"))
        }
        return repository.deleteBook(id)
    }
}

class GetAdminProductsUseCase(private val repository: AdminRepository) {
    suspend operator fun invoke(): List<AdminProduct> {
        return repository.getProducts()
    }
}

class SaveAdminProductUseCase(private val repository: AdminRepository) {
    suspend operator fun invoke(product: AdminProduct): Result<AdminProduct> {
        if (product.name.isBlank()) {
            return Result.failure(IllegalArgumentException("Nama produk tidak boleh kosong"))
        }
        if (product.code.isBlank()) {
            return Result.failure(IllegalArgumentException("Kode produk tidak boleh kosong"))
        }
        return repository.saveProduct(product)
    }
}

class DeleteAdminProductUseCase(private val repository: AdminRepository) {
    suspend operator fun invoke(id: String): Result<Boolean> {
        if (id.isBlank()) {
            return Result.failure(IllegalArgumentException("ID produk tidak valid"))
        }
        return repository.deleteProduct(id)
    }
}

class GetAdminMerchUseCase(private val repository: AdminRepository) {
    suspend operator fun invoke(): List<AdminMerch> {
        return repository.getMerchandise()
    }
}

class SaveAdminMerchUseCase(private val repository: AdminRepository) {
    suspend operator fun invoke(merch: AdminMerch): Result<AdminMerch> {
        if (merch.title.isBlank()) {
            return Result.failure(IllegalArgumentException("Nama merchandise tidak boleh kosong"))
        }
        if (merch.price <= 0.0) {
            return Result.failure(IllegalArgumentException("Harga merchandise harus lebih dari 0"))
        }
        return repository.saveMerchandise(merch)
    }
}

class DeleteAdminMerchUseCase(private val repository: AdminRepository) {
    suspend operator fun invoke(id: String): Result<Boolean> {
        if (id.isBlank()) {
            return Result.failure(IllegalArgumentException("ID merchandise tidak valid"))
        }
        return repository.deleteMerchandise(id)
    }
}

class GetAdminLiveMetricsUseCase(private val repository: AdminRepository) {
    suspend operator fun invoke(): AdminLiveMetrics {
        return repository.getLiveMetrics()
    }
}

class SaveAdminLiveMetricsUseCase(private val repository: AdminRepository) {
    suspend operator fun invoke(metrics: AdminLiveMetrics): Result<AdminLiveMetrics> {
        if (metrics.dataAccuracy.isBlank()) {
            return Result.failure(IllegalArgumentException("Data Accuracy tidak boleh kosong"))
        }
        if (metrics.totalClients.isBlank()) {
            return Result.failure(IllegalArgumentException("Total Clients tidak boleh kosong"))
        }
        if (metrics.establishedYear.isBlank()) {
            return Result.failure(IllegalArgumentException("Established Year tidak boleh kosong"))
        }
        return repository.saveLiveMetrics(metrics)
    }
}

class ValidateAdminRoleUseCase {
    operator fun invoke(role: String?): Boolean {
        if (role == null) return false
        val normalized = role.trim().uppercase()
        return normalized == "ADMIN" || normalized == "BOD" || normalized == "SYSADMIN"
    }
}
