package id.sekota

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.dp
import id.sekota.app.AppState
import id.sekota.app.di.appModule
import id.sekota.core.designsystem.SekotaTheme
import id.sekota.core.model.Language
import id.sekota.core.model.ProductSlug
import id.sekota.feature.books.presentation.BooksScreen
import id.sekota.feature.contact.domain.ContactGateway
import id.sekota.feature.contact.domain.ContactMessage
import id.sekota.feature.contact.domain.SendContactMessage
import id.sekota.feature.contact.presentation.ContactScreen
import id.sekota.feature.landing.presentation.section.FaqSection
import id.sekota.feature.legal.domain.LegalDocType
import id.sekota.feature.legal.presentation.LegalScreen
import id.sekota.feature.product.presentation.ProductDetailScreen
import org.koin.compose.KoinApplication
import org.koin.dsl.module
import kotlin.test.Test

private class UiTestContactGateway : ContactGateway {
    override suspend fun send(message: ContactMessage): Boolean = true
}

@OptIn(ExperimentalTestApi::class)
class SekotaUiTest {

    @Test
    fun landingRendersHero() = runComposeUiTest {
        setContent { App() }
        onNodeWithText("Data jadi keputusan.").assertExists()
        onNodeWithText("Keputusan jadi dampak.").assertExists()
        onNodeWithText("Sekota Intelligence Suite").assertExists()
    }

    @Test
    fun languageToggleSwitchesToEnglish() = runComposeUiTest {
        setContent { App() }
        onNodeWithText("Data jadi keputusan.").assertExists()
        onNodeWithText("EN").performClick()
        waitForIdle()
        onNodeWithText("Data into decisions.").assertExists()
        onNodeWithText("Decisions into impact.").assertExists()
    }

    @Test
    fun faqOpensOnClick() = runComposeUiTest {
        setContent {
            SekotaTheme {
                val open = remember { mutableStateOf(-1) }
                FaqSection(
                    content = id.sekota.feature.landing.data.LandingCatalog.content.faq.copy(
                        items = id.sekota.feature.landing.data.LandingCatalog.content.faq.items.take(1),
                    ),
                    language = Language.ID,
                    openFaqId = open.value,
                    onToggle = { open.value = it },
                )
            }
        }
        val answer = "Sekota adalah unit bisnis eBdesk Group."
        onNodeWithText(answer).assertDoesNotExist()
        onNodeWithText("Apa hubungan Sekota dan eBdesk?").performClick()
        waitForIdle()
        onNodeWithText(answer, substring = true).assertExists()
    }

    @Test
    fun contactFormValidatesAndSubmits() = runComposeUiTest {
        val contactTestModule = module {
            single<ContactGateway> { UiTestContactGateway() }
            single { SendContactMessage(get()) }
        }
        setContent {
            KoinApplication(application = {
                allowOverride(true)
                modules(appModule, contactTestModule)
            }) {
                SekotaTheme {
                    ContactScreen(appState = AppState(), initialPreset = null)
                }
            }
        }
        onNodeWithTag("direct-email").assertHasClickAction()
        onNodeWithTag("copy-email").assertHasClickAction()
        onNodeWithText("Kirim Pesan").performClick()
        waitForIdle()
        onNodeWithText("Nama wajib diisi.").assertExists()
        onNodeWithText("Masukkan alamat email yang valid.").assertExists()

        onNodeWithTag("field-name").performTextInput("Rina")
        onNodeWithTag("field-email").performTextInput("rina@sekota.id")
        onNodeWithTag("field-message").performTextInput("Kami ingin menjadwalkan demo Clarivio.")
        onNodeWithText("Kirim Pesan").performClick()
        waitForIdle()
        onNodeWithText("Pesan terkirim.").assertExists()
        onNodeWithText("Terima kasih, Rina. Tim Sekota akan menghubungi Anda di rina@sekota.id.").assertExists()
    }

    @Test
    fun productPageRendersHeadline() = runComposeUiTest {
        setContent {
            KoinApplication(application = { modules(appModule) }) {
                SekotaTheme {
                    ProductDetailScreen(
                        appState = AppState(),
                        slug = ProductSlug.Clarivio,
                        onProduct = {},
                        onContact = {},
                        onHomeSection = {},
                    )
                }
            }
        }
        onNodeWithText("Satu dasbor untuk semua sinyal yang perlu dipantau pimpinan.").assertExists()
    }

    @Test
    fun privacyPageRendersPolicy() = runComposeUiTest {
        setContent {
            KoinApplication(application = { modules(appModule) }) {
                SekotaTheme {
                    Box(Modifier.width(1024.dp)) {
                        LegalScreen(appState = AppState(), type = LegalDocType.Privacy)
                    }
                }
            }
        }
        onNodeWithText("Kebijakan Privasi").assertExists()
        onNodeWithText("Kebijakan ini menjelaskan", substring = true).assertExists()
    }

    @Test
    fun termsPageRendersTerms() = runComposeUiTest {
        setContent {
            KoinApplication(application = { modules(appModule) }) {
                SekotaTheme {
                    Box(Modifier.width(1024.dp)) {
                        LegalScreen(appState = AppState(), type = LegalDocType.Terms)
                    }
                }
            }
        }
        onNodeWithText("Syarat & Ketentuan").assertExists()
        // The table of contents is visible from the start and repeats the heading.
        onAllNodesWithText("Penerimaan syarat").assertCountEquals(2)
    }

    @Test
    fun booksPageRendersHero() = runComposeUiTest {
        setContent {
            KoinApplication(application = { modules(appModule) }) {
                SekotaTheme {
                    Box(Modifier.width(1024.dp)) {
                        BooksScreen(appState = AppState(), initialAnchor = null, onContact = {})
                    }
                }
            }
        }
        onNodeWithText("Cara berpikir kami, dibukukan.").assertExists()
        onNodeWithText("Buku-buku thought leadership yang ditulis Putu Aan J.", substring = true).assertExists()
    }
}
