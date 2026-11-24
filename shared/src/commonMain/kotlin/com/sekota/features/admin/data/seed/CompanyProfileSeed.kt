package com.sekota.features.admin.data.seed

import com.sekota.features.admin.domain.model.AboutConfig
import com.sekota.features.admin.domain.model.AboutPillar
import com.sekota.features.admin.domain.model.AdminBook
import com.sekota.features.admin.domain.model.AdminProduct
import com.sekota.features.admin.domain.model.Faq
import com.sekota.features.admin.domain.model.LocalizedText
import com.sekota.features.admin.domain.model.PartnerCtaConfig
import com.sekota.features.admin.domain.model.ServiceOffering

/**
 * Canonical content for the company profile revamp (CORE-07-01-03 / #22658).
 *
 * Copy is taken verbatim from the approved static design (ID + EN). It seeds an
 * empty store and is the target of the one-time content migration; after that the
 * CMS owns the data.
 */
object CompanyProfileSeed {

    /** Bumped when the canonical content shape changes and stored data must migrate. */
    const val CONTENT_SCHEMA_VERSION = 2

    /** Seed book ids from the previous design that no longer exist in the catalogue. */
    val RETIRED_BOOK_IDS = setOf("csr-berdampak", "esg-strategic-integration")

    /** Product codes from the previous design that were replaced by Clarivio and Evidara. */
    val RETIRED_PRODUCT_CODES = setOf("VRD", "ECO")

    private fun t(id: String, en: String) = LocalizedText(id, en)

    val services: List<ServiceOffering> = listOf(
        ServiceOffering(
            id = "svc-strategic-risk",
            number = "01",
            title = t("Analisis Strategis & Risiko", "Strategic & Risk Intelligence"),
            summary = t("Baca sinyal sebelum jadi krisis.", "Read the signals before they become a crisis."),
            highlights = listOf(
                t("Pemantauan isu", "Issue monitoring"),
                t("Peringatan dini", "Early warning"),
                t("Paparan untuk pimpinan", "Leadership briefings")
            ),
            productCode = "CLV",
            sortOrder = 1
        ),
        ServiceOffering(
            id = "svc-workforce",
            number = "02",
            title = t("Kesiapan & Kompetensi SDM", "Workforce Readiness & Competence"),
            summary = t("Kompetensi tetap hidup setelah pelatihan.", "Competence that lasts beyond training."),
            highlights = listOf(
                t("Asesmen skenario", "Scenario assessments"),
                t("Penguatan berkala", "Periodic reinforcement"),
                t("Dasbor kesiapan", "Readiness dashboard")
            ),
            productCode = "ASC",
            sortOrder = 2
        ),
        ServiceOffering(
            id = "svc-social-impact",
            number = "03",
            title = t("Dampak Sosial & CSR", "Social Impact & CSR"),
            summary = t("Dari kegiatan ke dampak yang terbukti.", "From activities to proven impact."),
            highlights = listOf(
                t("Data dasar sosial", "Social baseline"),
                t("Indikator dampak", "Impact indicators"),
                t("Penerimaan sosial", "Social license")
            ),
            productCode = "SOC",
            sortOrder = 3
        ),
        ServiceOffering(
            id = "svc-esg",
            number = "04",
            title = t("Kinerja ESG", "ESG Performance"),
            summary = t("Dari laporan ke kinerja yang teruji.", "From reports to tested performance."),
            highlights = listOf(
                t("Kesiapan & celah", "Readiness & gaps"),
                t("Materialitas", "Materiality"),
                t("Peta jalan ESG", "ESG roadmap")
            ),
            productCode = "EVD",
            sortOrder = 4
        )
    )

    private fun product(
        id: String,
        code: String,
        name: String,
        slug: String,
        serviceId: String,
        service: LocalizedText,
        tagline: LocalizedText,
        flow: LocalizedText,
        platformSummary: LocalizedText,
        accent: String,
        order: Int
    ) = AdminProduct(
        id = id,
        code = code,
        name = name,
        categoryEyebrow = service.id,
        description = tagline.id,
        features = emptyList(),
        accentColorHex = accent,
        categoryEyebrowEn = service.en,
        descriptionEn = tagline.en,
        slug = slug,
        serviceId = serviceId,
        tagline = tagline,
        flowLabel = flow,
        platformSummary = platformSummary,
        logoLightAsset = "${slug}_light",
        logoDarkAsset = "${slug}_dark",
        dashboardAsset = "dash_$slug",
        sortOrder = order
    )

    val products: List<AdminProduct> = listOf(
        product(
            id = "p-clarivio", code = "CLV", name = "Clarivio", slug = "clarivio", serviceId = "svc-strategic-risk",
            service = t("Analisis Strategis & Risiko", "Strategic & Risk Intelligence"),
            tagline = t("Analisis ancaman dan peluang berbasis AI", "AI-driven threat and opportunity intelligence"),
            flow = t("Sinyal → keputusan", "Signal → decision"),
            platformSummary = t("Analisis ancaman dan peluang untuk pimpinan.", "Threat and opportunity intelligence for leadership."),
            accent = "#02B6CF", order = 1
        ),
        product(
            id = "p2", code = "ASC", name = "Ascendio", slug = "ascendio", serviceId = "svc-workforce",
            service = t("Kesiapan & Kompetensi SDM", "Workforce Readiness & Competence"),
            tagline = t("Menjaga kompetensi tetap hidup setelah pelatihan", "Keeping competence alive after training"),
            flow = t("Tahu → melakukan", "Knowing → doing"),
            platformSummary = t("Kesiapan dan kompetensi tim setelah pelatihan.", "Team readiness and competence after training."),
            accent = "#378ADD", order = 2
        ),
        product(
            id = "p3", code = "SOC", name = "Sociara", slug = "sociara", serviceId = "svc-social-impact",
            service = t("Dampak Sosial & CSR", "Social Impact & CSR"),
            tagline = t("CSR dan pemberdayaan masyarakat hingga dampak terbukti", "CSR and community development with proven impact"),
            flow = t("Program → dampak", "Program → impact"),
            platformSummary = t("Manajemen CSR hingga dampak terbukti.", "CSR management through to proven impact."),
            accent = "#187D61", order = 3
        ),
        product(
            id = "p-evidara", code = "EVD", name = "Evidara", slug = "evidara", serviceId = "svc-esg",
            service = t("Kinerja ESG", "ESG Performance"),
            tagline = t("Kesiapan ESG: skor, celah, dan peta jalan", "ESG readiness: score, gaps, and roadmap"),
            flow = t("Laporan → kinerja", "Report → performance"),
            platformSummary = t("Kesiapan dan kinerja ESG berbasis AI.", "AI-driven ESG readiness and performance."),
            accent = "#60BD65", order = 4
        )
    )

    const val TRILOGY_SERIES_ID = "trilogi-csr-esg-kepemimpinan"
    private const val AUTHOR = "Putu Aan J."

    private fun book(
        id: String,
        title: String,
        flow: LocalizedText,
        tagline: LocalizedText,
        cover: String,
        order: Int,
        isbn: String = "",
        series: String? = null
    ) = AdminBook(
        id = id,
        title = title,
        author = AUTHOR,
        isbn = isbn,
        category = "WAWASAN",
        description = tagline.id,
        descriptionEn = tagline.en,
        slug = id,
        tagline = tagline,
        flowLabel = flow,
        seriesId = series,
        coverAsset = cover,
        sortOrder = order
    )

    val books: List<AdminBook> = listOf(
        book("forgetting-enterprise", "The Forgetting Enterprise",
            t("tahu → melakukan", "knowing → doing"),
            t("Mengapa yang sudah dilatih tidak dijalankan?", "Why isn't what we trained actually practiced?"),
            cover = "cover1", order = 1),
        book("blind-spot-radar", "Blind Spot Radar",
            t("sinyal → keputusan", "signal → decision"),
            t("Apa yang sedang kita lewatkan?", "What are we missing right now?"),
            cover = "cover2", order = 2, isbn = "978-623-99999-3-1"),
        book("manifesto-ekuitas-lahan", "Manifesto Ekuitas Lahan",
            t("kepemilikan → kepercayaan", "ownership → trust"),
            t("Mengapa lahan kita tidur?", "Why is our land lying idle?"),
            cover = "cover3", order = 3, isbn = "978-623-99999-0-0"),
        book("impact-driven-csr", "Impact-Driven CSR",
            t("program → dampak", "program → impact"),
            t("Dari kepatuhan ke dampak nyata bagi komunitas.", "From compliance to real impact for communities."),
            cover = "cover4", order = 4, series = TRILOGY_SERIES_ID),
        book("accountable-esg", "Accountable ESG",
            t("laporan → kinerja", "report → performance"),
            t("Dari laporan keberlanjutan ke kinerja yang teruji.", "From sustainability reports to tested performance."),
            cover = "cover5", order = 5, series = TRILOGY_SERIES_ID),
        book("sustainability-leadership", "Sustainability Leadership",
            t("perusahaan → sistem", "company → system"),
            t("Dari program perusahaan ke transformasi sistemik.", "From company programs to systemic transformation."),
            cover = "cover6", order = 6, series = TRILOGY_SERIES_ID)
    )

    val about = AboutConfig(
        eyebrow = t("Tentang Sekota", "About Sekota"),
        title = t("Mitra analisis yang tumbuh bersama Anda.", "An intelligence partner that grows with you."),
        description = t(
            "Unit bisnis eBdesk Group yang membantu organisasi di Indonesia memakai data untuk mengambil keputusan.",
            "A business unit of eBdesk Group that helps organizations in Indonesia use data to make decisions."
        ),
        pillars = listOf(
            AboutPillar(
                t("Gagasan yang dibukukan", "Ideas in print"),
                t(
                    "6 buku thought leadership berbahasa Indonesia menjadi landasan cara kami berpikir dan bekerja.",
                    "Six Indonesian-language thought leadership books shape how we think and work."
                ),
                iconName = "book"
            ),
            AboutPillar(
                t("Teknologi eBdesk", "eBdesk technology"),
                t(
                    "Big data dan AI di atas infrastruktur eBdesk bersertifikasi ISO 27001.",
                    "Big data and AI running on eBdesk's ISO 27001-certified infrastructure."
                ),
                iconName = "shield"
            ),
            AboutPillar(
                t("Tumbuh bersama", "Growing together"),
                t(
                    "Target disepakati bersama, tim Anda ikut mandiri.",
                    "Targets are agreed together, and your team becomes self-reliant."
                ),
                iconName = "growth"
            )
        ),
        sectors = listOf(
            t("Energi, Migas & Pertambangan", "Energy, Oil & Gas, and Mining"),
            t("Keamanan & Jasa Pengamanan", "Security Services"),
            t("BUMN & Grup Korporasi", "State-Owned Enterprises & Corporate Groups"),
            t("Pemerintah Daerah & K/L", "Local & Central Government"),
            t("Perkebunan & Agribisnis", "Plantations & Agribusiness"),
            t("Perbankan & Keuangan", "Banking & Finance")
        ),
        additionalOfferings = listOf(
            t("Solusi analisis kustom", "Custom intelligence solutions"),
            t("Pengembangan kapabilitas tim", "Team capability development")
        )
    )

    val partnerCta = PartnerCtaConfig(
        title = t("Mari tumbuh dan berkembang bersama.", "Let's grow together."),
        description = t(
            "Program Mitra Awal: bangun solusi analisis bersama tim inti Sekota, dengan target yang kita sepakati bersama.",
            "Early Partner Program: build intelligence solutions with Sekota's core team, against targets we agree on together."
        ),
        ctaLabel = t("Hubungi Kami", "Contact Us"),
        ctaRoute = "/kontak#mitra-awal"
    )

    val faqs: List<Faq> = listOf(
        Faq(
            "faq-ebdesk",
            t("Apa hubungan Sekota dan eBdesk?", "How are Sekota and eBdesk related?"),
            t(
                "Sekota adalah unit bisnis eBdesk Group. eBdesk membangun teknologinya; Sekota mendampingi organisasi memakainya, dari kerangka berpikir sampai solusi dipakai.",
                "Sekota is a business unit of eBdesk Group. eBdesk builds the technology; Sekota helps organizations use it, from the thinking framework to solutions in daily use."
            ),
            sortOrder = 1
        ),
        Faq(
            "faq-which-service",
            t("Layanan mana yang tepat untuk organisasi kami?", "Which service is right for our organization?"),
            t(
                "Mulai dari tantangan utamanya. Untuk membaca risiko dan isu, pilih Analisis Strategis & Risiko. Untuk menjaga kompetensi tim setelah pelatihan, pilih Kesiapan & Kompetensi SDM. Untuk membuktikan dampak program CSR, pilih Dampak Sosial & CSR. Untuk kesiapan dan kinerja ESG, pilih Kinerja ESG. Kalau belum yakin, tahap Diagnosis membantu memetakan kebutuhan sebelum memilih.",
                "Start from your main challenge. To read risks and issues, choose Strategic & Risk Intelligence. To keep your team competent after training, choose Workforce Readiness & Competence. To prove the impact of CSR programs, choose Social Impact & CSR. For ESG readiness and performance, choose ESG Performance. Not sure yet? The Diagnose stage maps your needs before you choose."
            ),
            sortOrder = 2
        ),
        Faq(
            "faq-subscription",
            t("Apakah harus berlangganan platform untuk memakai layanan Sekota?", "Do we need a platform subscription to use Sekota's services?"),
            t(
                "Tidak harus. Kerja sama bisa berbentuk proyek pendampingan, layanan analisis terkelola, langganan platform, atau program penguatan kapabilitas. Platform dipakai bila memang membantu mencapai target yang disepakati bersama.",
                "No. We can work together through advisory projects, managed intelligence, platform subscriptions, or capability programs. A platform is used only when it helps reach the targets we agree on."
            ),
            sortOrder = 3
        ),
        Faq(
            "faq-data",
            t("Bagaimana data organisasi kami dijaga?", "How is our organization's data protected?"),
            t(
                "Layanan Sekota berjalan di atas infrastruktur eBdesk yang tersertifikasi ISO 27001. Akses data dibatasi sesuai kebutuhan pekerjaan, dan aturan penggunaan data disepakati di awal kerja sama.",
                "Sekota's services run on eBdesk's ISO 27001-certified infrastructure. Data access is limited to what the work requires, and data-use rules are agreed at the start of the engagement."
            ),
            sortOrder = 4
        ),
        Faq(
            "faq-tailoring",
            t("Apakah layanan dan platform bisa disesuaikan dengan proses kami?", "Can the services and platforms be tailored to our processes?"),
            t(
                "Bisa. Solusi dirancang bersama pada tahap Perancangan, dan platform dapat dikonfigurasi sesuai data serta alur kerja organisasi Anda, atau dikembangkan sebagai solusi analisis kustom.",
                "Yes. Solutions are designed together in the Design stage, and platforms can be configured to your data and workflows, or built as a custom intelligence solution."
            ),
            sortOrder = 5
        ),
        Faq(
            "faq-duration",
            t("Berapa lama sebuah proyek biasanya berjalan?", "How long does a project usually take?"),
            t(
                "Tergantung ruang lingkupnya. Proyek pendampingan umumnya berjangka tertentu, sedangkan layanan analisis terkelola dan langganan platform berjalan berkelanjutan dengan evaluasi berkala. Perkiraan durasi kami sampaikan setelah tahap Diagnosis.",
                "It depends on the scope. Advisory projects usually run for a set period, while managed intelligence and platform subscriptions run continuously with regular reviews. We share an estimated timeline after the Diagnose stage."
            ),
            sortOrder = 6
        ),
        Faq(
            "faq-start",
            t("Bagaimana cara memulai kerja sama?", "How do we get started?"),
            t(
                "Hubungi kami lewat info@sekota.id atau tombol Hubungi Kami. Kami mulai dengan diskusi singkat dan tahap Diagnosis untuk memetakan kebutuhan, lalu menyusun rencana dan target bersama.",
                "Reach us at info@sekota.id or through the Contact Us page. We start with a short discussion and the Diagnose stage to map your needs, then set the plan and targets together."
            ),
            sortOrder = 7
        )
    )
}
