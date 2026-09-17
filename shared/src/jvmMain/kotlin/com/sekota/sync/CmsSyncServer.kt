package com.sekota.sync

import com.sekota.core.storage.AdminDataStorage
import com.sekota.features.admin.data.repository.AdminRepositoryImpl
import com.sekota.features.admin.domain.model.ClientInquiry
import com.sekota.features.sync.domain.model.SyncDataPayload
import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpServer
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.InetSocketAddress
import java.nio.charset.StandardCharsets
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicLong

object CmsSyncServer {
    private var server: HttpServer? = null
    private val versionCounter = AtomicLong(System.currentTimeMillis())
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        prettyPrint = false
    }

    fun notifyDataChanged() {
        versionCounter.incrementAndGet()
    }

    fun start(port: Int = 8088) {
        if (server != null) return

        try {
            val httpServer = HttpServer.create(InetSocketAddress("0.0.0.0", port), 0)
            httpServer.executor = Executors.newFixedThreadPool(4)

            // /api/v1/sync
            httpServer.createContext("/api/v1/sync") { exchange ->
                handleCors(exchange)
                when (exchange.requestMethod.uppercase()) {
                    "OPTIONS" -> {
                        exchange.sendResponseHeaders(204, -1)
                    }
                    "GET" -> {
                        val repository = AdminRepositoryImpl()
                        val payload = runBlocking {
                            SyncDataPayload(
                                books = repository.getBooks(),
                                products = repository.getProducts(),
                                merch = repository.getMerchandise(),
                                metrics = repository.getLiveMetrics(),
                                inquiries = repository.getInquiries(),
                                version = versionCounter.get()
                            )
                        }
                        val responseJson = json.encodeToString(payload)
                        val bytes = responseJson.toByteArray(StandardCharsets.UTF_8)
                        exchange.responseHeaders.set("Content-Type", "application/json; charset=utf-8")
                        exchange.sendResponseHeaders(200, bytes.size.toLong())
                        exchange.responseBody.use { it.write(bytes) }
                    }
                    "POST" -> {
                        val body = exchange.requestBody.bufferedReader(StandardCharsets.UTF_8).readText()
                        try {
                            val payload = json.decodeFromString<SyncDataPayload>(body)
                            val storage = AdminDataStorage()
                            storage.saveBooksJson(json.encodeToString(payload.books))
                            storage.saveProductsJson(json.encodeToString(payload.products))
                            storage.saveMerchJson(json.encodeToString(payload.merch))
                            storage.saveMetricsJson(json.encodeToString(payload.metrics))
                            storage.saveInquiriesJson(json.encodeToString(payload.inquiries))
                            versionCounter.incrementAndGet()
                            exchange.sendResponseHeaders(200, -1)
                        } catch (e: Exception) {
                            val err = "{\"error\":\"${e.message}\"}".toByteArray(StandardCharsets.UTF_8)
                            exchange.sendResponseHeaders(400, err.size.toLong())
                            exchange.responseBody.use { it.write(err) }
                        }
                    }
                    else -> exchange.sendResponseHeaders(405, -1)
                }
            }

            // /api/v1/inquiry
            httpServer.createContext("/api/v1/inquiry") { exchange ->
                handleCors(exchange)
                when (exchange.requestMethod.uppercase()) {
                    "OPTIONS" -> {
                        exchange.sendResponseHeaders(204, -1)
                    }
                    "POST" -> {
                        val body = exchange.requestBody.bufferedReader(StandardCharsets.UTF_8).readText()
                        try {
                            val inquiry = json.decodeFromString<ClientInquiry>(body)
                            val repository = AdminRepositoryImpl()
                            runBlocking {
                                repository.saveInquiry(inquiry)
                            }
                            versionCounter.incrementAndGet()
                            val ok = "{\"status\":\"success\"}".toByteArray(StandardCharsets.UTF_8)
                            exchange.responseHeaders.set("Content-Type", "application/json; charset=utf-8")
                            exchange.sendResponseHeaders(200, ok.size.toLong())
                            exchange.responseBody.use { it.write(ok) }
                        } catch (e: Exception) {
                            val err = "{\"error\":\"${e.message}\"}".toByteArray(StandardCharsets.UTF_8)
                            exchange.sendResponseHeaders(400, err.size.toLong())
                            exchange.responseBody.use { it.write(err) }
                        }
                    }
                    else -> exchange.sendResponseHeaders(405, -1)
                }
            }

            // /api/v1/status
            httpServer.createContext("/api/v1/status") { exchange ->
                handleCors(exchange)
                if (exchange.requestMethod.equals("OPTIONS", ignoreCase = true)) {
                    exchange.sendResponseHeaders(204, -1)
                } else {
                    val statusJson = "{\"status\":\"online\",\"version\":${versionCounter.get()}}"
                    val bytes = statusJson.toByteArray(StandardCharsets.UTF_8)
                    exchange.responseHeaders.set("Content-Type", "application/json; charset=utf-8")
                    exchange.sendResponseHeaders(200, bytes.size.toLong())
                    exchange.responseBody.use { it.write(bytes) }
                }
            }

            httpServer.start()
            server = httpServer
            println(" Sekota CMS Desktop Sync Server started on port $port")
        } catch (e: Exception) {
            println("⚠️ Could not start CmsSyncServer: ${e.message}")
        }
    }

    fun stop() {
        server?.stop(0)
        server = null
    }

    private fun handleCors(exchange: HttpExchange) {
        exchange.responseHeaders.set("Access-Control-Allow-Origin", "*")
        exchange.responseHeaders.set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
        exchange.responseHeaders.set("Access-Control-Allow-Headers", "Content-Type, Authorization, *")
    }
}
