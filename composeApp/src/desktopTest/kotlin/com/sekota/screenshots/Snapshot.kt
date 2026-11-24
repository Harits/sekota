package com.sekota.screenshots

import androidx.compose.runtime.Composable
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.unit.Density
import org.jetbrains.skia.EncodedImageFormat
import java.io.File

/**
 * Renders a composable off-screen with Skiko and writes a PNG to
 * `composeApp/build/screenshots/<name>.png` (visual review for Sprint 7).
 *
 * Several frames are rendered so asynchronously loaded fonts and drawables
 * from compose resources are in place before the final capture.
 */
fun snapshot(name: String, width: Int, height: Int, content: @Composable () -> Unit): File {
    val scene = ImageComposeScene(width = width, height = height, density = Density(1f), content = content)
    try {
        var time = 0L
        repeat(30) {
            scene.render(time)
            time += 100_000_000L // 100 ms per frame
            Thread.sleep(40)
        }
        val image = scene.render(time)
        val bytes = image.encodeToData(EncodedImageFormat.PNG)?.bytes ?: error("PNG encoding failed for $name")
        val dir = File(System.getProperty("user.dir"), "build/screenshots").apply { mkdirs() }
        return File(dir, "$name.png").apply { writeBytes(bytes) }
    } finally {
        scene.close()
    }
}
