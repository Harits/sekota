package com.sekota.data.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init(
        driverClassName: String = "org.postgresql.Driver",
        jdbcUrl: String = System.getenv("DB_JDBC_URL") ?: "jdbc:postgresql://localhost:5432/sekotadb",
        username: String = System.getenv("DB_USERNAME") ?: "postgres",
        password: String = System.getenv("DB_PASSWORD") ?: "password"
    ) {
        val database = Database.connect(hikari(driverClassName, jdbcUrl, username, password))
        transaction(database) {
            SchemaUtils.create(SprintsTable, WorkPackagesTable)
        }
    }

    private fun hikari(
        driverClassName: String,
        jdbcUrl: String,
        username: String,
        password: String
    ): HikariDataSource {
        val config = HikariConfig().apply {
            this.driverClassName = driverClassName
            this.jdbcUrl = jdbcUrl
            this.username = username
            this.password = password
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }
        return HikariDataSource(config)
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}
