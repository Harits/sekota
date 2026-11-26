package id.sekota

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.HttpHeaders
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.http.content.staticFiles
import jakarta.mail.Authenticator
import jakarta.mail.Message
import jakarta.mail.MessagingException
import jakarta.mail.PasswordAuthentication
import jakarta.mail.Session
import jakarta.mail.Transport
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import java.io.File
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets
import java.time.Duration
import java.time.Instant
import java.util.Properties

data class ContactMail(
    val name: String,
    val email: String,
    val topic: String,
    val subject: String,
    val body: String,
    val language: String,
    val page: String,
    val sentAt: String = Instant.now().toString(),
)

fun interface ContactMailer {
    fun send(mail: ContactMail)
}

/**
 * Test/alternative delivery without SMTP credentials. The recipient must have
 * activated the FormSubmit form once (an activation email is sent on first use).
 */
class FormSubmitMailer(
    private val to: String,
    private val origin: String = "https://sekota.id",
    private val endpoint: String = "https://formsubmit.co/ajax",
) : ContactMailer {

    override fun send(mail: ContactMail) {
        val form = listOf(
            "name" to mail.name,
            "email" to mail.email,
            "topic" to mail.topic,
            "message" to mail.body,
            "_subject" to mail.subject,
            "_template" to "table",
            "_captcha" to "false",
        ).joinToString("&") { (key, value) -> "${enc(key)}=${enc(value)}" }

        val request = HttpRequest.newBuilder(URI.create("${endpoint.trimEnd('/')}/$to"))
            .timeout(Duration.ofSeconds(25))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .header("Accept", "application/json")
            .header("Origin", origin)
            .header("Referer", "$origin/")
            .header("X-Requested-With", "XMLHttpRequest")
            .POST(HttpRequest.BodyPublishers.ofString(form))
            .build()

        val response = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build()
            .send(request, HttpResponse.BodyHandlers.ofString())
        val body = response.body()
        val accepted = response.statusCode() in 200..299 &&
            (body.contains("\"success\":\"true\"") || body.contains("\"success\":true"))
        if (!accepted) {
            throw IllegalStateException("FormSubmit failed (${response.statusCode()}): $body")
        }
    }

    private fun enc(value: String): String =
        URLEncoder.encode(value, StandardCharsets.UTF_8)
}

data class SmtpSettings(
    val host: String,
    val port: Int,
    val username: String,
    val password: String,
    val from: String,
    val to: String,
    val startTls: Boolean,
) {
    companion object {
        /** Reads SMTP settings from the environment; null means email is not configured. */
        fun fromEnv(env: Map<String, String> = System.getenv()): SmtpSettings? {
            val host = env["SEKOTA_SMTP_HOST"]?.trim().orEmpty()
            if (host.isEmpty()) return null
            return SmtpSettings(
                host = host,
                port = env["SEKOTA_SMTP_PORT"]?.toIntOrNull() ?: 587,
                username = env["SEKOTA_SMTP_USER"].orEmpty(),
                password = env["SEKOTA_SMTP_PASSWORD"].orEmpty(),
                from = env["SEKOTA_MAIL_FROM"]?.trim()?.takeIf { it.isNotEmpty() }
                    ?: env["SEKOTA_SMTP_USER"]?.trim().orEmpty().ifEmpty { "no-reply@sekota.id" },
                to = env["SEKOTA_MAIL_TO"]?.trim()?.takeIf { it.isNotEmpty() } ?: "info@sekota.id",
                startTls = env["SEKOTA_SMTP_STARTTLS"]?.toBooleanStrictOrNull() ?: true,
            )
        }
    }
}

class SmtpMailer(private val settings: SmtpSettings) : ContactMailer {
    override fun send(mail: ContactMail) {
        val props = Properties().apply {
            put("mail.smtp.host", settings.host)
            put("mail.smtp.port", settings.port.toString())
            put("mail.smtp.auth", settings.username.isNotEmpty().toString())
            put("mail.smtp.starttls.enable", settings.startTls.toString())
            put("mail.smtp.connectiontimeout", "10000")
            put("mail.smtp.timeout", "10000")
            put("mail.smtp.writetimeout", "10000")
        }
        val session = if (settings.username.isNotEmpty()) {
            Session.getInstance(props, object : Authenticator() {
                override fun getPasswordAuthentication() =
                    PasswordAuthentication(settings.username, settings.password)
            })
        } else {
            Session.getInstance(props)
        }
        try {
            val message = MimeMessage(session).apply {
                setFrom(InternetAddress(settings.from))
                setRecipients(Message.RecipientType.TO, InternetAddress.parse(settings.to))
                setReplyTo(arrayOf(InternetAddress(mail.email)))
                setSubject(mail.subject, "UTF-8")
                setText(mail.body, "UTF-8")
                setHeader("X-Sekota-Form", "contact")
                setHeader("X-Sekota-Language", mail.language)
                setHeader("X-Sekota-Page", mail.page)
            }
            Transport.send(message)
        } catch (error: MessagingException) {
            throw IllegalStateException("SMTP delivery failed: ${error.message}", error)
        }
    }
}

fun main() {
    val port = System.getenv("PORT")?.toIntOrNull() ?: 8080
    embeddedServer(Netty, port = port, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module(mailer: ContactMailer? = null, readEnvironment: Boolean = true) {
    val configuredMailer = mailer
        ?: if (readEnvironment) {
            SmtpSettings.fromEnv()?.let(::SmtpMailer)
                ?: System.getenv("SEKOTA_FORMSUBMIT_TO")
                    ?.trim()
                    ?.takeIf { it.isNotEmpty() }
                    ?.let { to ->
                        val origin = System.getenv("SEKOTA_FORMSUBMIT_ORIGIN")
                            ?.trim()
                            ?.takeIf { it.isNotEmpty() }
                            ?: "https://sekota.id"
                        FormSubmitMailer(to = to, origin = origin)
                    }
        } else {
            null
        }
    val allowedOrigins = (System.getenv("SEKOTA_ALLOWED_ORIGINS")
        ?: "http://localhost:8765,http://127.0.0.1:8765,http://localhost:8080,http://127.0.0.1:8080,http://localhost:8090,http://127.0.0.1:8090")
        .split(',')
        .map { it.trim().trimEnd('/') }
        .filter { it.isNotEmpty() }

    routing {
        get("/health") {
            call.respondText("ok", ContentType.Text.Plain)
        }

        options("/api/contact") {
            applyCors(call, allowedOrigins)
            call.respond(HttpStatusCode.NoContent)
        }

        post("/api/contact") {
            applyCors(call, allowedOrigins)
            if (configuredMailer == null) {
                call.respondText(
                    """{"status":"not_configured"}""",
                    ContentType.Application.Json,
                    HttpStatusCode.ServiceUnavailable,
                )
                return@post
            }
            val form = call.receiveParameters()
            val name = form["name"].orEmpty().trim()
            val email = form["email"].orEmpty().trim()
            val topic = form["topic"].orEmpty().trim().ifEmpty { "Konsultasi umum" }
            val subject = form["subject"].orEmpty().trim()
            val body = form["body"].orEmpty().trim()
            val problem = validate(name, email)
            if (problem != null) {
                call.respondText(
                    """{"status":"invalid","message":"$problem"}""",
                    ContentType.Application.Json,
                    HttpStatusCode.BadRequest,
                )
                return@post
            }
            val mail = ContactMail(
                name = name,
                email = email,
                topic = topic,
                subject = subject.ifEmpty { "[Website] $topic – $name" },
                body = body.ifEmpty { "Nama: $name\nEmail: $email\nTopik: $topic" },
                language = form["language"].orEmpty().ifEmpty { "id" },
                page = form["page"].orEmpty(),
            )
            try {
                configuredMailer.send(mail)
                call.respondText(
                    """{"status":"sent"}""",
                    ContentType.Application.Json,
                    HttpStatusCode.Accepted,
                )
            } catch (error: Exception) {
                log.warn("Contact delivery failed", error)
                call.respondText(
                    """{"status":"failed","message":"Email delivery failed"}""",
                    ContentType.Application.Json,
                    HttpStatusCode.BadGateway,
                )
            }
        }

        val webDir = System.getenv("SEKOTA_WEB_DIR")?.let(::File)
        if (webDir != null && webDir.isDirectory) {
            staticFiles("/", webDir) {
                default("index.html")
            }
        }
    }
}

private val emailPattern = Regex("^[^\\s@]+@[^\\s@]+\\.[^\\s@]{2,}$")

private fun validate(name: String, email: String): String? {
    if (name.length < 2) return "Name is required"
    if (!emailPattern.matches(email)) return "A valid email is required"
    if (name.length > 200 || email.length > 200) return "Field too long"
    return null
}

private fun applyCors(call: ApplicationCall, allowedOrigins: List<String>) {
    val origin = call.request.headers[HttpHeaders.Origin]?.trimEnd('/') ?: return
    if (origin in allowedOrigins) {
        call.response.headers.append(HttpHeaders.AccessControlAllowOrigin, origin)
        call.response.headers.append(HttpHeaders.Vary, HttpHeaders.Origin)
        call.response.headers.append(HttpHeaders.AccessControlAllowMethods, "POST, OPTIONS")
        call.response.headers.append(HttpHeaders.AccessControlAllowHeaders, "Content-Type")
    }
}
