package com.sekota.components.cms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sekota.getDmSansFontFamily
import com.sekota.getMontserratFontFamily
import com.sekota.ui.WindowWidth

val CmsInkNavy = Color(0xFF0D1F2D)
val CmsBrandTeal = Color(0xFF00B5C8)
val CmsBorderColor = Color(0xFFE2E8F0)
val CmsLabelColor = Color(0xFF475569)

/**
 * Reusable Atomic Input Field with standardized Sekota CMS typography, colors, and corner styling.
 */
@Composable
fun CmsInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    isSingleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (isSingleLine) 1 else 5,
    readOnly: Boolean = false,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = label.uppercase(),
            fontFamily = getDmSansFontFamily(),
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            letterSpacing = 0.5.sp,
            color = if (isError) Color(0xFFE53E3E) else CmsLabelColor
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = if (placeholder.isNotEmpty()) {
                {
                    Text(
                        text = placeholder,
                        fontFamily = getDmSansFontFamily(),
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }
            } else null,
            readOnly = readOnly,
            isError = isError,
            singleLine = isSingleLine,
            minLines = minLines,
            maxLines = maxLines,
            textStyle = androidx.compose.ui.text.TextStyle(
                fontFamily = getDmSansFontFamily(),
                fontSize = 13.sp,
                color = CmsInkNavy
            ),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CmsBrandTeal,
                unfocusedBorderColor = CmsBorderColor,
                focusedLabelColor = CmsBrandTeal,
                errorBorderColor = Color(0xFFE53E3E),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color(0xFFFBFBFB)
            ),
            modifier = Modifier.fillMaxWidth()
        )
        if (isError && !errorMessage.isNullOrEmpty()) {
            Text(
                text = errorMessage,
                fontFamily = getDmSansFontFamily(),
                fontSize = 11.sp,
                color = Color(0xFFE53E3E)
            )
        }
    }
}

/**
 * Standardized Section & Workbench Action Header for CMS.
 * Responsive between Compact (Mobile) and Expanded (Desktop/Tablet).
 */
@Composable
fun CmsActionHeader(
    title: String,
    subtitle: String,
    windowWidth: WindowWidth = WindowWidth.Expanded,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    secondaryLabel: String? = null,
    onSecondaryAction: (() -> Unit)? = null
) {
    val isCompact = windowWidth.isCompact
    val heading = @Composable { m: Modifier ->
        Column(modifier = m) {
            Text(
                text = title,
                fontFamily = getMontserratFontFamily(),
                fontWeight = FontWeight.Bold,
                fontSize = if (isCompact) 22.sp else 28.sp,
                lineHeight = if (isCompact) 28.sp else 36.sp,
                color = CmsInkNavy
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontFamily = getDmSansFontFamily(),
                color = Color.Gray,
                fontSize = if (isCompact) 12.sp else 13.sp
            )
        }
    }

    val actions = @Composable { m: Modifier ->
        Row(
            modifier = m,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (secondaryLabel != null && onSecondaryAction != null) {
                OutlinedButton(
                    onClick = onSecondaryAction,
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.dp, CmsBorderColor),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CmsInkNavy),
                    modifier = if (isCompact) Modifier.weight(1f) else Modifier
                ) {
                    Text(
                        text = secondaryLabel,
                        fontFamily = getDmSansFontFamily(),
                        fontSize = 13.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            if (actionLabel != null && onAction != null) {
                Button(
                    onClick = onAction,
                    colors = ButtonDefaults.buttonColors(containerColor = CmsBrandTeal),
                    shape = RoundedCornerShape(50),
                    modifier = if (isCompact) Modifier.weight(1f) else Modifier
                ) {
                    Text("+", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = actionLabel,
                        fontFamily = getDmSansFontFamily(),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }

    if (isCompact) {
        Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            heading(Modifier.fillMaxWidth())
            if (actionLabel != null || secondaryLabel != null) {
                actions(Modifier.fillMaxWidth())
            }
        }
    } else {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            heading(Modifier.weight(1f).padding(end = 16.dp))
            if (actionLabel != null || secondaryLabel != null) {
                actions(Modifier)
            }
        }
    }
}

// -------------------------------------------------------------
// Compose Desktop & Multi-State Previews
// -------------------------------------------------------------

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun CmsInputFieldDefaultPreview() {
    MaterialTheme {
        Column(modifier = Modifier.padding(24.dp).width(400.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            CmsInputField(
                value = "Smart Governance & Energy",
                onValueChange = {},
                label = "Judul Produk"
            )
            CmsInputField(
                value = "",
                onValueChange = {},
                label = "Deskripsi",
                placeholder = "Masukkan ringkasan modul...",
                isSingleLine = false,
                minLines = 3
            )
            CmsInputField(
                value = "Invalid Url Input",
                onValueChange = {},
                label = "PDF Reader URL",
                isError = true,
                errorMessage = "Format URL tidak valid. Harus diawali https://"
            )
        }
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun CmsActionHeaderDesktopPreview() {
    MaterialTheme {
        Column(modifier = Modifier.padding(24.dp).fillMaxWidth()) {
            CmsActionHeader(
                title = "Product Registry",
                subtitle = "Kelola katalog buku, intelligence suite, dan merchandise dalam satu repositori terpadu.",
                windowWidth = WindowWidth.Expanded,
                actionLabel = "Tambah Produk Baru",
                onAction = {},
                secondaryLabel = "Sync Status",
                onSecondaryAction = {}
            )
        }
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun CmsActionHeaderCompactPreview() {
    MaterialTheme {
        Column(modifier = Modifier.padding(16.dp).width(360.dp)) {
            CmsActionHeader(
                title = "Product Registry",
                subtitle = "Kelola katalog terpadu Sekota.",
                windowWidth = WindowWidth.Compact,
                actionLabel = "Tambah Item",
                onAction = {},
                secondaryLabel = "Refresh",
                onSecondaryAction = {}
            )
        }
    }
}
