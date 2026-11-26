package id.sekota.feature.contact

import id.sekota.feature.contact.domain.ContactFieldError
import id.sekota.feature.contact.domain.ContactForm
import id.sekota.feature.contact.domain.ContactLabels
import id.sekota.feature.contact.domain.ContactSubmitResult
import id.sekota.feature.contact.domain.ContactTopicKey
import id.sekota.feature.contact.domain.SubmitContactMessage
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class SubmitContactMessageTest {

    private val submit = SubmitContactMessage()
    private val labels = ContactLabels(
        name = "Nama",
        email = "Email",
        topic = "Topik",
        to = "Kepada",
        subject = "Subjek",
    )

    private fun form() = ContactForm(
        name = "  Rina  ",
        email = " rina@sekota.id ",
        topic = ContactTopicKey.DemoClarivio,
        message = "  Kami ingin demo Clarivio.  ",
    )

    @Test
    fun validFormBuildsDraft() {
        val result = submit(form(), "Demo Clarivio", labels)
        val ready = assertIs<ContactSubmitResult.Ready>(result)
        assertEquals("[Website] Demo Clarivio – Rina", ready.draft.subject)
        assertTrue(ready.draft.body.startsWith("Nama: Rina\nEmail: rina@sekota.id\nTopik: Demo Clarivio\n\n"))
        assertTrue(ready.draft.body.endsWith("Kami ingin demo Clarivio."))
        assertTrue(ready.draft.preview.startsWith("Kepada: info@sekota.id\nSubjek: [Website]"))
    }

    @Test
    fun invalidFormReturnsValidationWithoutDraft() {
        val result = submit(ContactForm(), "Konsultasi umum", labels)
        val invalid = assertIs<ContactSubmitResult.Invalid>(result)
        assertEquals(ContactFieldError.NameRequired, invalid.validation.name)
        assertEquals(ContactFieldError.EmailInvalid, invalid.validation.email)
        assertEquals(ContactFieldError.MessageRequired, invalid.validation.message)
    }

    @Test
    fun labelsFollowLanguage() {
        val english = labels.copy(name = "Name", email = "Email", topic = "Topic", to = "To", subject = "Subject")
        val ready = assertIs<ContactSubmitResult.Ready>(submit(form(), "Clarivio demo", english))
        assertTrue(ready.draft.body.contains("Name: Rina"))
        assertTrue(ready.draft.preview.contains("To: info@sekota.id"))
    }
}
