package com.sekota.localization

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Unit Tests for LocalizationManager & AppLocale.
 * WP-048 (#22553) - Verification of key lookups, fallbacks, and state transitions.
 */
class LocalizationManagerTest {

    @BeforeTest
    fun setUp() {
        LocalizationManager.setLocale(AppLocale.ID)
    }

    @Test
    fun testInitialLocaleIsIndonesian() {
        assertEquals(AppLocale.ID, LocalizationManager.currentLocale.value)
    }

    @Test
    fun testLocaleTransitions() {
        LocalizationManager.setLocale(AppLocale.EN)
        assertEquals(AppLocale.EN, LocalizationManager.currentLocale.value)

        LocalizationManager.setLocale("BAHASA INDONESIA")
        assertEquals(AppLocale.ID, LocalizationManager.currentLocale.value)

        LocalizationManager.setLocale("en")
        assertEquals(AppLocale.EN, LocalizationManager.currentLocale.value)

        LocalizationManager.setLocale("id")
        assertEquals(AppLocale.ID, LocalizationManager.currentLocale.value)
    }

    @Test
    fun testKeyLookupInIndonesian() {
        LocalizationManager.setLocale(AppLocale.ID)

        assertEquals("Beranda", LocalizationManager.getString("nav_home"))
        assertEquals("Solusi", LocalizationManager.getString("nav_solusi"))
        assertEquals("Produk", LocalizationManager.getString("nav_produk"))
        assertEquals("E-Book", LocalizationManager.getString("nav_ebook"))
        assertEquals("Kontak", LocalizationManager.getString("nav_kontak"))
        assertEquals("Konsultasi Strategis", LocalizationManager.getString("nav_cta_consultation"))

        assertEquals("TRUSTED INTELLIGENCE. MEASURED IMPACT.", LocalizationManager.getString("hero_eyebrow"))
        assertEquals("Ekosistem Produk", LocalizationManager.getString("hero_title_part1"))
        assertEquals("Intelligence Suite", LocalizationManager.getString("hero_title_part2"))
        assertEquals("Eksplorasi Produk", LocalizationManager.getString("hero_cta_explore"))
        assertEquals("Hubungi Tim Ahli", LocalizationManager.getString("hero_cta_contact"))

        assertEquals("TANTANGAN 2026", LocalizationManager.getString("features_eyebrow"))
        assertEquals("Data Tidak Terintegrasi", LocalizationManager.getString("feature_1_title"))

        assertEquals("Baca Sekarang", LocalizationManager.getString("catalog_read_now"))
        assertEquals("Kembali ke Katalog", LocalizationManager.getString("reader_back_to_catalog"))
        assertEquals("Daftar Bab", LocalizationManager.getString("reader_chapters"))
        assertEquals("Mode Malam", LocalizationManager.getString("reader_night_mode"))
    }

    @Test
    fun testKeyLookupInEnglish() {
        LocalizationManager.setLocale(AppLocale.EN)

        assertEquals("Home", LocalizationManager.getString("nav_home"))
        assertEquals("Solutions", LocalizationManager.getString("nav_solusi"))
        assertEquals("Products", LocalizationManager.getString("nav_produk"))
        assertEquals("E-Book", LocalizationManager.getString("nav_ebook"))
        assertEquals("Contact", LocalizationManager.getString("nav_kontak"))
        assertEquals("Strategic Consultation", LocalizationManager.getString("nav_cta_consultation"))

        assertEquals("TRUSTED INTELLIGENCE. MEASURED IMPACT.", LocalizationManager.getString("hero_eyebrow"))
        assertEquals("Product Ecosystem", LocalizationManager.getString("hero_title_part1"))
        assertEquals("Intelligence Suite", LocalizationManager.getString("hero_title_part2"))
        assertEquals("Explore Products", LocalizationManager.getString("hero_cta_explore"))
        assertEquals("Contact Expert Team", LocalizationManager.getString("hero_cta_contact"))

        assertEquals("2026 CHALLENGES", LocalizationManager.getString("features_eyebrow"))
        assertEquals("Unintegrated Data", LocalizationManager.getString("feature_1_title"))

        assertEquals("Read Now", LocalizationManager.getString("catalog_read_now"))
        assertEquals("Back to Catalog", LocalizationManager.getString("reader_back_to_catalog"))
        assertEquals("Chapters", LocalizationManager.getString("reader_chapters"))
        assertEquals("Night Mode", LocalizationManager.getString("reader_night_mode"))
    }

    @Test
    fun testFallbackToIndonesianWhenKeyMissingInEnglish() {
        LocalizationManager.setLocale(AppLocale.EN)
        // Even if a key were only defined in IndonesianStrings, it falls back to ID
        val value = LocalizationManager.getString("hero_title_part3")
        assertEquals("Sekota Sinergi Indonesia", value)
    }

    @Test
    fun testFallbackToKeyWhenKeyMissingInBoth() {
        LocalizationManager.setLocale(AppLocale.EN)
        val missingKey = "totally_unknown_key_12345"
        assertEquals(missingKey, LocalizationManager.getString(missingKey))

        LocalizationManager.setLocale(AppLocale.ID)
        assertEquals(missingKey, LocalizationManager.getString(missingKey))
    }

    @Test
    fun testAppLocaleParsing() {
        assertEquals(AppLocale.ID, AppLocale.fromCode("id"))
        assertEquals(AppLocale.EN, AppLocale.fromCode("en"))
        assertEquals(AppLocale.ID, AppLocale.fromDisplayName("BAHASA INDONESIA"))
        assertEquals(AppLocale.EN, AppLocale.fromDisplayName("ENGLISH"))
        assertEquals(AppLocale.ID, AppLocale.fromDisplayName("unknown_locale"))
    }

    @Test
    fun testPlaceholderSubstitution() {
        val result = LocalizationManager.getString("catalog_read_now")
        assertNotNull(result)

        // Custom template substitution test
        val customKey = "welcome_user"
        // Missing key returns the key with arguments formatted
        val substituted = LocalizationManager.getString("Hello {0}, you have {1} books", "Harits", 5)
        assertEquals("Hello Harits, you have 5 books", substituted)
    }

    @Test
    fun testIntelligenceSuiteAndProductLocalization() {
        // Test in ID
        LocalizationManager.setLocale(AppLocale.ID)
        assertEquals("EKOSISTEM PRODUK", LocalizationManager.getString("suite_eyebrow"))
        assertEquals("Intelligence Suite.", LocalizationManager.getString("suite_title"))
        assertEquals("Dirancang untuk saling terhubung dalam satu ekosistem data yang kohesif.", LocalizationManager.getString("suite_subtitle"))
        assertEquals("Pelajari Selengkapnya", LocalizationManager.getString("suite_learn_more"))

        val dummyVrd = com.sekota.features.admin.domain.model.AdminProduct(
            id = "p1",
            code = "VRD",
            name = "Veridia",
            categoryEyebrow = "ESG & Carbon Intelligence",
            description = "Desc in ID",
            features = listOf("F1", "F2", "F3", "F4")
        )
        val localizedVrdId = LocalizationManager.getLocalizedProduct(dummyVrd)
        assertEquals("ESG & Carbon Intelligence", localizedVrdId.categoryEyebrow)
        assertEquals(
            "Platform analitik komprehensif untuk pemantauan jejak karbon Scope 1, 2, dan 3, kepatuhan regulasi lingkungan, dan pelaporan ESG otomatis sesuai standar global.",
            localizedVrdId.description
        )
        assertEquals(4, localizedVrdId.features.size)
        assertEquals("Audit Jejak Karbon Scope 1, 2, & 3 Real-time", localizedVrdId.features[0])

        // Test in EN
        LocalizationManager.setLocale(AppLocale.EN)
        assertEquals("PRODUCT ECOSYSTEM", LocalizationManager.getString("suite_eyebrow"))
        assertEquals("Intelligence Suite.", LocalizationManager.getString("suite_title"))
        assertEquals("Designed to interconnect within a cohesive data ecosystem.", LocalizationManager.getString("suite_subtitle"))
        assertEquals("Learn More", LocalizationManager.getString("suite_learn_more"))

        val localizedVrdEn = LocalizationManager.getLocalizedProduct(dummyVrd)
        assertEquals("ESG & Carbon Intelligence", localizedVrdEn.categoryEyebrow)
        assertEquals(
            "Comprehensive analytics platform for Scope 1, 2, and 3 carbon footprint tracking, environmental compliance, and automated ESG reporting aligned with global standards.",
            localizedVrdEn.description
        )
        assertEquals(4, localizedVrdEn.features.size)
        assertEquals("Real-time Scope 1, 2, & 3 Carbon Footprint Audit", localizedVrdEn.features[0])
        assertEquals("Automated Sustainability Reporting (GRI, ISSB, IDX-ESG)", localizedVrdEn.features[1])
        assertEquals("Predictive Decarbonization Scenario Modeling", localizedVrdEn.features[2])
        assertEquals("Stakeholder Governance Dashboard", localizedVrdEn.features[3])

        // Test Ascendio, Sociara, Ecoflow in EN
        val dummyAsc = com.sekota.features.admin.domain.model.AdminProduct(
            id = "p2", code = "ASC", name = "Ascendio", categoryEyebrow = "", description = "", features = emptyList()
        )
        val localizedAscEn = LocalizationManager.getLocalizedProduct(dummyAsc)
        assertEquals("Strategic Corporate Intelligence", localizedAscEn.categoryEyebrow)
        assertEquals(
            "Strategic business intelligence system for competitive market mapping, public policy sentiment analysis, and AI-powered reputation risk mitigation.",
            localizedAscEn.description
        )
        assertEquals("24/7 Media Sentiment & Public Policy Monitoring", localizedAscEn.features[0])

        val dummySoc = com.sekota.features.admin.domain.model.AdminProduct(
            id = "p3", code = "SOC", name = "Sociara", categoryEyebrow = "", description = "", features = emptyList()
        )
        val localizedSocEn = LocalizationManager.getLocalizedProduct(dummySoc)
        assertEquals("Social Impact & Community Engagement", localizedSocEn.categoryEyebrow)
        assertEquals(
            "Social impact management and evaluation platform (Social Return on Investment / SROI), industrial community relations, and sustainable corporate CSR programs.",
            localizedSocEn.description
        )
        assertEquals("SROI Measurement & CSR Impact Effectiveness Evaluation", localizedSocEn.features[0])

        val dummyEco = com.sekota.features.admin.domain.model.AdminProduct(
            id = "p4", code = "ECO", name = "Ecoflow", categoryEyebrow = "", description = "", features = emptyList()
        )
        val localizedEcoEn = LocalizationManager.getLocalizedProduct(dummyEco)
        assertEquals("Resource & Ecological Intelligence", localizedEcoEn.categoryEyebrow)
        assertEquals(
            "Resource flow monitoring solution for energy, water, and circular waste to drive industrial operational efficiency toward Net Zero targets.",
            localizedEcoEn.description
        )
        assertEquals("Energy Consumption Monitoring & Operational Efficiency", localizedEcoEn.features[0])
    }

    @Test
    fun testValueLoopSectionLocalization() {
        // Indonesian
        LocalizationManager.setLocale(AppLocale.ID)
        assertEquals("SOLUSI STRATEGIS", LocalizationManager.getString("valueloop_eyebrow"))
        assertEquals("Siklus Nilai Berkelanjutan", LocalizationManager.getString("valueloop_title"))
        assertEquals("Mengubah kompleksitas data menjadi dampak nyata terukur bagi organisasi dan masyarakat.", LocalizationManager.getString("valueloop_desc"))
        assertEquals("Insight", LocalizationManager.getString("valueloop_step_1_title"))
        assertEquals("Agregasi data multi-dimensi", LocalizationManager.getString("valueloop_step_1_subtitle"))
        assertEquals("Integrasi", LocalizationManager.getString("valueloop_step_2_title"))
        assertEquals("Unifikasi silo informasi", LocalizationManager.getString("valueloop_step_2_subtitle"))
        assertEquals("Analisis", LocalizationManager.getString("valueloop_step_3_title"))
        assertEquals("Pemodelan prediktif AI", LocalizationManager.getString("valueloop_step_3_subtitle"))
        assertEquals("Aksi", LocalizationManager.getString("valueloop_step_4_title"))
        assertEquals("Eksekusi keputusan presisi", LocalizationManager.getString("valueloop_step_4_subtitle"))
        assertEquals("Dampak", LocalizationManager.getString("valueloop_step_5_title"))
        assertEquals("Pengukuran nilai terverifikasi", LocalizationManager.getString("valueloop_step_5_subtitle"))

        // English
        LocalizationManager.setLocale(AppLocale.EN)
        assertEquals("STRATEGIC SOLUTIONS", LocalizationManager.getString("valueloop_eyebrow"))
        assertEquals("Sustainable Value Loop", LocalizationManager.getString("valueloop_title"))
        assertEquals("Transforming data complexity into tangible, measurable impact for organizations and communities.", LocalizationManager.getString("valueloop_desc"))
        assertEquals("Insight", LocalizationManager.getString("valueloop_step_1_title"))
        assertEquals("Multi-dimensional data aggregation", LocalizationManager.getString("valueloop_step_1_subtitle"))
        assertEquals("Integrasi", LocalizationManager.getString("valueloop_step_2_title"))
        assertEquals("Unification of information silos", LocalizationManager.getString("valueloop_step_2_subtitle"))
        assertEquals("Analisis", LocalizationManager.getString("valueloop_step_3_title"))
        assertEquals("AI predictive modeling", LocalizationManager.getString("valueloop_step_3_subtitle"))
        assertEquals("Aksi", LocalizationManager.getString("valueloop_step_4_title"))
        assertEquals("Precision decision execution", LocalizationManager.getString("valueloop_step_4_subtitle"))
        assertEquals("Dampak", LocalizationManager.getString("valueloop_step_5_title"))
        assertEquals("Verified value measurement", LocalizationManager.getString("valueloop_step_5_subtitle"))
    }

    @Test
    fun testEBookPromoSectionLocalization() {
        LocalizationManager.setLocale(AppLocale.ID)
        assertEquals("PERPUSTAKAAN DIGITAL", LocalizationManager.getString("ebook_promo_eyebrow"))
        assertEquals("E-Book & Panduan\nStrategis.", LocalizationManager.getString("ebook_promo_title"))
        assertEquals("Semua", LocalizationManager.getString("ebook_filter_all"))
        assertEquals("Belum ada e-book untuk kategori ini.", LocalizationManager.getString("ebook_empty"))
        assertEquals("Lihat Detail", LocalizationManager.getString("ebook_view_details"))

        LocalizationManager.setLocale(AppLocale.EN)
        assertEquals("DIGITAL LIBRARY", LocalizationManager.getString("ebook_promo_eyebrow"))
        assertEquals("E-Books & Strategic\nGuides.", LocalizationManager.getString("ebook_promo_title"))
        assertEquals("All", LocalizationManager.getString("ebook_filter_all"))
        assertEquals("No e-books available for this category.", LocalizationManager.getString("ebook_empty"))
        assertEquals("View Details", LocalizationManager.getString("ebook_view_details"))
    }

    @Test
    fun testWhySekotaSectionLocalization() {
        LocalizationManager.setLocale(AppLocale.ID)
        assertEquals("MENGAPA SEKOTA", LocalizationManager.getString("why_sekota_eyebrow"))
        assertEquals("Arsitektur Keputusan Berbasis Bukti.", LocalizationManager.getString("why_sekota_title"))
        assertEquals(
            "Kami memadukan kedalaman riset kebijakan, kecerdasan buatan terapan, dan metodologi dampak terverifikasi untuk memandu navigasi strategis korporasi di tengah lanskap risiko 2026.",
            LocalizationManager.getString("why_sekota_desc")
        )
        assertEquals("Konsultasi Strategis", LocalizationManager.getString("why_sekota_cta"))
        assertEquals("Presisi Analitik", LocalizationManager.getString("why_sekota_card_1_title"))
        assertEquals("Kedalaman Riset & Data Nyata", LocalizationManager.getString("why_sekota_card_1_desc"))
        assertEquals("Tata Kelola Terpercaya", LocalizationManager.getString("why_sekota_card_2_title"))
        assertEquals("Standar Keamanan & Kepatuhan Tinggi", LocalizationManager.getString("why_sekota_card_2_desc"))
        assertEquals("Dampak Terukur", LocalizationManager.getString("why_sekota_card_3_title"))
        assertEquals("Metrik Keberlanjutan Sesuai Regulasi", LocalizationManager.getString("why_sekota_card_3_desc"))

        LocalizationManager.setLocale(AppLocale.EN)
        assertEquals("WHY SEKOTA", LocalizationManager.getString("why_sekota_eyebrow"))
        assertEquals("Evidence-Based Decision Architecture.", LocalizationManager.getString("why_sekota_title"))
        assertEquals(
            "We combine deep policy research, applied artificial intelligence, and verified impact methodologies to guide corporate strategic navigation amidst the 2026 risk landscape.",
            LocalizationManager.getString("why_sekota_desc")
        )
        assertEquals("Strategic Consultation", LocalizationManager.getString("why_sekota_cta"))
        assertEquals("Analytical Precision: Deep Research & Real-World Data", LocalizationManager.getString("why_sekota_card_1_title"))
        assertEquals("Trusted Governance: High Security & Compliance Standards", LocalizationManager.getString("why_sekota_card_2_title"))
        assertEquals("Measurable Impact: Regulatory-Aligned Sustainability Metrics", LocalizationManager.getString("why_sekota_card_3_title"))
    }

    @Test
    fun testContactFormSectionLocalization() {
        LocalizationManager.setLocale(AppLocale.ID)
        assertEquals("Mulai Diskusi Strategis.", LocalizationManager.getString("contact_title"))
        assertEquals(
            "Diskusikan tantangan analitik, kepatuhan ESG, atau tata kelola data organisasi Anda bersama tim konsultan Sekota.",
            LocalizationManager.getString("contact_subtitle")
        )
        assertEquals("Nama Lengkap", LocalizationManager.getString("contact_name_label"))
        assertEquals("Masukkan nama lengkap Anda", LocalizationManager.getString("contact_name_placeholder"))
        assertEquals("Email Perusahaan", LocalizationManager.getString("contact_email_label"))
        assertEquals("nama@perusahaan.com", LocalizationManager.getString("contact_email_placeholder"))
        assertEquals("Nama Institusi / Korporasi", LocalizationManager.getString("contact_org_label"))
        assertEquals("Contoh: PT Nusantara Daya", LocalizationManager.getString("contact_org_placeholder"))
        assertEquals("Topik Kebutuhan", LocalizationManager.getString("contact_topic_label"))
        assertEquals("Pesan / Catatan Tambahan", LocalizationManager.getString("contact_message_label"))
        assertEquals(
            "Jelaskan ringkas konteks kebutuhan atau permasalahan strategis Anda...",
            LocalizationManager.getString("contact_message_placeholder")
        )
        assertEquals("Kirim Permintaan Konsultasi", LocalizationManager.getString("contact_submit_btn"))
        assertEquals("Permintaan Konsultasi Diterima", LocalizationManager.getString("contact_success_title"))
        assertEquals("Tim analis strategis kami akan menghubungi Anda dalam 1x24 jam kerja.", LocalizationManager.getString("contact_success_desc"))

        LocalizationManager.setLocale(AppLocale.EN)
        assertEquals("Start a Strategic Discussion.", LocalizationManager.getString("contact_title"))
        assertEquals(
            "Discuss your organization's analytics, ESG compliance, or data governance challenges with Sekota consultants.",
            LocalizationManager.getString("contact_subtitle")
        )
        assertEquals("Full Name", LocalizationManager.getString("contact_name_label"))
        assertEquals("Enter your full name", LocalizationManager.getString("contact_name_placeholder"))
        assertEquals("Corporate Email", LocalizationManager.getString("contact_email_label"))
        assertEquals("name@company.com", LocalizationManager.getString("contact_email_placeholder"))
        assertEquals("Organization / Corporation", LocalizationManager.getString("contact_org_label"))
        assertEquals("e.g. Nusantara Energy Ltd.", LocalizationManager.getString("contact_org_placeholder"))
        assertEquals("Topic of Inquiry", LocalizationManager.getString("contact_topic_label"))
        assertEquals("Message / Additional Notes", LocalizationManager.getString("contact_message_label"))
        assertEquals(
            "Briefly outline your strategic requirements or challenges...",
            LocalizationManager.getString("contact_message_placeholder")
        )
        assertEquals("Submit Consultation Request", LocalizationManager.getString("contact_submit_btn"))
        assertEquals("Consultation Request Received", LocalizationManager.getString("contact_success_title"))
        assertEquals("Our strategic analyst team will contact you within 1 business day.", LocalizationManager.getString("contact_success_desc"))
    }

    @Test
    fun testCatalogAndBookDetailsLocalization() {
        LocalizationManager.setLocale(AppLocale.ID)
        assertEquals("PERPUSTAKAAN DIGITAL", LocalizationManager.getString("catalog_eyebrow"))
        assertEquals("eBook Catalogue", LocalizationManager.getString("catalog_title"))
        assertEquals("Tidak ada e-book yang cocok dengan filter Anda.", LocalizationManager.getString("catalog_empty"))
        assertEquals("Baca Sekarang", LocalizationManager.getString("book_details_read_btn"))
        assertEquals("Pratinjau PDF", LocalizationManager.getString("book_details_preview_pdf"))
        assertEquals("Sinopsis Riset", LocalizationManager.getString("book_details_synopsis"))
        assertEquals("Tentang Penulis", LocalizationManager.getString("book_details_about_author"))
        assertEquals("Apa yang Ada di Dalam Buku?", LocalizationManager.getString("book_details_whats_inside"))

        LocalizationManager.setLocale(AppLocale.EN)
        assertEquals("DIGITAL LIBRARY", LocalizationManager.getString("catalog_eyebrow"))
        assertEquals("eBook Catalogue", LocalizationManager.getString("catalog_title"))
        assertEquals("No e-books match your active filters.", LocalizationManager.getString("catalog_empty"))
        assertEquals("Read Now", LocalizationManager.getString("book_details_read_btn"))
        assertEquals("PDF Preview", LocalizationManager.getString("book_details_preview_pdf"))
        assertEquals("Research Synopsis", LocalizationManager.getString("book_details_synopsis"))
        assertEquals("About the Author", LocalizationManager.getString("book_details_about_author"))
        assertEquals("What's Inside the Book?", LocalizationManager.getString("book_details_whats_inside"))
    }

    @Test
    fun testAdminProductBilingualResolution() {
        val productWithEn = com.sekota.features.admin.domain.model.AdminProduct(
            id = "custom-1",
            code = "CST",
            name = "Custom Platform",
            categoryEyebrow = "Kategori ID",
            description = "Deskripsi dalam Bahasa Indonesia",
            features = listOf("Fitur 1", "Fitur 2"),
            categoryEyebrowEn = "Category EN",
            descriptionEn = "Description in English",
            featuresEn = listOf("Feature 1", "Feature 2")
        )

        // Resolve directly
        val resolvedEn = productWithEn.resolveForLocale(AppLocale.EN)
        assertEquals("Category EN", resolvedEn.categoryEyebrow)
        assertEquals("Description in English", resolvedEn.description)
        assertEquals(listOf("Feature 1", "Feature 2"), resolvedEn.features)

        val resolvedId = productWithEn.resolveForLocale(AppLocale.ID)
        assertEquals("Kategori ID", resolvedId.categoryEyebrow)
        assertEquals("Deskripsi dalam Bahasa Indonesia", resolvedId.description)
        assertEquals(listOf("Fitur 1", "Fitur 2"), resolvedId.features)

        // Test through LocalizationManager.getLocalizedProduct
        LocalizationManager.setLocale(AppLocale.EN)
        val localizedEn = LocalizationManager.getLocalizedProduct(productWithEn)
        assertEquals("Category EN", localizedEn.categoryEyebrow)
        assertEquals("Description in English", localizedEn.description)
        assertEquals(listOf("Feature 1", "Feature 2"), localizedEn.features)

        LocalizationManager.setLocale(AppLocale.ID)
        val localizedId = LocalizationManager.getLocalizedProduct(productWithEn)
        assertEquals("Kategori ID", localizedId.categoryEyebrow)
        assertEquals("Deskripsi dalam Bahasa Indonesia", localizedId.description)
        assertEquals(listOf("Fitur 1", "Fitur 2"), localizedId.features)

        // Test fallback to ID fields when EN fields are blank or null
        val productPartial = com.sekota.features.admin.domain.model.AdminProduct(
            id = "custom-2",
            code = "CST2",
            name = "Custom 2",
            categoryEyebrow = "Kategori Tetap",
            description = "Deskripsi Tetap",
            features = listOf("Fitur Tetap"),
            categoryEyebrowEn = "",
            descriptionEn = null,
            featuresEn = emptyList()
        )
        val fallbackEn = productPartial.resolveForLocale(AppLocale.EN)
        assertEquals("Kategori Tetap", fallbackEn.categoryEyebrow)
        assertEquals("Deskripsi Tetap", fallbackEn.description)
        assertEquals(listOf("Fitur Tetap"), fallbackEn.features)
    }

    @Test
    fun testAdminBookBilingualResolution() {
        val bookWithEn = com.sekota.features.admin.domain.model.AdminBook(
            id = "manifesto-ekuitas-lahan",
            title = "Manifesto Ekuitas Lahan",
            author = "Putu Aan J.",
            isbn = "978-623-99999-0-0",
            category = "SMART CITY",
            description = "Strategi dan framework implementasi Smart City dan tata kelola ekuitas lahan perkotaan.",
            categoryEn = "URBAN INTELLIGENCE",
            descriptionEn = "Smart City implementation strategy and equitable land governance frameworks."
        )

        // 1. Resolve directly
        val resolvedEn = bookWithEn.resolveForLocale(AppLocale.EN)
        assertEquals("URBAN INTELLIGENCE", resolvedEn.category)
        assertEquals("Smart City implementation strategy and equitable land governance frameworks.", resolvedEn.description)

        val resolvedId = bookWithEn.resolveForLocale(AppLocale.ID)
        assertEquals("SMART CITY", resolvedId.category)
        assertEquals("Strategi dan framework implementasi Smart City dan tata kelola ekuitas lahan perkotaan.", resolvedId.description)

        // 2. Test through LocalizationManager.getLocalizedBook
        LocalizationManager.setLocale(AppLocale.EN)
        val localizedEn = LocalizationManager.getLocalizedBook(bookWithEn)
        assertEquals("URBAN INTELLIGENCE", localizedEn.category)
        assertEquals("Smart City implementation strategy and equitable land governance frameworks.", localizedEn.description)

        LocalizationManager.setLocale(AppLocale.ID)
        val localizedId = LocalizationManager.getLocalizedBook(bookWithEn)
        assertEquals("SMART CITY", localizedId.category)
        assertEquals("Strategi dan framework implementasi Smart City dan tata kelola ekuitas lahan perkotaan.", localizedId.description)

        // 3. Fallback when EN fields are null or blank
        val bookFallback = com.sekota.features.admin.domain.model.AdminBook(
            id = "fallback-book",
            title = "Buku Fallback",
            author = "Penulis",
            isbn = "123",
            category = "Kategori Utama",
            description = "Deskripsi Utama",
            categoryEn = "  ",
            descriptionEn = null
        )
        val resolvedFallback = bookFallback.resolveForLocale(AppLocale.EN)
        assertEquals("Kategori Utama", resolvedFallback.category)
        assertEquals("Deskripsi Utama", resolvedFallback.description)
    }
}

