package id.sekota.feature.contact.domain

/**
 * Pure validation + draft building. The design hands the message to the visitor's
 * email app, so "submitting" is deterministic and fully unit-testable.
 */
object ContactValidator {
    private val emailPattern = Regex("^[^\\s@]+@[^\\s@]+\\.[^\\s@]{2,}$")

    fun validate(form: ContactForm): ContactValidation = ContactValidation(
        name = if (form.name.trim().length > 1) null else ContactFieldError.NameRequired,
        email = if (emailPattern.matches(form.email.trim())) null else ContactFieldError.EmailInvalid,
        message = if (form.message.trim().length > 2) null else ContactFieldError.MessageRequired,
    )
}

class SubmitContactMessage {
    operator fun invoke(
        form: ContactForm,
        topicLabel: String,
        labels: ContactLabels,
    ): ContactSubmitResult {
        val validation = ContactValidator.validate(form)
        if (!validation.isValid) return ContactSubmitResult.Invalid(validation)

        val name = form.name.trim()
        val email = form.email.trim()
        val message = form.message.trim()
        val subject = "[Website] $topicLabel – $name"
        val body = buildString {
            append(labels.name).append(": ").append(name).append('\n')
            append(labels.email).append(": ").append(email).append('\n')
            append(labels.topic).append(": ").append(topicLabel).append("\n\n")
            append(message)
        }
        val preview = buildString {
            append(labels.to).append(": info@sekota.id\n")
            append(labels.subject).append(": ").append(subject).append("\n\n")
            append(body)
        }
        return ContactSubmitResult.Ready(ContactDraft(subject = subject, body = body, preview = preview))
    }
}
