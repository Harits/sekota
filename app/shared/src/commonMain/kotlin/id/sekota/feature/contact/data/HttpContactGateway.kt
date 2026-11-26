package id.sekota.feature.contact.data

import id.sekota.feature.contact.domain.ContactGateway
import id.sekota.feature.contact.domain.ContactMessage
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.forms.submitForm
import io.ktor.http.Parameters
import io.ktor.http.isSuccess

/** Base URL of the Sekota API; empty string means "same origin". */
internal expect fun contactApiBaseUrl(): String

internal object ContactHttp {
    val client: HttpClient by lazy {
        HttpClient {
            expectSuccess = false
            install(HttpTimeout) {
                requestTimeoutMillis = 15_000
                connectTimeoutMillis = 10_000
            }
        }
    }
}

/**
 * Posts the message to `POST /api/contact` (form encoded). The endpoint is
 * implemented by `:server`, which relays the message over SMTP. Any network
 * error is reported as a failure so the UI never claims a message was sent.
 */
class HttpContactGateway(
    private val client: HttpClient = ContactHttp.client,
    private val baseUrl: String = contactApiBaseUrl(),
) : ContactGateway {

    override suspend fun send(message: ContactMessage): Boolean = runCatching {
        val response = client.submitForm(
            url = "${baseUrl.trimEnd('/')}/api/contact",
            formParameters = Parameters.build {
                append("name", message.name)
                append("email", message.email)
                append("topic", message.topic)
                append("subject", message.subject)
                append("body", message.body)
                append("language", message.language)
                append("page", message.page)
            },
        )
        response.status.isSuccess()
    }.getOrDefault(false)
}
