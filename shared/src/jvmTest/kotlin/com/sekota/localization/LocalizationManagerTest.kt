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
}
