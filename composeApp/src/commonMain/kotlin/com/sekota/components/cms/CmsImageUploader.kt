package com.sekota.components.cms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sekota.getDmSansFontFamily
import com.sekota.getMontserratFontFamily
import com.sekota.utils.decodeBase64ToBitmap
import com.sekota.utils.selectAndReadImageFile
import kotlinx.coroutines.launch

/**
 * Standardized Multiplatform Image Uploader & Media Preview Component.
 * Supports Base64 data URI, raw Base64, and local image selection across Desktop and Wasm.
 */
@Composable
fun CmsImageUploader(
    imageBase64: String,
    onImageSelected: (String) -> Unit,
    label: String = "Media Cover / Asset Image",
    aspectRatio: Float = 16f / 9f,
    modifier: Modifier = Modifier,
    isReadOnly: Boolean = false,
    helperText: String = "Klik untuk memilih gambar dari perangkat (JPG / PNG, disarankan < 1MB)."
) {
    val scope = rememberCoroutineScope()
    var isUploading by remember { mutableStateOf(false) }

    val decodedBitmap: ImageBitmap? = remember(imageBase64) {
        if (imageBase64.isNotBlank()) {
            val cleanBase64 = if (imageBase64.contains(",")) {
                imageBase64.substringAfter(",")
            } else {
                imageBase64
            }
            try {
                decodeBase64ToBitmap(cleanBase64)
            } catch (e: Exception) {
                null
            }
        } else null
    }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = label.uppercase(),
            fontFamily = getDmSansFontFamily(),
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            letterSpacing = 0.5.sp,
            color = CmsLabelColor
        )

        Surface(
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, if (decodedBitmap != null) CmsBrandTeal else CmsBorderColor),
            color = Color(0xFFF8FAFC),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(aspectRatio)
                .clip(RoundedCornerShape(10.dp))
                .clickable(enabled = !isReadOnly && !isUploading) {
                    isUploading = true
                    try {
                        selectAndReadImageFile { base64Data ->
                            onImageSelected(base64Data)
                            isUploading = false
                        }
                    } catch (e: Exception) {
                        isUploading = false
                    }
                }
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (decodedBitmap != null) {
                    Image(
                        bitmap = decodedBitmap,
                        contentDescription = label,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    if (!isReadOnly) {
                        Surface(
                            color = Color.Black.copy(alpha = 0.55f),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(10.dp)
                        ) {
                            Text(
                                text = "Ganti Gambar",
                                fontFamily = getDmSansFontFamily(),
                                fontSize = 11.sp,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Surface(
                            color = Color(0xFFE2E8F0),
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.size(42.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = if (isUploading) "\u23F3" else "\uD83D\uDDBC",
                                    fontSize = 18.sp
                                )
                            }
                        }

                        Text(
                            text = if (isUploading) "Memproses File..." else "Unggah Gambar",
                            fontFamily = getMontserratFontFamily(),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            color = CmsInkNavy
                        )

                        Text(
                            text = helperText,
                            fontFamily = getDmSansFontFamily(),
                            fontSize = 11.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                if (isUploading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White.copy(alpha = 0.7f)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = CmsBrandTeal,
                            strokeWidth = 3.dp,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Previews
// -------------------------------------------------------------

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun CmsImageUploaderEmptyPreview() {
    MaterialTheme {
        Column(modifier = Modifier.padding(24.dp).width(360.dp)) {
            CmsImageUploader(
                imageBase64 = "",
                onImageSelected = {},
                label = "Cover Buku"
            )
        }
    }
}
