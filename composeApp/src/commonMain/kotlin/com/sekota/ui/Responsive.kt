package com.sekota.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Material Design 3 window size classes.
 *
 * Breakpoints follow the M3 adaptive layout spec:
 *  - Compact    : < 600dp    (phone portrait)
 *  - Medium     : 600-839dp  (phone landscape, small tablet, split-screen browser)
 *  - Expanded   : 840-1199dp (tablet landscape, small desktop window)
 *  - Large      : 1200-1599dp (desktop)
 *  - ExtraLarge : >= 1600dp  (ultrawide; centered canvas, capped content width)
 *
 * Every adaptive decision across Web Sekota and the CMS Workbench derives from
 * this single source of truth, so a breakpoint change lands everywhere at once.
 */
enum class WindowWidth {
    Compact,
    Medium,
    Expanded,
    Large,
    ExtraLarge;

    val isCompact: Boolean get() = this == Compact
    /** Compact or Medium: everything narrower than a tablet landscape. */
    val isAtMostMedium: Boolean get() = this == Compact || this == Medium
    val isAtLeastExpanded: Boolean get() = ordinal >= Expanded.ordinal
    val isAtLeastLarge: Boolean get() = ordinal >= Large.ordinal
}

object Breakpoints {
    val Medium: Dp = 600.dp
    val Expanded: Dp = 840.dp
    val Large: Dp = 1200.dp
    val ExtraLarge: Dp = 1600.dp

    /** Content never stretches past this on ultrawide desktop monitors. */
    val MaxContentWidth: Dp = 1400.dp
}

/** Classifies a measured width into its M3 window size class. */
fun windowWidthOf(width: Dp): WindowWidth = when {
    width < Breakpoints.Medium -> WindowWidth.Compact
    width < Breakpoints.Expanded -> WindowWidth.Medium
    width < Breakpoints.Large -> WindowWidth.Expanded
    width < Breakpoints.ExtraLarge -> WindowWidth.Large
    else -> WindowWidth.ExtraLarge
}

@Composable
fun rememberWindowWidth(width: Dp): WindowWidth = remember(width) { windowWidthOf(width) }

/**
 * M3 pane margin, widened for this brand's editorial desktop layout.
 * Compact keeps the 20dp gutter the Hero and Catalog already use.
 */
val WindowWidth.sectionHorizontalPadding: Dp
    get() = when (this) {
        WindowWidth.Compact -> 20.dp
        WindowWidth.Medium -> 32.dp
        WindowWidth.Expanded -> 48.dp
        else -> 64.dp
    }

/** Vertical rhythm between landing-page sections. */
val WindowWidth.sectionVerticalPadding: Dp
    get() = when (this) {
        WindowWidth.Compact -> 56.dp
        WindowWidth.Medium -> 72.dp
        WindowWidth.Expanded -> 100.dp
        else -> 120.dp
    }

/** Inner padding for content cards (feature, suite, value-prop). */
val WindowWidth.cardPadding: Dp
    get() = when (this) {
        WindowWidth.Compact -> 24.dp
        WindowWidth.Medium -> 28.dp
        else -> 40.dp
    }

/** Columns for a product / e-book card grid laid out inside the main content pane. */
val WindowWidth.contentGridColumns: Int
    get() = when (this) {
        WindowWidth.Compact -> 1
        WindowWidth.Medium -> 2
        else -> 3
    }

/** Gap between grid cells; tighter on small screens so cards keep usable width. */
val WindowWidth.gridSpacing: Dp
    get() = if (isCompact) 20.dp else 32.dp

/** Height of the sticky public navbar, which collapses to a 64dp M3 top app bar. */
val WindowWidth.navbarHeight: Dp
    get() = if (isAtLeastExpanded) 80.dp else 64.dp

/**
 * Horizontal pane padding that also centres the canvas on ultrawide displays.
 *
 * Past [Breakpoints.MaxContentWidth] the extra space becomes symmetric gutter
 * rather than ever-longer lines of text, which keeps section backgrounds
 * full-bleed while the content itself stays within a readable measure.
 */
fun contentHorizontalPadding(availableWidth: Dp): Dp {
    val base = windowWidthOf(availableWidth).sectionHorizontalPadding
    val overflow = (availableWidth - Breakpoints.MaxContentWidth) / 2
    return if (overflow > base) overflow else base
}
