package id.sekota.feature.contact.data

import id.sekota.core.model.BookSlug
import id.sekota.core.model.LText
import id.sekota.core.model.ltext
import id.sekota.feature.contact.domain.ContactContent
import id.sekota.feature.contact.domain.ContactErrorStrings
import id.sekota.feature.contact.domain.ContactLabels
import id.sekota.feature.contact.domain.ContactPrefill
import id.sekota.feature.contact.domain.ContactRepository
import id.sekota.feature.contact.domain.ContactStep
import id.sekota.feature.contact.domain.ContactTopic
import id.sekota.feature.contact.domain.ContactTopicKey

class StaticContactRepository : ContactRepository {
    override fun content(): ContactContent = ContactCatalog.content
    override fun prefill(preset: String?): ContactPrefill? = ContactCatalog.prefill(preset)
}

object ContactCatalog {

    private val topics = listOf(
        ContactTopic(ContactTopicKey.General, ltext("Konsultasi umum", "General consultation")),
        ContactTopic(
            ContactTopicKey.DemoClarivio,
            ltext("Demo Clarivio – Analisis Strategis & Risiko", "Demo Clarivio – Strategic & Risk Intelligence"),
        ),
        ContactTopic(
            ContactTopicKey.DemoAscendio,
            ltext("Demo Ascendio – Kesiapan & Kompetensi SDM", "Demo Ascendio – Workforce Readiness & Competence"),
        ),
        ContactTopic(
            ContactTopicKey.DemoSociara,
            ltext("Demo Sociara – Dampak Sosial & CSR", "Demo Sociara – Social Impact & CSR"),
        ),
        ContactTopic(
            ContactTopicKey.DemoEvidara,
            ltext("Demo Evidara – Kinerja ESG", "Demo Evidara – ESG Performance"),
        ),
        ContactTopic(ContactTopicKey.Partner, ltext("Program Mitra Awal", "Early Partner Program")),
        ContactTopic(ContactTopicKey.Books, ltext("Wawasan & buku Sekota", "Insights & Sekota books")),
        ContactTopic(ContactTopicKey.Other, ltext("Lainnya", "Other")),
    )

    val content = ContactContent(
        overline = ltext("Hubungi Kami", "Contact Us"),
        titlePrefix = ltext("Mari diskusikan kebutuhan ", "Let's talk about "),
        titleGradient = ltext("organisasi Anda.", "your organization's needs."),
        lead = ltext(
            "Ceritakan tantangan yang sedang Anda hadapi. Tim Sekota akan menghubungi Anda untuk menjadwalkan diskusi atau demo platform.",
            "Tell us about the challenge you're facing. The Sekota team will get back to you to schedule a discussion or a platform demo.",
        ),
        steps = listOf(
            ContactStep(
                "1",
                ltext("Kirim pesan", "Send a message"),
                ltext(
                    "Isi email dan pesan singkat melalui formulir ini.",
                    "Fill in your email and a short message using this form.",
                ),
            ),
            ContactStep(
                "2",
                ltext("Kami hubungi Anda", "We get in touch"),
                ltext(
                    "Tim kami membalas melalui email untuk mengatur waktu diskusi.",
                    "Our team replies by email to set up a time to talk.",
                ),
            ),
            ContactStep(
                "3",
                ltext("Diskusi & diagnosis awal", "Discussion & initial diagnosis"),
                ltext(
                    "Kami petakan kebutuhan Anda dan menyarankan layanan yang paling tepat.",
                    "We map your needs and recommend the most suitable service.",
                ),
            ),
        ),
        directEmailLabel = ltext("Atau email langsung ke", "Or email us directly at"),
        email = "info@sekota.id",
        formTitle = ltext("Kirim pesan", "Send a message"),
        requiredNote = ltext("Kolom bertanda * wajib diisi.", "Fields marked * are required."),
        nameLabel = ltext("Nama lengkap", "Full name"),
        emailLabel = ltext("Email", "Email"),
        topicLabel = ltext("Topik", "Topic"),
        messageLabel = ltext("Pesan", "Message"),
        namePlaceholder = ltext("Nama Anda", "Your name"),
        emailPlaceholder = ltext("nama@perusahaan.co.id", "name@company.co.id"),
        messagePlaceholder = ltext(
            "Ceritakan singkat kebutuhan atau pertanyaan Anda.",
            "Briefly describe your needs or question.",
        ),
        topics = topics,
        submitLabel = ltext("Kirim Pesan", "Send Message"),
        sendingLabel = ltext("Mengirim…", "Sending…"),
        hintBefore = ltext(
            "Dengan mengirim pesan, Anda setuju dihubungi kembali oleh tim Sekota melalui email sesuai ",
            "By sending this message, you agree to be contacted by the Sekota team by email in accordance with our ",
        ),
        hintLink = ltext("Kebijakan Privasi", "Privacy Policy"),
        hintAfter = ltext(" kami.", "."),
        successTitle = ltext("Pesan terkirim.", "Message sent."),
        successText = ltext(
            "Terima kasih, {name}. Tim Sekota akan menghubungi Anda di {email}.",
            "Thank you, {name}. The Sekota team will get back to you at {email}.",
        ),
        againLabel = ltext("Kirim pesan lain", "Send another message"),
        sendError = ltext(
            "Pesan belum terkirim. Coba lagi, atau email langsung ke info@sekota.id.",
            "The message was not sent. Try again, or email us directly at info@sekota.id.",
        ),
        copyEmailLabel = ltext("Salin alamat email", "Copy email address"),
        copiedEmailLabel = ltext("Tersalin", "Copied"),
        draftLabels = ContactLabels(
            name = "Nama",
            email = "Email",
            topic = "Topik",
            to = "Kepada",
            subject = "Subjek",
        ),
        draftLabelsEn = ContactLabels(
            name = "Name",
            email = "Email",
            topic = "Topic",
            to = "To",
            subject = "Subject",
        ),
        errors = ContactErrorStrings(
            nameRequired = ltext("Nama wajib diisi.", "Name is required."),
            emailInvalid = ltext("Masukkan alamat email yang valid.", "Enter a valid email address."),
            messageRequired = ltext("Pesan wajib diisi.", "Message is required."),
        ),
    )

    /** Resolves deep-link presets such as demo-clarivio, mitra-awal or buku-blind-spot-radar. */
    fun prefill(preset: String?): ContactPrefill? {
        if (preset.isNullOrBlank()) return null
        val demo = when (preset) {
            "demo-clarivio" -> ContactPrefill(
                ContactTopicKey.DemoClarivio,
                ltext(
                    "Kami ingin menjadwalkan demo Clarivio untuk organisasi kami.",
                    "We would like to schedule a Clarivio demo for our organization.",
                ),
            )
            "demo-ascendio" -> ContactPrefill(
                ContactTopicKey.DemoAscendio,
                ltext(
                    "Kami ingin menjadwalkan demo Ascendio untuk organisasi kami.",
                    "We would like to schedule an Ascendio demo for our organization.",
                ),
            )
            "demo-sociara" -> ContactPrefill(
                ContactTopicKey.DemoSociara,
                ltext(
                    "Kami ingin menjadwalkan demo Sociara untuk organisasi kami.",
                    "We would like to schedule a Sociara demo for our organization.",
                ),
            )
            "demo-evidara" -> ContactPrefill(
                ContactTopicKey.DemoEvidara,
                ltext(
                    "Kami ingin menjadwalkan demo Evidara untuk organisasi kami.",
                    "We would like to schedule an Evidara demo for our organization.",
                ),
            )
            "mitra-awal" -> ContactPrefill(
                ContactTopicKey.Partner,
                ltext(
                    "Kami tertarik mengikuti Program Mitra Awal dan ingin berdiskusi lebih lanjut.",
                    "We are interested in the Early Partner Program and would like to discuss further.",
                ),
            )
            else -> null
        }
        if (demo != null) return demo

        if (preset.startsWith("buku-")) {
            val slug = BookSlug.fromAnchor(preset.removePrefix("buku-")) ?: return null
            val title = bookTitle(slug)
            return ContactPrefill(
                ContactTopicKey.Books,
                ltext(
                    "Kami tertarik mendiskusikan gagasan buku $title dan relevansinya untuk organisasi kami.",
                    "We would like to discuss the ideas in $title and their relevance to our organization.",
                ),
            )
        }
        return null
    }

    private fun bookTitle(slug: BookSlug): String = when (slug) {
        BookSlug.ForgettingEnterprise -> "The Forgetting Enterprise"
        BookSlug.BlindSpotRadar -> "Blind Spot Radar"
        BookSlug.ManifestoEkuitasLahan -> "Manifesto Ekuitas Lahan"
        BookSlug.ImpactDrivenCsr -> "Impact-Driven CSR"
        BookSlug.AccountableEsg -> "Accountable ESG"
        BookSlug.SustainabilityLeadership -> "Sustainability Leadership"
    }
}
