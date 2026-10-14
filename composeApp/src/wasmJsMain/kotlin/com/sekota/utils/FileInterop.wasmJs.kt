package com.sekota.utils

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.browser.document
import org.jetbrains.skia.Image
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import org.w3c.files.FileReader
import org.w3c.files.get

actual fun decodeImageToBitmap(bytes: ByteArray): ImageBitmap? {
    return try {
        Image.makeFromEncoded(bytes).toComposeImageBitmap()
    } catch (_: Exception) {
        null
    }
}

@OptIn(kotlin.js.ExperimentalWasmJsInterop::class)
actual fun selectAndReadImageFile(onFileRead: (String) -> Unit, onCancel: () -> Unit) {
    val fileInput = document.createElement("input") as HTMLInputElement
    fileInput.type = "file"
    fileInput.accept = "image/*"

    var handled = false
    fileInput.addEventListener("change", { _: Event ->
        handled = true
        val file = fileInput.files?.get(0)
        if (file != null) {
            val reader = FileReader()
            reader.onload = {
                val base64 = reader.result.toString()
                onFileRead(base64)
            }
            reader.onerror = {
                onCancel()
            }
            reader.readAsDataURL(file)
        } else {
            onCancel()
        }
    })

    fileInput.addEventListener("cancel", { _: Event ->
        handled = true
        onCancel()
    })

    fileInput.click()
}
