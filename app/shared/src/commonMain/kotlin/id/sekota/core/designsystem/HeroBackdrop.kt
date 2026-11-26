package id.sekota.core.designsystem

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Grid + three drifting glows from the design's `.hero-bg`.
 * Coordinates mirror `index.html`: 520px cyan (right 6%/top 6%),
 * 380px green (right -4%/top 46%), 420px green (left -14%/top -24%).
 */
@Composable
fun HeroBackdrop(modifier: Modifier = Modifier, showGrid: Boolean = true) {
    Canvas(modifier) {
        val w = size.width
        val h = size.height
        if (showGrid && w > 600f) {
            val gridBrush = Brush.radialGradient(
                colors = listOf(
                    SekotaColors.Line2.copy(alpha = 1f),
                    SekotaColors.Line2.copy(alpha = 0.7f),
                    Color.Transparent,
                ),
                center = Offset(w * 0.55f, h * 0.45f),
                radius = w * 0.58f,
            )
            val step = 64.dp.toPx()
            var x = -step
            while (x < w + step) {
                drawLine(gridBrush, Offset(x, 0f), Offset(x, h), 1f)
                x += step
            }
            var y = -step
            while (y < h + step) {
                drawLine(gridBrush, Offset(0f, y), Offset(w, y), 1f)
                y += step
            }
        }

        val cyanX = w - w * 0.06f - 260.dp.toPx()
        val cyanY = h * 0.06f + 260.dp.toPx()
        glow(cyanX, cyanY, 305.dp.toPx(), SekotaColors.Cyan, 0.38f)

        val greenX = w + w * 0.04f - 190.dp.toPx()
        val greenY = h * 0.46f + 190.dp.toPx()
        glow(greenX, greenY, 235.dp.toPx(), SekotaColors.Green, 0.32f)

        val cornerX = -w * 0.14f + 210.dp.toPx()
        val cornerY = -h * 0.24f + 210.dp.toPx()
        glow(cornerX, cornerY, 255.dp.toPx(), SekotaColors.Green, 0.18f)
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.glow(
    x: Float,
    y: Float,
    radius: Float,
    color: Color,
    alpha: Float,
) {
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                color.copy(alpha = alpha),
                color.copy(alpha = alpha * 0.45f),
                Color.Transparent,
            ),
            center = Offset(x, y),
            radius = radius,
        ),
        radius = radius,
        center = Offset(x, y),
    )
}
