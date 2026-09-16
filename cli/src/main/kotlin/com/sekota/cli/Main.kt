package com.sekota.cli

import com.sekota.features.admin.data.repository.AdminRepositoryImpl
import com.sekota.features.admin.domain.model.AdminBook
import com.sekota.features.admin.domain.usecase.*
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) = runBlocking {
    val repository = AdminRepositoryImpl()
    val getBooksUseCase = GetAdminBooksUseCase(repository)
    val saveBookUseCase = SaveAdminBookUseCase(repository)
    val deleteBookUseCase = DeleteAdminBookUseCase(repository)
    val getProductsUseCase = GetAdminProductsUseCase(repository)
    val getMerchUseCase = GetAdminMerchUseCase(repository)

    if (args.isEmpty()) {
        printHelp()
        return@runBlocking
    }

    when (args[0]) {
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
                else -> printHelp()
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
                printHelp()
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
                printHelp()
            }
        }
        "help" -> printHelp()
        else -> {
            println("Unknown command: ${args[0]}")
            printHelp()
        }
    }
}

fun printHelp() {
    println("""
        Sekota Admin CLI (Production Engineering v1.0)
        Usage: cli [command] [action] [options]
        
        Commands:
          books list                    - Prints list of books
          books add [id] [title] [auth] - Adds a new book
          books delete <id>             - Deletes a book by ID
          products list                 - Prints list of intelligence suite products
          merch list                    - Prints merchandise list
          help                          - Prints this help message
    """.trimIndent())
}
