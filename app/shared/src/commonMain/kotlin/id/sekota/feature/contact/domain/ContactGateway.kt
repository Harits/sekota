package id.sekota.feature.contact.domain

import id.sekota.core.model.Language

/**
 * A validated message handed to the delivery layer. The body is built in the
 * domain so the email a visitor triggers is exactly the one that was tested.
 */
data class ContactMessage(
    val name: String,
    val email: String,
    val topic: String,
    val subject: String,
    val body: String,
    val language: String,
    val page: String,
)

/** Delivery boundary: the API/SMTP implementation lives in `data`. */
interface ContactGateway {
    suspend fun send(message: ContactMessage): Boolean
}

sealed interface ContactSendResult {
    data class Sent(val name: String, val email: String) : ContactSendResult
    data class Invalid(val validation: ContactValidation) : ContactSendResult
    data object Failed : ContactSendResult
}

/**
 * Validates, builds the email body and asks the gateway to deliver it.
 * A failed delivery never reports success; the form keeps its contents.
 */
class SendContactMessage(
    private val gateway: ContactGateway,
    private val draftBuilder: SubmitContactMessage = SubmitContactMessage(),
) {
    suspend operator fun invoke(
        form: ContactForm,
        topicLabel: String,
        labels: ContactLabels,
        language: Language,
        page: String,
    ): ContactSendResult {
        return when (val draftResult = draftBuilder(form, topicLabel, labels)) {
            is ContactSubmitResult.Invalid -> ContactSendResult.Invalid(draftResult.validation)
            is ContactSubmitResult.Ready -> {
                val message = ContactMessage(
                    name = form.name.trim(),
                    email = form.email.trim(),
                    topic = topicLabel,
                    subject = draftResult.draft.subject,
                    body = draftResult.draft.body,
                    language = language.code,
                    page = page,
                )
                if (gateway.send(message)) {
                    ContactSendResult.Sent(message.name, message.email)
                } else {
                    ContactSendResult.Failed
                }
            }
        }
    }
}
