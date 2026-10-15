package com.sekota.localization

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * CompositionLocal providing LocalizationManager across the Compose hierarchy.
 * WP-048 (#22551) - Localization Provider.
 */
val LocalLocalization = staticCompositionLocalOf { LocalizationManager }

/**
 * Composable helper to resolve localized strings from the active LocalizationManager.
 * Automatically recomposes when the current locale changes.
 */
@Composable
fun stringResource(key: String): String {
    val manager = LocalLocalization.current
    val locale by manager.currentLocale.collectAsState()
    return manager.getString(key)
}

/**
 * Composable helper to resolve localized strings with index-based placeholder arguments.
 */
@Composable
fun stringResource(key: String, vararg args: Any): String {
    val manager = LocalLocalization.current
    val locale by manager.currentLocale.collectAsState()
    return manager.getString(key, *args)
}
