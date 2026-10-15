package com.sekota.localization

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Singleton Localization Manager for Sekota Web.
 * WP-048 (#22550) - Multi-localization architecture & dictionary resolver.
 */
object LocalizationManager {

    private val _currentLocale = MutableStateFlow(AppLocale.ID)
    val currentLocale: StateFlow<AppLocale> = _currentLocale.asStateFlow()

    fun setLocale(locale: AppLocale) {
        _currentLocale.value = locale
    }

    fun setLocale(localeNameOrCode: String) {
        _currentLocale.value = AppLocale.fromDisplayName(localeNameOrCode)
    }

    /**
     * Resolves localized string by key with safe fallback:
     * Current Locale Map -> ID Locale Map -> Key itself.
     */
    fun getString(key: String): String {
        val locale = _currentLocale.value
        val localizedMap = when (locale) {
            AppLocale.EN -> EnglishStrings
            AppLocale.ID -> IndonesianStrings
        }
        return localizedMap[key] ?: IndonesianStrings[key] ?: key
    }

    /**
     * Resolves localized string with index-based placeholder substitution ({0}, {1}, etc.).
     */
    fun getString(key: String, vararg args: Any): String {
        var str = getString(key)
        args.forEachIndexed { index, arg ->
            str = str.replace("{$index}", arg.toString())
        }
        return str
    }

    // Comprehensive Bilingual Dictionaries
    private val IndonesianStrings: Map<String, String> = mapOf(
        // Navigation Labels
        "nav_home" to "Beranda",
        "nav_solusi" to "Solusi",
        "nav_produk" to "Produk",
        "nav_ebook" to "E-Book",
        "nav_mengapa" to "Mengapa Sekota",
        "nav_merchandise" to "Merchandise",
        "nav_kontak" to "Kontak",
        "nav_login" to "Masuk / Login",
        "nav_login_short" to "Login",
        "nav_signup" to "Daftar",
        "nav_profile" to "Profil Saya",
        "nav_profile_short" to "Profil",
        "nav_admin" to "Admin",
        "nav_cta_consultation" to "Konsultasi Strategis",
        "nav_sync_live" to "LIVE",
        "nav_sync_live_full" to "LIVE SYNC",
        "nav_sync_offline" to "OFFLINE",

        // Hero Section Copy
        "hero_eyebrow" to "TRUSTED INTELLIGENCE. MEASURED IMPACT.",
        "hero_title_part1" to "Ekosistem Produk",
        "hero_title_part2" to "Intelligence Suite",
        "hero_title_part3" to "Sekota Sinergi Indonesia",
        "hero_description" to "Sekota menghadirkan Intelligence Suite: ekosistem produk berbasis data yang dirancang untuk membantu korporasi memetakan risiko sosial, mengukur dampak keberlanjutan, dan memperkuat relasi pemangku kepentingan.",
        "hero_cta_explore" to "Eksplorasi Produk",
        "hero_cta_contact" to "Hubungi Tim Ahli",
        "hero_suite_badge" to "INTELLIGENCE SUITE\nPRODUCTS",
        "hero_metric_title" to "LIVE METRICS",
        "hero_metric_accuracy" to "Data Accuracy",
        "hero_metric_clients" to "KLIEN & MITRA",
        "hero_metric_established" to "TAHUN BERDIRI",

        // Value Loop & Feature Copy
        "features_eyebrow" to "TANTANGAN 2026",
        "features_title" to "Data Melimpah,\nKeputusan Masih Kabur.",
        "features_subtitle" to "Di tahun 2026, organisasi modern menghadapi paradoks data, volume informasi yang belum pernah sebesar ini, namun kepercayaan terhadap data justru semakin menipis.",
        "feature_1_title" to "Data Tidak Terintegrasi",
        "feature_1_desc" to "Silo data antar departemen dan sistem yang tidak terhubung menghasilkan insight yang fragmentaris dan tidak dapat diandalkan untuk keputusan level eksekutif.",
        "feature_2_title" to "AI Tanpa Transparansi",
        "feature_2_desc" to "Penggunaan AI yang tidak dapat dijelaskan (black-box) memicu krisis kepercayaan internal dan eksternal, serta risiko kepatuhan regulasi yang semakin ketat.",
        "feature_3_title" to "ESG Hanya Beban Administratif",
        "feature_3_desc" to "Pelaporan ESG yang terpisah dari operasional bisnis menjadikannya beban birokrasi, bukan kompas strategis yang mendorong nilai jangka panjang.",

        "value_loop_eyebrow" to "SOLUSI STRATEGIS",
        "value_loop_title" to "Siklus Nilai Berkelanjutan",
        "value_loop_desc" to "Mengubah kompleksitas data menjadi dampak nyata terukur bagi organisasi dan masyarakat.",

        // Catalog & Reader Terms
        "catalog_filter_category" to "Kategori",
        "catalog_all_categories" to "Semua Kategori",
        "catalog_sort_by" to "Urutkan",
        "catalog_sort_latest" to "Terbaru",
        "catalog_sort_popular" to "Terpopuler",
        "catalog_sort_rating" to "Rating Tertinggi",
        "catalog_read_now" to "Baca Sekarang",
        "catalog_search_placeholder" to "Cari publikasi atau topik riset...",
        "reader_back_to_catalog" to "Kembali ke Katalog",
        "reader_chapters" to "Daftar Bab",
        "reader_progress" to "Kemajuan Membaca",
        "reader_night_mode" to "Mode Malam",
        "reader_bookmark" to "Tandai Halaman",
        "reader_font_size" to "Ukuran Teks",
        "reader_table_of_contents" to "Daftar Isi",

        // Footer Columns and Copyright Text
        "footer_tagline" to "Trusted Intelligence for Sustainable Impact. Memberdayakan keputusan strategis melalui data.",
        "footer_col_products" to "PRODUK",
        "footer_col_company" to "PERUSAHAAN",
        "footer_col_legal" to "LEGAL",
        "footer_link_about" to "Tentang Kami",
        "footer_link_ebook" to "E-Book",
        "footer_link_merch" to "Merchandise",
        "footer_link_insights" to "Insights",
        "footer_link_contact" to "Kontak",
        "footer_link_privacy" to "Privasi",
        "footer_link_terms" to "Syarat & Ketentuan",
        "footer_affiliation" to "Afiliasi eBdesk Group",
        "footer_affiliation_subtext" to "MITRA NASIONAL",
        "footer_copyright" to "© 2025 PT. SEKOTA SINERGI INDONESIA. BAGIAN DARI EBDESK GROUP.",
        "footer_lang_id" to "BAHASA INDONESIA",
        "footer_lang_en" to "ENGLISH",

        // Intelligence Suite Section
        "suite_eyebrow" to "EKOSISTEM PRODUK",
        "suite_title" to "Intelligence Suite.",
        "suite_subtitle" to "Dirancang untuk saling terhubung dalam satu ekosistem data yang kohesif.",
        "suite_learn_more" to "Pelajari Selengkapnya",

        // Suite Product Translations (Veridia)
        "product_vrd_category" to "ESG & Carbon Intelligence",
        "product_vrd_desc" to "Platform analitik komprehensif untuk pemantauan jejak karbon Scope 1, 2, dan 3, kepatuhan regulasi lingkungan, dan pelaporan ESG otomatis sesuai standar global.",
        "product_vrd_feat_1" to "Audit Jejak Karbon Scope 1, 2, & 3 Real-time",
        "product_vrd_feat_2" to "Pelaporan Keberlanjutan Otomatis (GRI, ISSB, IDX-ESG)",
        "product_vrd_feat_3" to "Pemodelan Skenario Dekarbonisasi Prediktif",
        "product_vrd_feat_4" to "Dashboard Tata Kelola Pemangku Kepentingan",

        // Suite Product Translations (Ascendio)
        "product_asc_category" to "Strategic Corporate Intelligence",
        "product_asc_desc" to "Sistem intelijen bisnis strategis untuk pemetaan kompetitif pasar, analisis sentimen kebijakan publik, dan mitigasi risiko reputasi berbasis big data AI.",
        "product_asc_feat_1" to "Pemantauan Sentimen Media & Kebijakan Publik 24/7",
        "product_asc_feat_2" to "Pemetaan Risiko Reputasi & Mitigasi Krisis B2B/B2G",
        "product_asc_feat_3" to "Analisis Lanskap Regulasi & Pemangku Kepentingan",
        "product_asc_feat_4" to "Executive Briefing & AI Strategic Recommendations",

        // Suite Product Translations (Sociara)
        "product_soc_category" to "Social Impact & Community Engagement",
        "product_soc_desc" to "Platform manajemen dan evaluasi dampak sosial (Social Return on Investment / SROI), relasi masyarakat lingkar industri, dan program CSR korporasi berkelanjutan.",
        "product_soc_feat_1" to "Pengukuran SROI & Evaluasi Efektivitas Dampak CSR",
        "product_soc_feat_2" to "Pemetaan Sosial & Dinamika Komunitas Lingkar Industri",
        "product_soc_feat_3" to "Manajemen Keluhan & Relasi Pemangku Kepentingan Lokal",
        "product_soc_feat_4" to "Pelaporan Keterlibatan Sosial Terverifikasi Audit",

        // Suite Product Translations (Ecoflow)
        "product_eco_category" to "Resource & Ecological Intelligence",
        "product_eco_desc" to "Solusi pemantauan aliran sumber daya energi, air, dan limbah sirkular untuk efisiensi operasional industri menuju target Net Zero Emission.",
        "product_eco_feat_1" to "Pemantauan Konsumsi Energi & Efisiensi Operasional",
        "product_eco_feat_2" to "Manajemen Sirkularitas Limbah & Daur Ulang Industri",
        "product_eco_feat_3" to "Pelacakan Konsumsi & Kualitas Neraca Air Industri",
        "product_eco_feat_4" to "Integrasi Sertifikasi Green Building & ISO 14001",

        // Value Loop Section
        "valueloop_eyebrow" to "SOLUSI STRATEGIS",
        "valueloop_title" to "Siklus Nilai Berkelanjutan",
        "valueloop_desc" to "Mengubah kompleksitas data menjadi dampak nyata terukur bagi organisasi dan masyarakat.",
        "valueloop_step_1_title" to "Insight",
        "valueloop_step_1_subtitle" to "Agregasi data multi-dimensi",
        "valueloop_step_2_title" to "Integrasi",
        "valueloop_step_2_subtitle" to "Unifikasi silo informasi",
        "valueloop_step_3_title" to "Analisis",
        "valueloop_step_3_subtitle" to "Pemodelan prediktif AI",
        "valueloop_step_4_title" to "Aksi",
        "valueloop_step_4_subtitle" to "Eksekusi keputusan presisi",
        "valueloop_step_5_title" to "Dampak",
        "valueloop_step_5_subtitle" to "Pengukuran nilai terverifikasi",

        // E-Book Promo Section
        "ebook_promo_eyebrow" to "PERPUSTAKAAN DIGITAL",
        "ebook_promo_title" to "E-Book & Panduan\nStrategis.",
        "ebook_filter_all" to "Semua",
        "ebook_empty" to "Belum ada e-book untuk kategori ini.",
        "ebook_view_details" to "Lihat Detail",

        // Why Sekota / ValuePropDark Section
        "why_sekota_eyebrow" to "MENGAPA SEKOTA",
        "why_sekota_title" to "Arsitektur Keputusan Berbasis Bukti.",
        "why_sekota_desc" to "Kami memadukan kedalaman riset kebijakan, kecerdasan buatan terapan, dan metodologi dampak terverifikasi untuk memandu navigasi strategis korporasi di tengah lanskap risiko 2026.",
        "why_sekota_cta" to "Konsultasi Strategis",
        "why_sekota_card_1_title" to "Presisi Analitik",
        "why_sekota_card_1_desc" to "Kedalaman Riset & Data Nyata",
        "why_sekota_card_2_title" to "Tata Kelola Terpercaya",
        "why_sekota_card_2_desc" to "Standar Keamanan & Kepatuhan Tinggi",
        "why_sekota_card_3_title" to "Dampak Terukur",
        "why_sekota_card_3_desc" to "Metrik Keberlanjutan Sesuai Regulasi",

        // Contact Form Section
        "contact_title" to "Mulai Diskusi Strategis.",
        "contact_subtitle" to "Diskusikan tantangan analitik, kepatuhan ESG, atau tata kelola data organisasi Anda bersama tim konsultan Sekota.",
        "contact_name_label" to "Nama Lengkap",
        "contact_name_placeholder" to "Masukkan nama lengkap Anda",
        "contact_email_label" to "Email Perusahaan",
        "contact_email_placeholder" to "nama@perusahaan.com",
        "contact_org_label" to "Nama Institusi / Korporasi",
        "contact_org_placeholder" to "Contoh: PT Nusantara Daya",
        "contact_topic_label" to "Topik Kebutuhan",
        "contact_message_label" to "Pesan / Catatan Tambahan",
        "contact_message_placeholder" to "Jelaskan ringkas konteks kebutuhan atau permasalahan strategis Anda...",
        "contact_submit_btn" to "Kirim Permintaan Konsultasi",
        "contact_success_title" to "Permintaan Konsultasi Diterima",
        "contact_success_desc" to "Tim analis strategis kami akan menghubungi Anda dalam 1x24 jam kerja.",

        // Catalog, Book Details & Merchandise Global Terms
        "catalog_eyebrow" to "PERPUSTAKAAN DIGITAL",
        "catalog_title" to "eBook Catalogue",
        "catalog_desc" to "Membawa semangat sinergi ke dalam genggaman. E-book katalog ini adalah kurasi instrumen taktis yang dirancang untuk memperkuat konektivitas tim dan mitra strategis Anda. Mari orkestrasi identitas profesional bersama Sekota.",
        "catalog_empty" to "Tidak ada e-book yang cocok dengan filter Anda.",
        "book_details_read_btn" to "Baca Sekarang",
        "book_details_preview_pdf" to "Pratinjau PDF",
        "book_details_synopsis" to "Sinopsis Riset",
        "book_details_about_author" to "Tentang Penulis",
        "book_details_whats_inside" to "Apa yang Ada di Dalam Buku?",

        // Raw label fallback bridges
        "Solusi" to "Solusi",
        "Produk" to "Produk",
        "E-Book" to "E-Book",
        "Mengapa Sekota" to "Mengapa Sekota",
        "Merchandise" to "Merchandise",
        "Kontak" to "Kontak",
        "Tentang Kami" to "Tentang Kami",
        "Privasi" to "Privasi",
        "Syarat & Ketentuan" to "Syarat & Ketentuan",
        "Konsultasi Strategis" to "Konsultasi Strategis"
    )

    private val EnglishStrings: Map<String, String> = mapOf(
        // Navigation Labels
        "nav_home" to "Home",
        "nav_solusi" to "Solutions",
        "nav_produk" to "Products",
        "nav_ebook" to "E-Book",
        "nav_mengapa" to "Why Sekota",
        "nav_merchandise" to "Merchandise",
        "nav_kontak" to "Contact",
        "nav_login" to "Login",
        "nav_login_short" to "Login",
        "nav_signup" to "Sign Up",
        "nav_profile" to "My Profile",
        "nav_profile_short" to "Profile",
        "nav_admin" to "Admin",
        "nav_cta_consultation" to "Strategic Consultation",
        "nav_sync_live" to "LIVE",
        "nav_sync_live_full" to "LIVE SYNC",
        "nav_sync_offline" to "OFFLINE",

        // Hero Section Copy
        "hero_eyebrow" to "TRUSTED INTELLIGENCE. MEASURED IMPACT.",
        "hero_title_part1" to "Product Ecosystem",
        "hero_title_part2" to "Intelligence Suite",
        "hero_title_part3" to "Sekota Sinergi Indonesia",
        "hero_description" to "Sekota presents Intelligence Suite: a data-driven product ecosystem designed to help corporations map social risks, measure sustainability impacts, and strengthen stakeholder relationships.",
        "hero_cta_explore" to "Explore Products",
        "hero_cta_contact" to "Contact Expert Team",
        "hero_suite_badge" to "INTELLIGENCE SUITE\nPRODUCTS",
        "hero_metric_title" to "LIVE METRICS",
        "hero_metric_accuracy" to "Data Accuracy",
        "hero_metric_clients" to "CLIENTS & PARTNERS",
        "hero_metric_established" to "ESTABLISHED YEAR",

        // Value Loop & Feature Copy
        "features_eyebrow" to "2026 CHALLENGES",
        "features_title" to "Abundant Data,\nUnclear Decisions.",
        "features_subtitle" to "In 2026, modern organizations face a data paradox: information volume has never been greater, yet trust in data is eroding.",
        "feature_1_title" to "Unintegrated Data",
        "feature_1_desc" to "Data silos across departments and disconnected systems yield fragmented insights that cannot be trusted for executive decisions.",
        "feature_2_title" to "AI Without Transparency",
        "feature_2_desc" to "Unexplainable AI (black-box) sparks internal and external trust crises, alongside mounting regulatory compliance risks.",
        "feature_3_title" to "ESG as Mere Administrative Burden",
        "feature_3_desc" to "ESG reporting divorced from business operations becomes bureaucratic baggage, rather than a strategic compass driving long-term value.",

        "value_loop_eyebrow" to "STRATEGIC SOLUTIONS",
        "value_loop_title" to "Sustainable Value Loop",
        "value_loop_desc" to "Transforming data complexity into tangible, measurable impact for organizations and communities.",

        // Catalog & Reader Terms
        "catalog_filter_category" to "Category",
        "catalog_all_categories" to "All Categories",
        "catalog_sort_by" to "Sort By",
        "catalog_sort_latest" to "Latest",
        "catalog_sort_popular" to "Most Popular",
        "catalog_sort_rating" to "Highest Rated",
        "catalog_read_now" to "Read Now",
        "catalog_search_placeholder" to "Search publications or research topics...",
        "reader_back_to_catalog" to "Back to Catalog",
        "reader_chapters" to "Chapters",
        "reader_progress" to "Reading Progress",
        "reader_night_mode" to "Night Mode",
        "reader_bookmark" to "Bookmark",
        "reader_font_size" to "Text Size",
        "reader_table_of_contents" to "Table of Contents",

        // Footer Columns and Copyright Text
        "footer_tagline" to "Trusted Intelligence for Sustainable Impact. Empowering strategic decisions through data.",
        "footer_col_products" to "PRODUCTS",
        "footer_col_company" to "COMPANY",
        "footer_col_legal" to "LEGAL",
        "footer_link_about" to "About Us",
        "footer_link_ebook" to "E-Book",
        "footer_link_merch" to "Merchandise",
        "footer_link_insights" to "Insights",
        "footer_link_contact" to "Contact",
        "footer_link_privacy" to "Privacy Policy",
        "footer_link_terms" to "Terms & Conditions",
        "footer_affiliation" to "eBdesk Group Affiliation",
        "footer_affiliation_subtext" to "NATIONWIDE PARTNER",
        "footer_copyright" to "© 2025 PT. SEKOTA SINERGI INDONESIA. PART OF EBDESK GROUP.",
        "footer_lang_id" to "BAHASA INDONESIA",
        "footer_lang_en" to "ENGLISH",

        // Intelligence Suite Section
        "suite_eyebrow" to "PRODUCT ECOSYSTEM",
        "suite_title" to "Intelligence Suite.",
        "suite_subtitle" to "Designed to interconnect within a cohesive data ecosystem.",
        "suite_learn_more" to "Learn More",

        // Suite Product Translations (Veridia)
        "product_vrd_category" to "ESG & Carbon Intelligence",
        "product_vrd_desc" to "Comprehensive analytics platform for Scope 1, 2, and 3 carbon footprint tracking, environmental compliance, and automated ESG reporting aligned with global standards.",
        "product_vrd_feat_1" to "Real-time Scope 1, 2, & 3 Carbon Footprint Audit",
        "product_vrd_feat_2" to "Automated Sustainability Reporting (GRI, ISSB, IDX-ESG)",
        "product_vrd_feat_3" to "Predictive Decarbonization Scenario Modeling",
        "product_vrd_feat_4" to "Stakeholder Governance Dashboard",

        // Suite Product Translations (Ascendio)
        "product_asc_category" to "Strategic Corporate Intelligence",
        "product_asc_desc" to "Strategic business intelligence system for competitive market mapping, public policy sentiment analysis, and AI-powered reputation risk mitigation.",
        "product_asc_feat_1" to "24/7 Media Sentiment & Public Policy Monitoring",
        "product_asc_feat_2" to "Reputation Risk Mapping & B2B/B2G Crisis Mitigation",
        "product_asc_feat_3" to "Regulatory & Stakeholder Landscape Analysis",
        "product_asc_feat_4" to "Executive Briefing & AI Strategic Recommendations",

        // Suite Product Translations (Sociara)
        "product_soc_category" to "Social Impact & Community Engagement",
        "product_soc_desc" to "Social impact management and evaluation platform (Social Return on Investment / SROI), industrial community relations, and sustainable corporate CSR programs.",
        "product_soc_feat_1" to "SROI Measurement & CSR Impact Effectiveness Evaluation",
        "product_soc_feat_2" to "Social Mapping & Community Dynamics Around Industrial Rings",
        "product_soc_feat_3" to "Grievance Management & Local Stakeholder Relations",
        "product_soc_feat_4" to "Audit-Verified Social Engagement Reporting",

        // Suite Product Translations (Ecoflow)
        "product_eco_category" to "Resource & Ecological Intelligence",
        "product_eco_desc" to "Resource flow monitoring solution for energy, water, and circular waste to drive industrial operational efficiency toward Net Zero targets.",
        "product_eco_feat_1" to "Energy Consumption Monitoring & Operational Efficiency",
        "product_eco_feat_2" to "Industrial Waste Circularity & Recycling Management",
        "product_eco_feat_3" to "Industrial Water Balance Quality & Consumption Tracking",
        "product_eco_feat_4" to "Green Building & ISO 14001 Certification Integration",

        // Value Loop Section
        "valueloop_eyebrow" to "STRATEGIC SOLUTIONS",
        "valueloop_title" to "Sustainable Value Loop",
        "valueloop_desc" to "Transforming data complexity into tangible, measurable impact for organizations and communities.",
        "valueloop_step_1_title" to "Insight",
        "valueloop_step_1_subtitle" to "Multi-dimensional data aggregation",
        "valueloop_step_2_title" to "Integrasi",
        "valueloop_step_2_subtitle" to "Unification of information silos",
        "valueloop_step_3_title" to "Analisis",
        "valueloop_step_3_subtitle" to "AI predictive modeling",
        "valueloop_step_4_title" to "Aksi",
        "valueloop_step_4_subtitle" to "Precision decision execution",
        "valueloop_step_5_title" to "Dampak",
        "valueloop_step_5_subtitle" to "Verified value measurement",

        // E-Book Promo Section
        "ebook_promo_eyebrow" to "DIGITAL LIBRARY",
        "ebook_promo_title" to "E-Books & Strategic\nGuides.",
        "ebook_filter_all" to "All",
        "ebook_empty" to "No e-books available for this category.",
        "ebook_view_details" to "View Details",

        // Why Sekota / ValuePropDark Section
        "why_sekota_eyebrow" to "WHY SEKOTA",
        "why_sekota_title" to "Evidence-Based Decision Architecture.",
        "why_sekota_desc" to "We combine deep policy research, applied artificial intelligence, and verified impact methodologies to guide corporate strategic navigation amidst the 2026 risk landscape.",
        "why_sekota_cta" to "Strategic Consultation",
        "why_sekota_card_1_title" to "Analytical Precision: Deep Research & Real-World Data",
        "why_sekota_card_1_desc" to "Analytical Precision: Deep Research & Real-World Data",
        "why_sekota_card_2_title" to "Trusted Governance: High Security & Compliance Standards",
        "why_sekota_card_2_desc" to "Trusted Governance: High Security & Compliance Standards",
        "why_sekota_card_3_title" to "Measurable Impact: Regulatory-Aligned Sustainability Metrics",
        "why_sekota_card_3_desc" to "Measurable Impact: Regulatory-Aligned Sustainability Metrics",

        // Contact Form Section
        "contact_title" to "Start a Strategic Discussion.",
        "contact_subtitle" to "Discuss your organization's analytics, ESG compliance, or data governance challenges with Sekota consultants.",
        "contact_name_label" to "Full Name",
        "contact_name_placeholder" to "Enter your full name",
        "contact_email_label" to "Corporate Email",
        "contact_email_placeholder" to "name@company.com",
        "contact_org_label" to "Organization / Corporation",
        "contact_org_placeholder" to "e.g. Nusantara Energy Ltd.",
        "contact_topic_label" to "Topic of Inquiry",
        "contact_message_label" to "Message / Additional Notes",
        "contact_message_placeholder" to "Briefly outline your strategic requirements or challenges...",
        "contact_submit_btn" to "Submit Consultation Request",
        "contact_success_title" to "Consultation Request Received",
        "contact_success_desc" to "Our strategic analyst team will contact you within 1 business day.",

        // Catalog, Book Details & Merchandise Global Terms
        "catalog_eyebrow" to "DIGITAL LIBRARY",
        "catalog_title" to "eBook Catalogue",
        "catalog_desc" to "Bringing the spirit of synergy into your hands. This eBook catalogue is a curated tactical instrument designed to strengthen team and partner connectivity.",
        "catalog_empty" to "No e-books match your active filters.",
        "book_details_read_btn" to "Read Now",
        "book_details_preview_pdf" to "PDF Preview",
        "book_details_synopsis" to "Research Synopsis",
        "book_details_about_author" to "About the Author",
        "book_details_whats_inside" to "What's Inside the Book?",

        // Raw label fallback bridges
        "Solusi" to "Solutions",
        "Produk" to "Products",
        "E-Book" to "E-Book",
        "Mengapa Sekota" to "Why Sekota",
        "Merchandise" to "Merchandise",
        "Kontak" to "Contact",
        "Tentang Kami" to "About Us",
        "Privasi" to "Privacy Policy",
        "Syarat & Ketentuan" to "Terms & Conditions",
        "Konsultasi Strategis" to "Strategic Consultation"
    )

    /**
     * Resolves localized version of canonical products (Veridia, Ascendio, Sociara, Ecoflow) or dynamic products.
     * First checks if product bilingual fields (descriptionEn or featuresEn) are present and current locale is EN.
     * Otherwise falls back to canonical dictionary lookups.
     */
    fun getLocalizedProduct(product: com.sekota.features.admin.domain.model.AdminProduct): com.sekota.features.admin.domain.model.AdminProduct {
        val locale = _currentLocale.value
        if (locale == AppLocale.EN) {
            val hasEnContent = (!product.descriptionEn.isNullOrBlank()) || product.featuresEn.isNotEmpty() || (!product.categoryEyebrowEn.isNullOrBlank())
            if (hasEnContent) {
                return product.resolveForLocale(AppLocale.EN)
            }
        }

        val codeKey = when (product.code.uppercase().trim()) {
            "VRD" -> "vrd"
            "ASC" -> "asc"
            "SOC" -> "soc"
            "ECO" -> "eco"
            else -> when (product.name.lowercase().trim()) {
                "veridia" -> "vrd"
                "ascendio" -> "asc"
                "sociara" -> "soc"
                "ecoflow" -> "eco"
                else -> null
            }
        } ?: return product

        val localizedCategory = getString("product_${codeKey}_category")
        val localizedDesc = getString("product_${codeKey}_desc")
        val localizedFeatures = (1..product.features.size.coerceAtLeast(4)).mapNotNull { idx ->
            val featKey = "product_${codeKey}_feat_$idx"
            if (IndonesianStrings.containsKey(featKey) || EnglishStrings.containsKey(featKey)) {
                getString(featKey)
            } else null
        }.ifEmpty { product.features }

        return product.copy(
            categoryEyebrow = if (localizedCategory != "product_${codeKey}_category") localizedCategory else product.categoryEyebrow,
            description = if (localizedDesc != "product_${codeKey}_desc") localizedDesc else product.description,
            features = localizedFeatures
        )
    }

    /**
     * Resolves localized version of a book for the current active locale.
     * When EN is selected, delegates to book.resolveForLocale(AppLocale.EN).
     */
    fun getLocalizedBook(book: com.sekota.features.admin.domain.model.AdminBook): com.sekota.features.admin.domain.model.AdminBook {
        return book.resolveForLocale(_currentLocale.value)
    }
}
