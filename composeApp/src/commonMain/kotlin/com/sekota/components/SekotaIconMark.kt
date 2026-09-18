package com.sekota.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val BrandGreen = Color(0xFF60BD65)
private val BrandTeal = Color(0xFF02B6CF)

@Composable
fun SekotaIconMark(
    size: Dp = 34.dp,
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
                drawPath(path, brush = brush)
            }
        }
    }
}
