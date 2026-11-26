package id.sekota.feature.contact

import id.sekota.core.model.Language
import id.sekota.feature.contact.domain.ContactFieldError
import id.sekota.feature.contact.domain.ContactGateway
import id.sekota.feature.contact.domain.ContactLabels
import id.sekota.feature.contact.domain.ContactMessage
import id.sekota.feature.contact.domain.ContactTopicKey
import id.sekota.feature.contact.domain.SendContactMessage
import id.sekota.feature.contact.presentation.ContactViewModel
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

private class FakeGateway(var succeeds: Boolean = true) : ContactGateway {
    val messages = mutableListOf<ContactMessage>()
    override suspend fun send(message: ContactMessage): Boolean {
        messages += message
        return succeeds
    }
}

class ContactViewModelTest {

    private val labels = ContactLabels("Nama", "Email", "Topik", "Kepada", "Subjek")
    private val gateway = FakeGateway()

    private fun viewModel() = ContactViewModel(SendContactMessage(gateway))

    private fun ContactViewModel.fillValid() {
        onNameChange("Rina")
        onEmailChange("rina@sekota.id")
        onMessageChange("Kami ingin menjadwalkan demo Clarivio.")
    }

    private suspend fun ContactViewModel.submitValid(topic: String = "Demo Clarivio") {
        submit(topic, labels, Language.ID, "#/id/contact")
    }

    @Test
    fun submitInvalidFormShowsErrorsWithoutCallingGateway() = runTest {
        val vm = viewModel()
        vm.submit("Konsultasi umum", labels, Language.ID, "#/id/contact")
        val state = vm.state.value
        assertTrue(state.showErrors)
        assertFalse(state.sent)
        assertEquals(ContactFieldError.NameRequired, state.validation.name)
        assertEquals(ContactFieldError.EmailInvalid, state.validation.email)
        assertEquals(ContactFieldError.MessageRequired, state.validation.message)
        assertTrue(gateway.messages.isEmpty())
    }

    @Test
    fun successfulDeliveryMarksSentWithVisitorDetails() = runTest {
        val vm = viewModel()
        vm.fillValid()
        vm.submitValid()
        val state = vm.state.value
        assertTrue(state.sent)
        assertFalse(state.sending)
        assertFalse(state.failed)
        assertFalse(state.showErrors)
        assertEquals("Rina", state.sentName)
        assertEquals("rina@sekota.id", state.sentEmail)

        val message = gateway.messages.single()
        assertEquals("Demo Clarivio", message.topic)
        assertTrue(message.subject.startsWith("[Website] Demo Clarivio"))
        assertTrue(message.body.startsWith("Nama: Rina\nEmail: rina@sekota.id\nTopik: Demo Clarivio\n\n"))
        assertEquals("id", message.language)
        assertEquals("#/id/contact", message.page)
    }

    @Test
    fun failedDeliveryKeepsFormAndShowsError() = runTest {
        gateway.succeeds = false
        val vm = viewModel()
        vm.fillValid()
        vm.submitValid()
        val state = vm.state.value
        assertFalse(state.sent)
        assertTrue(state.failed)
        assertFalse(state.sending)
        assertEquals("Rina", state.form.name)
        assertEquals("rina@sekota.id", state.form.email)

        gateway.succeeds = true
        vm.submitValid()
        assertTrue(vm.state.value.sent)
        assertFalse(vm.state.value.failed)
    }

    @Test
    fun editingAfterSubmitRevalidatesLive() = runTest {
        val vm = viewModel()
        vm.submit("Konsultasi umum", labels, Language.ID, "")
        vm.onNameChange("Rina")
        assertEquals(null, vm.state.value.validation.name)
    }

    @Test
    fun prefillAppliesTopicAndMessageOnce() {
        val vm = viewModel()
        vm.prefill(ContactTopicKey.DemoEvidara, "Pesan awal")
        assertEquals(ContactTopicKey.DemoEvidara, vm.state.value.form.topic)
        assertEquals("Pesan awal", vm.state.value.form.message)

        vm.onMessageChange("Pesan pengguna")
        vm.prefill(ContactTopicKey.Partner, "Pesan lain")
        assertEquals(ContactTopicKey.Partner, vm.state.value.form.topic)
        assertEquals("Pesan pengguna", vm.state.value.form.message)
    }

    @Test
    fun languagePrefillReplacesGeneratedCopyButNotTypedText() {
        val vm = viewModel()
        vm.prefill(ContactTopicKey.DemoClarivio, "Kami ingin demo Clarivio.")
        vm.prefill(ContactTopicKey.DemoClarivio, "We would like a Clarivio demo.")
        assertEquals("We would like a Clarivio demo.", vm.state.value.form.message)

        vm.onMessageChange("Pesan yang saya tulis sendiri")
        vm.prefill(ContactTopicKey.DemoClarivio, "Third generated message")
        assertEquals("Pesan yang saya tulis sendiri", vm.state.value.form.message)
    }

    @Test
    fun resetClearsEverything() = runTest {
        val vm = viewModel()
        vm.fillValid()
        vm.submitValid()
        assertNotNull(vm.state.value.sentName)
        vm.reset()
        assertEquals("", vm.state.value.form.name)
        assertFalse(vm.state.value.sent)
        assertEquals("", vm.state.value.sentEmail)
    }
}
