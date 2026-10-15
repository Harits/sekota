package com.sekota.localization

/**
 * Supported Locales in Sekota Web.
 * WP-048 (#22550) - Multi-localization architecture core.
 */
enum class AppLocale(val code: String, val displayName: String) {
    ID("id", "BAHASA INDONESIA"),
    EN("en", "ENGLISH");

    companion object {
        fun fromDisplayName(name: String): AppLocale {
            val trimmed = name.trim()
            return entries.firstOrNull { it.displayName.equals(trimmed, ignoreCase = true) }
                ?: entries.firstOrNull { it.code.equals(trimmed, ignoreCase = true) }
                ?: ID
        }

        fun fromCode(code: String): AppLocale {
            val trimmed = code.trim()
            return entries.firstOrNull { it.code.equals(trimmed, ignoreCase = true) }
                ?: entries.firstOrNull { it.displayName.equals(trimmed, ignoreCase = true) }
                ?: ID
        }
    }
}
