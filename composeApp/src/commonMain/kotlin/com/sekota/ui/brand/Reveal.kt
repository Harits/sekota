package com.sekota.ui.brand

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** The design's --ease curve: cubic-bezier(.2,.7,.2,1). */
val BrandEasing = CubicBezierEasing(0.2f, 0.7f, 0.2f, 1f)

/**
 * Platform "reduce motion" preference. Web reads `prefers-reduced-motion`;
 * other targets return false.
 */
expect fun platformPrefersReducedMotion(): Boolean

/**
 * True when animations must be skipped. Provided at the app root from
 * [platformPrefersReducedMotion]; previews and tests can override it.
 */
val LocalReduceMotion = staticCompositionLocalOf { false }

/**
 * Fades and lifts an element in the first time it enters the viewport
 * (WEB-07-01-03 / #22662), matching the design's `data-reveal` behaviour.
 *
 * @param index position in a group; each step delays the start by 80 ms.
 * Under reduce motion, in previews, or before the window is measured, the element
 * is shown immediately with no movement.
 */
fun Modifier.reveal(index: Int = 0, distance: Dp = 24.dp): Modifier = composed {
    if (LocalReduceMotion.current || LocalInspectionMode.current) return@composed this

    val windowHeight = LocalWindowInfo.current.containerSize.height
    var shown by remember { mutableStateOf(windowHeight <= 0) }
    val progress by animateFloatAsState(
        targetValue = if (shown) 1f else 0f,
        animationSpec = tween(durationMillis = 700, delayMillis = (index.coerceAtLeast(0) * 80).coerceAtMost(480), easing = BrandEasing),
        label = "reveal"
    )
    val offsetPx = with(LocalDensity.current) { distance.toPx() }

    this
        .onGloballyPositioned { coordinates ->
            if (!shown) {
                val top = coordinates.boundsInWindow().top
                if (windowHeight <= 0 || top < windowHeight * 0.92f) shown = true
            }
        }
        .graphicsLayer {
            alpha = progress
            translationY = (1f - progress) * offsetPx
        }
}
