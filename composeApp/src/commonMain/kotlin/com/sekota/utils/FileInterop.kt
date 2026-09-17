package com.sekota.utils

import androidx.compose.ui.graphics.ImageBitmap
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

/**
 * Platform-agnostic file picker for selecting an image file.
 * Returns the data URI / Base64 string of the selected image.
 */
expect fun selectAndReadImageFile(onFileRead: (String) -> Unit)

/**
 * Platform-agnostic image decoder for Compose Multiplatform.
 */
expect fun decodeImageToBitmap(bytes: ByteArray): ImageBitmap?

/**
 * Helper to decode a Base64 string (with or without data URI prefix) into a Compose ImageBitmap.
 */
@OptIn(ExperimentalEncodingApi::class)
fun decodeBase64ToBitmap(dataUriOrBase64: String): ImageBitmap? {
    return try {
        val pureBase64 = if (dataUriOrBase64.contains("base64,")) {
            dataUriOrBase64.substringAfter("base64,")
        } else {
            dataUriOrBase64
        }.trim()
        val bytes = Base64.decode(pureBase64)
        decodeImageToBitmap(bytes)
    } catch (_: Exception) {
        null
    }
}
