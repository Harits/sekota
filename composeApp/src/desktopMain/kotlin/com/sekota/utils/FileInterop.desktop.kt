package com.sekota.utils

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image
import java.util.Base64
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

actual fun decodeImageToBitmap(bytes: ByteArray): ImageBitmap? {
    return try {
        Image.makeFromEncoded(bytes).toComposeImageBitmap()
    } catch (_: Exception) {
        null
    }
}

actual fun selectAndReadImageFile(onFileRead: (String) -> Unit) {
    val fileChooser = JFileChooser().apply {
        fileFilter = FileNameExtensionFilter("Image files", "png", "jpg", "jpeg", "gif", "webp")
        dialogTitle = "Pilih Gambar"
    }
    if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        try {
            val file = fileChooser.selectedFile
            val bytes = file.readBytes()
            val base64String = Base64.getEncoder().encodeToString(bytes)
            val ext = file.extension.lowercase()
            val mimeType = when (ext) {
                "png" -> "image/png"
                "gif" -> "image/gif"
                "webp" -> "image/webp"
                else -> "image/jpeg"
            }
            onFileRead("data:$mimeType;base64,$base64String")
        } catch (e: Exception) {
            println("Desktop Image Pick Error: ${e.message}")
        }
    }
}
