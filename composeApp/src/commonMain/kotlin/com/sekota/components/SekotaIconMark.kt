package com.sekota.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val BrandGreen = Color(0xFF60BD65)
private val BrandTeal = Color(0xFF02B6CF)
private val InkNavy = Color(0xFF0D1F2D)

@Composable
fun SekotaIconMark(
    size: Dp = 34.dp,
    tint: Color? = null,
    modifier: Modifier = Modifier
) {
    val paths = remember {
        listOf(
            PathParser().parsePathString("M107.78,84.84L107.86,99.54L119.26,92.94L119.18,78.24L107.78,84.84Z").toPath() to
                    Brush.linearGradient(
                        colors = listOf(BrandGreen, BrandTeal),
                        start = Offset(128.76f, 98.38f),
                        end = Offset(75.56f, 64.67f)
                    ),
            PathParser().parsePathString("M93.24,88.92L93.3,97.1L99.64,93.43L99.57,85.25L93.24,88.92Z").toPath() to
                    Brush.linearGradient(
                        colors = listOf(BrandGreen, BrandTeal),
                        start = Offset(104.91f, 96.46f),
                        end = Offset(75.31f, 77.69f)
                    ),
            PathParser().parsePathString("M86.34,70.26L86.43,84.69L97.61,78.22L97.52,63.79L86.34,70.26Z").toPath() to
                    Brush.linearGradient(
                        colors = listOf(BrandGreen, BrandTeal),
                        start = Offset(123.21f, 93.69f),
                        end = Offset(70.96f, 60.6f)
                    ),
            PathParser().parsePathString("M105.11,57.91L105.24,77.67L120.56,68.81L120.43,49.05L105.11,57.91Z").toPath() to
                    Brush.linearGradient(
                        colors = listOf(BrandGreen, BrandTeal),
                        start = Offset(145.81f, 83.9f),
                        end = Offset(74.31f, 38.58f)
                    ),
            PathParser().parsePathString("M128.18,26.69L88.11,3.75C85.85,2.45 83.41,1.53 80.93,0.91C74.04,-0.9 66.56,-0.01 60.23,3.6L19.64,26.84C11.19,31.67 6,40.59 6,50.25V58.23C6,60.75 6.13,63.25 6.34,65.73V96.42C6.34,106.26 11.64,115.36 20.24,120.3L48.96,136.74C50.38,137.66 51.78,138.56 53.27,139.39L60.25,143.38C62.62,144.73 65.14,145.67 67.75,146.27C67.75,146.27 67.75,146.27 67.75,146.29H67.77C69.77,146.71 71.82,146.99 73.87,146.99C76.26,146.99 78.63,146.67 80.93,146.07C83.41,145.46 85.82,144.54 88.11,143.23L128.18,120.3C136.78,115.36 142.08,106.26 142.08,96.42V50.57C142.08,40.72 136.78,31.62 128.18,26.69ZM81.93,13.19L122.54,36.43C127.51,39.29 130.6,44.54 130.6,50.25V96.72C130.6,102.42 127.54,107.7 122.54,110.54L81.93,133.77C81.21,134.18 80.48,134.5 79.73,134.8C79.86,115.11 75.27,60.48 22.59,38.69C23.56,37.75 24.64,36.92 25.82,36.24L65.9,13.34C68.46,11.87 71.35,11.14 74.21,11.14C75.5,11.14 76.8,11.31 78.07,11.61C79.4,11.97 80.7,12.49 81.93,13.19ZM17.51,65.24V50.57C17.51,49.93 17.57,49.28 17.63,48.67C40.97,58.08 56.61,75.66 64.09,101.05C66.52,109.32 67.68,117.2 68.22,123.76C65.27,120.38 62.36,116.99 59.54,113.5C53.7,106.29 48.14,98.83 43.21,90.97C42.24,89.41 40.26,88.82 38.58,89.56C42.15,99.56 46.7,109.19 51.61,118.61C53.4,121.98 55.25,125.34 57.13,128.65L54.54,127.17C33.32,113.25 19.66,90.33 17.48,65.26L17.51,65.24Z").toPath() to
                    Brush.linearGradient(
                        colors = listOf(BrandGreen, BrandTeal),
                        start = Offset(142.1f, 73.48f),
                        end = Offset(-12.49f, 73.48f)
                    )
        )
    }

    Canvas(
        modifier = modifier.size(size)
    ) {
        val scaleFactor = this.size.minDimension / 147f
        scale(scaleFactor, pivot = Offset.Zero) {
            for ((path, brush) in paths) {
                if (tint != null) {
                    drawPath(path, color = tint)
                } else {
                    drawPath(path, brush = brush)
                }
            }
        }
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun SekotaIconMarkDefaultGradientPreview() {
    MaterialTheme {
        Row(
            modifier = Modifier.padding(24.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SekotaIconMark(size = 34.dp)
            SekotaIconMark(size = 64.dp)
            SekotaIconMark(size = 120.dp)
        }
    }
}

@Preview(device = DESKTOP, showBackground = true, backgroundColor = 0xFF0D1F2D)
@Composable
fun SekotaIconMarkTintedPreview() {
    MaterialTheme {
        Row(
            modifier = Modifier
                .background(Color(0xFF0D1F2D))
                .padding(24.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // White monochrome on dark surface
            SekotaIconMark(size = 48.dp, tint = Color.White)
            // Brand Teal tinted
            SekotaIconMark(size = 48.dp, tint = BrandTeal)
            // Brand Green tinted
            SekotaIconMark(size = 48.dp, tint = BrandGreen)
        }
    }
}

@Composable
fun BookEditionVectorIcon(
    size: Dp = 16.dp,
    tint: Color = Color(0xFF00796B),
    modifier: Modifier = Modifier
) {
    val bookPath = remember {
        PathParser().parsePathString(
            "M18 2H6c-1.1 0-2 .9-2 2v16c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V4c0-1.1-.9-2-2-2zM6 4h5v8l-2.5-1.5L6 12V4zm12 16H6c-.55 0-1-.45-1-1s.45-1 1-1h12v2z"
        ).toPath()
    }

    Canvas(modifier = modifier.size(size)) {
        val scaleFactor = this.size.minDimension / 24f
        scale(scaleFactor, pivot = Offset.Zero) {
            drawPath(bookPath, color = tint)
        }
    }
}

@Composable
fun SekotaCheckmarkVectorIcon(
    size: Dp = 20.dp,
    tint: Color = Color(0xFF2E7D32),
    modifier: Modifier = Modifier
) {
    val checkPath = remember {
        PathParser().parsePathString(
            "M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z"
        ).toPath()
    }

    Canvas(modifier = modifier.size(size)) {
        val scaleFactor = this.size.minDimension / 24f
        scale(scaleFactor, pivot = Offset.Zero) {
            drawPath(checkPath, color = tint)
        }
    }
}

@Composable
fun SekotaAnalyticsVectorIcon(
    size: Dp = 16.dp,
    tint: Color = Color(0xFF00B5C8),
    modifier: Modifier = Modifier
) {
    val barPath = remember {
        PathParser().parsePathString(
            "M5 9.2h3V19H5zM10.6 5h2.8v14h-2.8zm5.6 8H19v6h-2.8z"
        ).toPath()
    }

    Canvas(modifier = modifier.size(size)) {
        val scaleFactor = this.size.minDimension / 24f
        scale(scaleFactor, pivot = Offset.Zero) {
            drawPath(barPath, color = tint)
        }
    }
}

@Composable
fun SekotaArrowUpVectorIcon(
    size: Dp = 16.dp,
    tint: Color = InkNavy,
    modifier: Modifier = Modifier
) {
    val upPath = remember {
        PathParser().parsePathString("M7.41 15.41L12 10.83l4.59 4.58L18 14l-6-6-6 6z").toPath()
    }
    Canvas(modifier = modifier.size(size)) {
        val scaleFactor = this.size.minDimension / 24f
        scale(scaleFactor, pivot = Offset.Zero) {
            drawPath(upPath, color = tint)
        }
    }
}

@Composable
fun SekotaArrowDownVectorIcon(
    size: Dp = 16.dp,
    tint: Color = InkNavy,
    modifier: Modifier = Modifier
) {
    val downPath = remember {
        PathParser().parsePathString("M7.41 8.59L12 13.17l4.59-4.58L18 10l-6 6-6-6z").toPath()
    }
    Canvas(modifier = modifier.size(size)) {
        val scaleFactor = this.size.minDimension / 24f
        scale(scaleFactor, pivot = Offset.Zero) {
            drawPath(downPath, color = tint)
        }
    }
}

@Composable
fun SekotaTrashVectorIcon(
    size: Dp = 16.dp,
    tint: Color = Color(0xFFE53E3E),
    modifier: Modifier = Modifier
) {
    val trashPath = remember {
        PathParser().parsePathString("M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z").toPath()
    }
    Canvas(modifier = modifier.size(size)) {
        val scaleFactor = this.size.minDimension / 24f
        scale(scaleFactor, pivot = Offset.Zero) {
            drawPath(trashPath, color = tint)
        }
    }
}

@Composable
fun SekotaWarningVectorIcon(
    size: Dp = 16.dp,
    tint: Color = Color(0xFFC62828),
    modifier: Modifier = Modifier
) {
    val warnPath = remember {
        PathParser().parsePathString("M1 21h22L12 2 1 21zm12-3h-2v-2h2v2zm0-4h-2v-4h2v4z").toPath()
    }
    Canvas(modifier = modifier.size(size)) {
        val scaleFactor = this.size.minDimension / 24f
        scale(scaleFactor, pivot = Offset.Zero) {
            drawPath(warnPath, color = tint)
        }
    }
}

@Composable
fun SekotaInfoVectorIcon(
    size: Dp = 16.dp,
    tint: Color = InkNavy,
    modifier: Modifier = Modifier
) {
    val infoPath = remember {
        PathParser().parsePathString("M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-6h2v6zm0-8h-2V7h2v2z").toPath()
    }
    Canvas(modifier = modifier.size(size)) {
        val scaleFactor = this.size.minDimension / 24f
        scale(scaleFactor, pivot = Offset.Zero) {
            drawPath(infoPath, color = tint)
        }
    }
}

@Composable
fun SekotaShieldCheckVectorIcon(
    size: Dp = 16.dp,
    tint: Color = Color(0xFFC62828),
    modifier: Modifier = Modifier
) {
    val shieldCheckPath = remember {
        PathParser().parsePathString("M12 1L3 5v6c0 5.55 3.84 10.74 9 12 5.16-1.26 9-6.45 9-12V5l-9-4zm-2 16l-4-4 1.41-1.41L10 14.17l6.59-6.59L18 9l-8 8z").toPath()
    }
    Canvas(modifier = modifier.size(size)) {
        val scaleFactor = this.size.minDimension / 24f
        scale(scaleFactor, pivot = Offset.Zero) {
            drawPath(shieldCheckPath, color = tint)
        }
    }
}

@Composable
fun SekotaCmsNavVectorIcon(
    index: Int,
    size: Dp = 20.dp,
    tint: Color = Color.LightGray,
    modifier: Modifier = Modifier
) {
    val pathData = remember(index) {
        when (index) {
            0 -> "M18 2H6c-1.1 0-2 .9-2 2v16c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V4c0-1.1-.9-2-2-2zM6 4h5v8l-2.5-1.5L6 12V4z" // Book / Product Registry
            1 -> "M12 4V1L8 5l4 4V6c3.31 0 6 2.69 6 6 0 1.01-.25 1.97-.7 2.8l1.46 1.46C19.54 15.03 20 13.57 20 12c0-4.42-3.58-8-8-8zm0 14c-3.31 0-6-2.69-6-6 0-1.01.25-1.97.7-2.8L5.24 7.74C4.46 8.97 4 10.43 4 12c0 4.42 3.58 8 8 8v3l4-4-4-4v3z" // Refresh / Brand Story Loop
            2 -> "M3 13h18v-2H3v2zm0-7h18V4H3v2zm0 14h18v-2H3v2z" // Site Configuration / AppBar
            3 -> "M19 3H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zM9 17H7v-7h2v7zm4 0h-2V7h2v10zm4 0h-2v-4h2v4z" // Analytics / Insights & Feedback
            4 -> "M12 4V1L8 5l4 4V6c3.31 0 6 2.69 6 6 0 1.01-.25 1.97-.7 2.8l1.46 1.46C19.54 15.03 20 13.57 20 12c0-4.42-3.58-8-8-8zm0 14c-3.31 0-6-2.69-6-6 0-1.01.25-1.97.7-2.8L5.24 7.74C4.46 8.97 4 10.43 4 12c0 4.42 3.58 8 8 8v3l4-4-4-4v3z" // Legacy Loop
            5 -> "M9 21c0 .55.45 1 1 1h4c.55 0 1-.45 1-1v-1H9v1zm3-19C8.14 2 5 5.14 5 9c0 2.38 1.19 4.47 3 5.74V17c0 .55.45 1 1 1h6c.55 0 1-.45 1-1v-2.26c1.81-1.27 3-3.36 3-5.74 0-3.86-3.14-7-7-7z" // Legacy Lightbulb
            6 -> "M20 4H4c-1.1 0-1.99.9-1.99 2L2 18c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2zm0 4l-8 5-8-5V6l8 5 8-5v2z" // Legacy Mail
            7 -> "M4 17h16v2H4v-2zm0-14h16c1.1 0 2 .9 2 2v10c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V5c0-1.1.9-2 2-2zm0 2v10h16V5H4z" // Legacy Footer
            8 -> "M3 13h18v-2H3v2zm0-7h18V4H3v2zm0 14h18v-2H3v2z" // Legacy AppBar
            else -> "M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2z"
        }
    }
    val iconPath = remember(pathData) {
        PathParser().parsePathString(pathData).toPath()
    }
    Canvas(modifier = modifier.size(size)) {
        val scaleFactor = this.size.minDimension / 24f
        scale(scaleFactor, pivot = Offset.Zero) {
            drawPath(iconPath, color = tint)
        }
    }
}

@Composable
fun SekotaExitVectorIcon(
    size: Dp = 18.dp,
    tint: Color = Color(0xFFFF8A80),
    modifier: Modifier = Modifier
) {
    val exitPath = remember {
        PathParser().parsePathString("M10.09 15.59L11.5 17l5-5-5-5-1.41 1.41L12.67 11H3v2h9.67l-2.58 2.59zM19 3H5c-1.11 0-2 .9-2 2v4h2V5h14v14H5v-4H3v4c0 1.1.89 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2z").toPath()
    }
    Canvas(modifier = modifier.size(size)) {
        val scaleFactor = this.size.minDimension / 24f
        scale(scaleFactor, pivot = Offset.Zero) {
            drawPath(exitPath, color = tint)
        }
    }
}

@Composable
fun SekotaSaveVectorIcon(
    size: Dp = 16.dp,
    tint: Color = Color.White,
    modifier: Modifier = Modifier
) {
    val savePath = remember {
        PathParser().parsePathString("M17 3H5c-1.11 0-2 .9-2 2v14c0 1.1.89 2 2 2h14c1.1 0 2-.9 2-2V7l-4-4zm-5 16c-1.66 0-3-1.34-3-3s1.34-3 3-3 3 1.34 3 3-1.34 3-3 3zm3-10H5V5h10v4z").toPath()
    }
    Canvas(modifier = modifier.size(size)) {
        val scaleFactor = this.size.minDimension / 24f
        scale(scaleFactor, pivot = Offset.Zero) {
            drawPath(savePath, color = tint)
        }
    }
}

@Composable
fun SekotaRefreshVectorIcon(
    size: Dp = 16.dp,
    tint: Color = InkNavy,
    modifier: Modifier = Modifier
) {
    val refreshPath = remember {
        PathParser().parsePathString("M17.65 6.35C16.2 4.9 14.21 4 12 4c-4.42 0-7.99 3.58-7.99 8s3.57 8 7.99 8c3.73 0 6.84-2.55 7.73-6h-2.08c-.82 2.33-3.04 4-5.65 4-3.31 0-6-2.69-6-6s2.69-6 6-6c1.66 0 3.14.69 4.22 1.78L13 11h7V4l-2.35 2.35z").toPath()
    }
    Canvas(modifier = modifier.size(size)) {
        val scaleFactor = this.size.minDimension / 24f
        scale(scaleFactor, pivot = Offset.Zero) {
            drawPath(refreshPath, color = tint)
        }
    }
}

@Composable
fun SekotaCloseVectorIcon(
    size: Dp = 16.dp,
    tint: Color = Color(0xFF64748B),
    modifier: Modifier = Modifier
) {
    val closePath = remember {
        PathParser().parsePathString("M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z").toPath()
    }
    Canvas(modifier = modifier.size(size)) {
        val scaleFactor = this.size.minDimension / 24f
        scale(scaleFactor, pivot = Offset.Zero) {
            drawPath(closePath, color = tint)
        }
    }
}

@Composable
fun SekotaPlusVectorIcon(
    size: Dp = 16.dp,
    tint: Color = Color.White,
    modifier: Modifier = Modifier
) {
    val plusPath = remember {
        PathParser().parsePathString("M19 13h-6v6h-2v-6H5v-2h6V5h2v6h6v2z").toPath()
    }
    Canvas(modifier = modifier.size(size)) {
        val scaleFactor = this.size.minDimension / 24f
        scale(scaleFactor, pivot = Offset.Zero) {
            drawPath(plusPath, color = tint)
        }
    }
}

@Composable
fun SekotaGearVectorIcon(
    size: Dp = 20.dp,
    tint: Color = Color(0xFF9333EA),
    modifier: Modifier = Modifier
) {
    val gearPath = remember {
        PathParser().parsePathString("M19.14 12.94c.04-.3.06-.61.06-.94 0-.32-.02-.64-.07-.94l2.03-1.58c.18-.14.23-.41.12-.61l-1.92-3.32c-.12-.22-.37-.29-.59-.22l-2.39.96c-.5-.38-1.03-.7-1.62-.94l-.36-2.54c-.04-.24-.24-.41-.48-.41h-3.84c-.24 0-.43.17-.47.41l-.36 2.54c-.59.24-1.13.57-1.62.94l-2.39-.96c-.22-.08-.47 0-.59.22L2.74 8.87c-.12.21-.08.47.12.61l2.03 1.58c-.05.3-.09.63-.09.94s.02.64.07.94l-2.03 1.58c-.18.14-.23.41-.12.61l1.92 3.32c.12.22.37.29.59.22l2.39-.96c.5.38 1.03.7 1.62.94l.36 2.54c.05.24.24.41.48.41h3.84c.24 0 .44-.17.47-.41l.36-2.54c.59-.24 1.13-.56 1.62-.94l2.39.96c.22.08.47 0 .59-.22l1.92-3.32c.12-.22.07-.47-.12-.61l-2.01-1.58zM12 15.6c-1.98 0-3.6-1.62-3.6-3.6s1.62-3.6 3.6-3.6 3.6 1.62 3.6 3.6-1.62 3.6-3.6 3.6z").toPath()
    }
    Canvas(modifier = modifier.size(size)) {
        val scaleFactor = this.size.minDimension / 24f
        scale(scaleFactor, pivot = Offset.Zero) {
            drawPath(gearPath, color = tint)
        }
    }
}

@Composable
fun SekotaApparelVectorIcon(
    size: Dp = 20.dp,
    tint: Color = Color(0xFF2563EB),
    modifier: Modifier = Modifier
) {
    val apparelPath = remember {
        PathParser().parsePathString("M16 2l-4 3-4-3-6 4 3 4 3-1v12h12V8l3 1 3-4-7-4zm-4 4.5l2-1.5 2 1.5-2 2-2-2z").toPath()
    }
    Canvas(modifier = modifier.size(size)) {
        val scaleFactor = this.size.minDimension / 24f
        scale(scaleFactor, pivot = Offset.Zero) {
            drawPath(apparelPath, color = tint)
        }
    }
}

@Composable
fun SekotaSocialVectorIcon(
    platform: String,
    size: Dp = 14.dp,
    tint: Color = Color(0xFF94A3B8),
    modifier: Modifier = Modifier
) {
    val pathData = remember(platform) {
        when (platform.trim().uppercase()) {
            "LINKEDIN" -> "M19 3a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h14m-.5 15.5v-5.3a3.26 3.26 0 0 0-3.26-3.26c-.85 0-1.84.52-2.28 1.3v-1.11h-2.79v8.37h2.79v-4.93c0-.77.62-1.4 1.39-1.4a1.4 1.4 0 0 1 1.4 1.4v4.93h2.75M6.46 10.9v8.37H9.2V10.9H6.46M7.83 6.64a1.66 1.66 0 0 0-1.66 1.66c0 .92.74 1.66 1.66 1.66.92 0 1.66-.74 1.66-1.66 0-.92-.74-1.66-1.66-1.66z"
            "TWITTER", "X" -> "M18.244 2.25h3.308l-7.227 8.26 8.502 11.24H16.17l-5.214-6.817L4.99 21.75H1.68l7.73-8.835L1.254 2.25H8.08l4.713 6.231zm-1.161 17.52h1.833L7.084 4.126H5.117z"
            "INSTAGRAM" -> "M12 2.163c3.204 0 3.584.012 4.85.07 3.252.148 4.771 1.691 4.919 4.919.058 1.265.069 1.645.069 4.849 0 3.205-.012 3.584-.069 4.849-.149 3.225-1.664 4.771-4.919 4.919-1.266.058-1.644.07-4.85.07-3.204 0-3.584-.012-4.849-.07-3.26-.149-4.771-1.699-4.919-4.92-.058-1.265-.07-1.644-.07-4.849 0-3.204.013-3.583.07-4.849.149-3.227 1.664-4.771 4.919-4.919 1.266-.057 1.645-.069 4.849-.069zm0-2.163c-3.259 0-3.667.014-4.947.072-4.358.2-6.78 2.618-6.98 6.98-.059 1.281-.073 1.689-.073 4.948 0 3.259.014 3.668.072 4.948.2 4.358 2.618 6.78 6.98 6.98 1.281.058 1.689.072 4.948.072 3.259 0 3.668-.014 4.948-.072 4.354-.2 6.782-2.618 6.979-6.98.059-1.28.073-1.689.073-4.948 0-3.259-.014-3.667-.072-4.947-.196-4.354-2.617-6.78-6.979-6.98-1.281-.059-1.69-.073-4.949-.073zm0 5.838c-3.403 0-6.162 2.759-6.162 6.162s2.759 6.163 6.162 6.163 6.162-2.759 6.162-6.163c0-3.403-2.759-6.162-6.162-6.162zm0 10.162c-2.209 0-4-1.79-4-4 0-2.209 1.791-4 4-4s4 1.791 4 4c0 2.21-1.791 4-4 4zm6.406-11.845c-.796 0-1.441.645-1.441 1.44s.645 1.44 1.441 1.44c.795 0 1.439-.645 1.439-1.44s-.644-1.44-1.439-1.44z"
            else -> "M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-1 17.93c-3.95-.49-7-3.85-7-7.93 0-.62.08-1.21.21-1.79L9 15v1c0 1.1.9 2 2 2v1.93zm6.9-2.54c-.26-.81-1-1.39-1.9-1.39h-1v-3c0-.55-.45-1-1-1H8v-2h2c.55 0 1-.45 1-1V7h2c1.1 0 2-.9 2-2v-.41c2.93 1.19 5 4.06 5 7.41 0 2.08-.8 3.97-2.1 5.39z"
        }
    }
    val socialPath = remember(pathData) {
        PathParser().parsePathString(pathData).toPath()
    }
    Canvas(modifier = modifier.size(size)) {
        val scaleFactor = this.size.minDimension / 24f
        scale(scaleFactor, pivot = Offset.Zero) {
            drawPath(socialPath, color = tint)
        }
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun BookEditionVectorIconPreview() {
    MaterialTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BookEditionVectorIcon(size = 16.dp)
            BookEditionVectorIcon(size = 24.dp)
            BookEditionVectorIcon(size = 32.dp, tint = Color(0xFF00B5C8))
            SekotaCheckmarkVectorIcon(size = 24.dp)
            SekotaAnalyticsVectorIcon(size = 24.dp)
            SekotaArrowUpVectorIcon(size = 20.dp)
            SekotaArrowDownVectorIcon(size = 20.dp)
            SekotaTrashVectorIcon(size = 20.dp)
            SekotaSaveVectorIcon(size = 20.dp, tint = Color.Black)
            SekotaRefreshVectorIcon(size = 20.dp)
            SekotaCloseVectorIcon(size = 20.dp)
            SekotaPlusVectorIcon(size = 20.dp, tint = Color.Black)
            SekotaGearVectorIcon(size = 20.dp)
            SekotaApparelVectorIcon(size = 20.dp)
            SekotaSocialVectorIcon(platform = "LINKEDIN", size = 20.dp)
            SekotaSocialVectorIcon(platform = "X", size = 20.dp)
            SekotaSocialVectorIcon(platform = "INSTAGRAM", size = 20.dp)
        }
    }
}




