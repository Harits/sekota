package id.sekota.feature.contact.presentation

import id.sekota.core.model.Language
import id.sekota.feature.contact.domain.ContactForm
import id.sekota.feature.contact.domain.ContactLabels
import id.sekota.feature.contact.domain.ContactSendResult
import id.sekota.feature.contact.domain.ContactTopicKey
import id.sekota.feature.contact.domain.ContactValidation
import id.sekota.feature.contact.domain.ContactValidator
import id.sekota.feature.contact.domain.SendContactMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ContactUiState(
    val form: ContactForm = ContactForm(),
    val validation: ContactValidation = ContactValidation(),
    val showErrors: Boolean = false,
    val sending: Boolean = false,
    val sent: Boolean = false,
    val failed: Boolean = false,
    val sentName: String = "",
    val sentEmail: String = "",
    val generatedMessage: String? = null,
)

class ContactViewModel(
    private val sendUseCase: SendContactMessage,
) {
    private val _state = MutableStateFlow(ContactUiState())
    val state: StateFlow<ContactUiState> = _state.asStateFlow()

    fun onNameChange(value: String) = updateForm { it.copy(name = value) }

    fun onEmailChange(value: String) = updateForm { it.copy(email = value) }

    fun onMessageChange(value: String) = updateForm { it.copy(message = value) }

    fun onTopicChange(topic: ContactTopicKey) = updateForm { it.copy(topic = topic) }

    /**
     * Applies a generated preset message. A language switch may replace only
     * generated copy; anything the visitor typed is preserved.
     */
    fun prefill(topic: ContactTopicKey, message: String) {
        _state.update { current ->
            val replace = current.form.message.isBlank() || current.form.message == current.generatedMessage
            current.copy(
                form = current.form.copy(
                    topic = topic,
                    message = if (replace) message else current.form.message,
                ),
                generatedMessage = if (replace) message else current.generatedMessage,
                sent = false,
                failed = false,
                sending = false,
            )
        }
    }

    /**
     * Validates and delivers the message. `sent` is only set after the gateway
     * confirms delivery; a failure keeps the form contents and shows the error.
     */
    suspend fun submit(
        topicLabel: String,
        labels: ContactLabels,
        language: Language,
        page: String,
    ) {
        val current = _state.value
        val validation = ContactValidator.validate(current.form)
        if (!validation.isValid) {
            _state.update {
                it.copy(showErrors = true, validation = validation, failed = false)
            }
            return
        }
        _state.update {
            it.copy(
                sending = true,
                showErrors = false,
                validation = ContactValidation(),
                failed = false,
            )
        }
        when (val result = sendUseCase(current.form, topicLabel, labels, language, page)) {
            is ContactSendResult.Sent -> _state.update {
                it.copy(
                    sending = false,
                    sent = true,
                    failed = false,
                    showErrors = false,
                    sentName = result.name,
                    sentEmail = result.email,
                )
            }
            is ContactSendResult.Invalid -> _state.update {
                it.copy(sending = false, showErrors = true, validation = result.validation)
            }
            ContactSendResult.Failed -> _state.update {
                it.copy(sending = false, failed = true)
            }
        }
    }

    fun reset() {
        _state.value = ContactUiState()
    }

    private fun updateForm(transform: (ContactForm) -> ContactForm) {
        _state.update { current ->
            val form = transform(current.form)
            val validation = if (current.showErrors) ContactValidator.validate(form) else current.validation
            current.copy(form = form, validation = validation, failed = false)
        }
    }
}
