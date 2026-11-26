package id.sekota

import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.server.testing.testApplication
import com.sun.net.httpserver.HttpServer
import java.net.InetSocketAddress
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ApplicationTest {

    private class RecordingMailer : ContactMailer {
        val mails = mutableListOf<ContactMail>()
        override fun send(mail: ContactMail) {
            mails += mail
        }
    }

    private fun form(name: String = "Rina", email: String = "rina@sekota.id") = Parameters.build {
        append("name", name)
        append("email", email)
        append("topic", "Demo Clarivio")
        append("subject", "[Website] Demo Clarivio – Rina")
        append("body", "Nama: Rina\nEmail: rina@sekota.id\nTopik: Demo Clarivio\n\nKami ingin demo.")
        append("language", "id")
        append("page", "#/id/contact")
    }

    @Test
    fun healthIsOk() = testApplication {
        application { module(readEnvironment = false) }
        val response = client.get("/health")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("ok", response.bodyAsText())
    }

    @Test
    fun validSubmissionIsDeliveredToMailer() = testApplication {
        val mailer = RecordingMailer()
        application { module(mailer) }
        val response = client.submitForm("/api/contact", form())
        assertEquals(HttpStatusCode.Accepted, response.status)
        val mail = mailer.mails.single()
        assertEquals("Rina", mail.name)
        assertEquals("rina@sekota.id", mail.email)
        assertEquals("Demo Clarivio", mail.topic)
        assertTrue(mail.body.contains("Kami ingin demo."))
        assertEquals("id", mail.language)
        assertEquals("#/id/contact", mail.page)
        assertNotNull(mail.sentAt)
    }

    @Test
    fun invalidSubmissionIsRejected() = testApplication {
        val mailer = RecordingMailer()
        application { module(mailer) }
        val response = client.submitForm("/api/contact", form(name = "R", email = "not-an-email"))
        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertTrue(mailer.mails.isEmpty())
    }

    @Test
    fun deliveryFailureIsReportedAsBadGateway() = testApplication {
        application {
            module(ContactMailer { throw IllegalStateException("smtp down") })
        }
        val response = client.submitForm("/api/contact", form())
        assertEquals(HttpStatusCode.BadGateway, response.status)
    }

    @Test
    fun formSubmitMailerPostsFormAndAcceptsSuccess() {
        var receivedBody = ""
        var receivedOrigin = ""
        val server = HttpServer.create(InetSocketAddress("127.0.0.1", 0), 0)
        server.createContext("/") { exchange ->
            receivedBody = exchange.requestBody.bufferedReader().readText()
            receivedOrigin = exchange.requestHeaders.getFirst("Origin").orEmpty()
            val payload = """{"success":"true"}""".toByteArray()
            exchange.sendResponseHeaders(200, payload.size.toLong())
            exchange.responseBody.use { it.write(payload) }
        }
        server.start()
        try {
            val mailer = FormSubmitMailer(
                to = "test@example.com",
                origin = "https://sekota.id",
                endpoint = "http://127.0.0.1:${server.address.port}/ajax",
            )
            mailer.send(
                ContactMail(
                    name = "Rina",
                    email = "rina@sekota.id",
                    topic = "Demo Clarivio",
                    subject = "[Website] Demo Clarivio – Rina",
                    body = "Nama: Rina | Email: rina@sekota.id | Kami ingin demo.",
                    language = "id",
                    page = "#/id/contact",
                ),
            )
            assertTrue(receivedBody.contains("name=Rina"))
            assertTrue(receivedBody.contains("email=rina%40sekota.id"))
            assertEquals("https://sekota.id", receivedOrigin)
        } finally {
            server.stop(0)
        }
    }

    @Test
    fun missingConfigurationReturnsServiceUnavailable() = testApplication {
        application { module(mailer = null, readEnvironment = false) }
        val response = client.submitForm("/api/contact", form())
        assertEquals(HttpStatusCode.ServiceUnavailable, response.status)
    }
}
