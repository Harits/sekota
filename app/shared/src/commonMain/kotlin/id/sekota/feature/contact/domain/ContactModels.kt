package id.sekota.feature.contact.domain

import id.sekota.core.model.LText

enum class ContactTopicKey {
    General,
    DemoClarivio,
    DemoAscendio,
    DemoSociara,
    DemoEvidara,
    Partner,
    Books,
    Other,
}

data class ContactTopic(val key: ContactTopicKey, val label: LText)

data class ContactContent(
    val overline: LText,
    val titlePrefix: LText,
    val titleGradient: LText,
    val lead: LText,
    val steps: List<ContactStep>,
    val directEmailLabel: LText,
    val email: String,
    val formTitle: LText,
    val requiredNote: LText,
    val nameLabel: LText,
    val emailLabel: LText,
    val topicLabel: LText,
    val messageLabel: LText,
    val namePlaceholder: LText,
    val emailPlaceholder: LText,
    val messagePlaceholder: LText,
    val topics: List<ContactTopic>,
    val submitLabel: LText,
    val sendingLabel: LText,
    val hintBefore: LText,
    val hintLink: LText,
    val hintAfter: LText,
    val successTitle: LText,
    /** Contains `{name}` and `{email}` placeholders. */
    val successText: LText,
    val againLabel: LText,
    val sendError: LText,
    val copyEmailLabel: LText,
    val copiedEmailLabel: LText,
    val draftLabels: ContactLabels,
    val draftLabelsEn: ContactLabels,
    val errors: ContactErrorStrings,
)

data class ContactStep(val number: String, val title: LText, val text: LText)

data class ContactLabels(
    val name: String,
    val email: String,
    val topic: String,
    val to: String,
    val subject: String,
)

data class ContactErrorStrings(
    val nameRequired: LText,
    val emailInvalid: LText,
    val messageRequired: LText,
)

data class ContactForm(
    val name: String = "",
    val email: String = "",
    val topic: ContactTopicKey = ContactTopicKey.General,
    val message: String = "",
)

enum class ContactFieldError { NameRequired, EmailInvalid, MessageRequired }

data class ContactValidation(
    val name: ContactFieldError? = null,
    val email: ContactFieldError? = null,
    val message: ContactFieldError? = null,
) {
    val isValid: Boolean get() = name == null && email == null && message == null
}

data class ContactDraft(
    val subject: String,
    val body: String,
    val preview: String,
)

sealed interface ContactSubmitResult {
    data class Ready(val draft: ContactDraft) : ContactSubmitResult
    data class Invalid(val validation: ContactValidation) : ContactSubmitResult
}

data class ContactPrefill(val topic: ContactTopicKey, val message: LText)

fun ContactContent.topicLabel(key: ContactTopicKey, language: id.sekota.core.model.Language): String =
    topics.first { it.key == key }.label[language]

/** Content + deep-link presets, so presentation never touches the data catalog. */
interface ContactRepository {
    fun content(): ContactContent
    fun prefill(preset: String?): ContactPrefill?
}

class GetContactContent(private val repository: ContactRepository) {
    operator fun invoke(): ContactContent = repository.content()
    fun prefill(preset: String?): ContactPrefill? = repository.prefill(preset)
}
