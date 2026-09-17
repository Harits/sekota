package com.sekota.features.sync.data.repository

import com.sekota.core.storage.AdminDataStorage
import com.sekota.features.admin.domain.model.ClientInquiry
import com.sekota.features.sync.domain.model.SyncDataPayload
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SyncService(
    private val dataStorage: AdminDataStorage = AdminDataStorage()
) {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        prettyPrint = false
    }

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(json)
        }
    }

    private val _syncState = MutableStateFlow("Disconnected")
    val syncState: StateFlow<String> = _syncState.asStateFlow()

    private val _syncEventFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val syncEventFlow: SharedFlow<Unit> = _syncEventFlow.asSharedFlow()

    private var lastSyncedVersion: Long = -1L
    private var syncJob: Job? = null

    fun connect(scope: CoroutineScope) {
        if (syncJob?.isActive == true) return

        syncJob = scope.launch {
            _syncState.value = "Connecting..."
            while (isActive) {
                try {
                    val response = client.get("http://localhost:8088/api/v1/sync")
                    if (response.status.isSuccess()) {
                        val bodyText = response.bodyAsText()
                        val payload = json.decodeFromString<SyncDataPayload>(bodyText)
                        if (payload.version != lastSyncedVersion) {
                            lastSyncedVersion = payload.version
                            dataStorage.saveBooksJson(json.encodeToString(payload.books))
                            dataStorage.saveProductsJson(json.encodeToString(payload.products))
                            dataStorage.saveMerchJson(json.encodeToString(payload.merch))
                            dataStorage.saveMetricsJson(json.encodeToString(payload.metrics))
                            dataStorage.saveInquiriesJson(json.encodeToString(payload.inquiries))
                            _syncEventFlow.tryEmit(Unit)
                        }
                        _syncState.value = "Connected"
                    } else {
                        _syncState.value = "Disconnected"
                    }
                } catch (_: Exception) {
                    _syncState.value = "Disconnected"
                }
                delay(1500)
            }
        }
    }

    suspend fun submitInquiry(inquiry: ClientInquiry): Boolean {
        // 1. Try sending to Desktop CMS Sync Server
        try {
            val response = client.post("http://localhost:8088/api/v1/inquiry") {
                contentType(ContentType.Application.Json)
                setBody(inquiry)
            }
            if (response.status.isSuccess()) {
                return true
            }
        } catch (_: Exception) {}

        // 2. Fallback to local storage
        val current = dataStorage.getInquiriesJson()?.let {
            try { json.decodeFromString<List<ClientInquiry>>(it) } catch (_: Exception) { null }
        } ?: emptyList()
        val updated = listOf(inquiry) + current
        dataStorage.saveInquiriesJson(json.encodeToString(updated))
        _syncEventFlow.tryEmit(Unit)
        return true
    }

    fun triggerLocalUpdate() {
        _syncEventFlow.tryEmit(Unit)
    }

    fun disconnect() {
        syncJob?.cancel()
        syncJob = null
        _syncState.value = "Disconnected"
    }
}
