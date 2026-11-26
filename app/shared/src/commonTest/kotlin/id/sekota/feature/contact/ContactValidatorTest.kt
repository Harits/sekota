package id.sekota.feature.contact

import id.sekota.feature.contact.domain.ContactFieldError
import id.sekota.feature.contact.domain.ContactForm
import id.sekota.feature.contact.domain.ContactValidator
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ContactValidatorTest {

    private fun validForm() = ContactForm(
        name = "Rina",
        email = "rina@sekota.id",
        message = "Kami ingin menjadwalkan demo.",
    )

    @Test
    fun validFormPasses() {
        assertTrue(ContactValidator.validate(validForm()).isValid)
    }

    @Test
    fun singleCharacterNameIsRejected() {
        val result = ContactValidator.validate(validForm().copy(name = "R"))
        assertEquals(ContactFieldError.NameRequired, result.name)
    }

    @Test
    fun whitespaceOnlyNameIsRejected() {
        val result = ContactValidator.validate(validForm().copy(name = "   "))
        assertFalse(result.isValid)
        assertEquals(ContactFieldError.NameRequired, result.name)
    }

    @Test
    fun badEmailsAreRejected() {
        listOf("", "no-at-sign", "a@b", "a@b.c", "a b@c.co").forEach { email ->
            val result = ContactValidator.validate(validForm().copy(email = email))
            assertEquals(ContactFieldError.EmailInvalid, result.email, "email=$email")
        }
    }

    @Test
    fun goodEmailsAreAccepted() {
        listOf("a@b.co", "name.surname@company.co.id", "ops+team@sub.domain.io").forEach { email ->
            val result = ContactValidator.validate(validForm().copy(email = email))
            assertEquals(null, result.email, "email=$email")
        }
    }

    @Test
    fun shortMessageIsRejected() {
        val result = ContactValidator.validate(validForm().copy(message = "hi"))
        assertEquals(ContactFieldError.MessageRequired, result.message)
    }

    @Test
    fun multipleErrorsAreReportedTogether() {
        val result = ContactValidator.validate(ContactForm())
        assertFalse(result.isValid)
        assertEquals(ContactFieldError.NameRequired, result.name)
        assertEquals(ContactFieldError.EmailInvalid, result.email)
        assertEquals(ContactFieldError.MessageRequired, result.message)
    }
}
