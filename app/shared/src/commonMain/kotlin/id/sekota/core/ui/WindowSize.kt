package id.sekota.core.ui

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Breakpoints follow the design's media queries:
 * <600 phone, <860 collapsed nav, <1100 two-column fallbacks, >=1100 full layout.
 */
enum class WindowSize {
    Compact,
    Medium,
    Wide,
    Expanded;

    val isCompact: Boolean get() = this == Compact
    val isPhone: Boolean get() = this == Compact || this == Medium
    val isCollapsedNav: Boolean get() = this == Compact || this == Medium
    val isExpanded: Boolean get() = this == Expanded
}

val LocalWindowSize = staticCompositionLocalOf { WindowSize.Expanded }

/** Viewport height, useful for full-height heroes (design `.ct{min-height:100vh}`). */
val LocalViewportHeight = androidx.compose.runtime.staticCompositionLocalOf { 800.dp }

@Composable
fun ProvideWindowSize(content: @Composable () -> Unit) {
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val size = when {
            maxWidth < 600.dp -> WindowSize.Compact
            maxWidth < 860.dp -> WindowSize.Medium
            maxWidth < 1100.dp -> WindowSize.Wide
            else -> WindowSize.Expanded
        }
        CompositionLocalProvider(
            LocalWindowSize provides size,
            LocalViewportHeight provides maxHeight,
            content = content,
        )
    }
}
