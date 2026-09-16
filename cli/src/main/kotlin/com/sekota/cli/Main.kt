package com.sekota.cli

import com.sekota.core.storage.TokenStorage
import com.sekota.features.admin.data.repository.AdminRepositoryImpl
import com.sekota.features.admin.domain.model.AdminBook
import com.sekota.features.admin.domain.usecase.*
import com.sekota.features.auth.data.repository.AuthRepositoryImpl
import com.sekota.features.auth.domain.model.AuthRequest
import com.sekota.features.auth.domain.usecase.ClearTokenUseCase
import com.sekota.features.auth.domain.usecase.GetTokenUseCase
import com.sekota.features.auth.domain.usecase.LoginUseCase
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) = runBlocking {
    val tokenStorage = TokenStorage()
    val authRepository = AuthRepositoryImpl(tokenStorage)
    val loginUseCase = LoginUseCase(authRepository)
    val getTokenUseCase = GetTokenUseCase(authRepository)
    val clearTokenUseCase = ClearTokenUseCase(authRepository)
    val validateAdminRoleUseCase = ValidateAdminRoleUseCase()

    val repository = AdminRepositoryImpl()
    val getBooksUseCase = GetAdminBooksUseCase(repository)
    val saveBookUseCase = SaveAdminBookUseCase(repository)
    val deleteBookUseCase = DeleteAdminBookUseCase(repository)
    val getProductsUseCase = GetAdminProductsUseCase(repository)
    val getMerchUseCase = GetAdminMerchUseCase(repository)

    if (args.isEmpty()) {
        printHelp(getTokenUseCase() != null)
        return@runBlocking
    }

    when (args[0]) {
        "login" -> {
            val email = args.getOrNull(1)
            val password = args.getOrNull(2)
            if (email == null || password == null) {
                println("❌ Usage: cli login <email> <password>")
                return@runBlocking
            }
            val result = loginUseCase(AuthRequest(email, password))
            if (result.isSuccess) {
                val auth = result.getOrNull()
                val role = auth?.role ?: "READER"
                if (validateAdminRoleUseCase(role)) {
                    println("✅ Login successful as ${role.uppercase()} ($email). Token stored in session.")
                } else {
                    println("⚠️ Warning: Authenticated with role '$role'. Note: CMS access requires ADMIN or BOD.")
                }
            } else {
                println("❌ Login failed: ${result.exceptionOrNull()?.message}")
            }
        }
        "logout" -> {
            clearTokenUseCase()
            println("✅ Successfully logged out from Sekota CLI.")
        }
        "whoami" -> {
            val token = getTokenUseCase()
            if (token != null) {
                println("🔑 Authenticated session active. Token: ${token.take(15)}...")
            } else {
                println("⚠️ No active authentication session. Run: cli login <email> <password>")
            }
        }
        "books" -> {
            when (args.getOrNull(1)) {
                "list" -> {
                    val books = getBooksUseCase()
                    println("--- Admin Books ---")
                    books.forEach {
                        println("${it.id.padEnd(5)} | ${it.title.padEnd(30)} | ${it.author.padEnd(20)} | ${it.isbn}")
                    }
                }
                "add" -> {
                    val id = args.getOrNull(2) ?: "b-${System.currentTimeMillis() % 1000}"
                    val title = args.getOrNull(3) ?: "New Book"
                    val author = args.getOrNull(4) ?: "Sekota Author"
                    val isbn = args.getOrNull(5) ?: "978-00000000"
                    val result = saveBookUseCase(AdminBook(id, title, author, isbn))
                    if (result.isSuccess) {
                        println("✅ Book added successfully: $id - $title")
                    } else {
                        println("❌ Failed to add book: ${result.exceptionOrNull()?.message}")
                    }
                }
                "delete" -> {
                    val id = args.getOrNull(2)
                    if (id == null) {
                        println("Usage: cli books delete <id>")
                    } else {
                        val result = deleteBookUseCase(id)
                        if (result.isSuccess && result.getOrDefault(false)) {
                            println("✅ Book deleted: $id")
                        } else {
                            println("❌ Book not found or failed to delete: $id")
                        }
                    }
                }
                else -> printHelp(getTokenUseCase() != null)
            }
        }
        "products" -> {
            if (args.getOrNull(1) == "list") {
                val products = getProductsUseCase()
                println("--- Intelligence Suite Products ---")
                products.forEach {
                    println("${it.id.padEnd(5)} | ${it.code.padEnd(5)} | ${it.name.padEnd(15)} | ${it.categoryEyebrow}")
                }
            } else {
                printHelp(getTokenUseCase() != null)
            }
        }
        "merch" -> {
            if (args.getOrNull(1) == "list") {
                val merch = getMerchUseCase()
                println("--- Merchandise ---")
                merch.forEach {
                    println("${it.id.padEnd(5)} | ${it.title.padEnd(30)} | ${it.category.padEnd(15)} | \$${it.price}")
                }
            } else {
                printHelp(getTokenUseCase() != null)
            }
        }
        "help" -> printHelp(getTokenUseCase() != null)
        else -> {
            println("Unknown command: ${args[0]}")
            printHelp(getTokenUseCase() != null)
        }
    }
}

fun printHelp(isLoggedIn: Boolean) {
    val sessionStatus = if (isLoggedIn) "ACTIVE" else "NONE"
    println("""
        Sekota Admin CLI (Production Engineering v1.0) [Session: $sessionStatus]
        Usage: cli [command] [action] [options]
        
        Auth Commands:
          login <email> <password>      - Authenticates admin and saves session token
          logout                        - Clears stored session token
          whoami                        - Shows active session status
        
        Management Commands:
          books list                    - Prints list of books
          books add [id] [title] [auth] - Adds a new book
          books delete <id>             - Deletes a book by ID
          products list                 - Prints list of intelligence suite products
          merch list                    - Prints merchandise list
          help                          - Prints this help message
    """.trimIndent())
}
