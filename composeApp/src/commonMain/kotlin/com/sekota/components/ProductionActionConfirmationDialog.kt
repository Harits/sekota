package com.sekota.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.sekota.getDmSansFontFamily
import com.sekota.getMontserratFontFamily

@Composable
fun ProductionActionConfirmationDialog(
    actionName: String,
    entityName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    isCriticalDelete: Boolean = false
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            tonalElevation = 6.dp,
            border = BorderStroke(1.5.dp, if (isCriticalDelete) Color(0xFFEF5350) else Color(0xFFFFB74D)),
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .widthIn(max = 480.dp)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    color = if (isCriticalDelete) Color(0xFFFFEBEE) else Color(0xFFFFF3E0),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.size(56.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        if (isCriticalDelete) {
                            SekotaWarningVectorIcon(size = 28.dp, tint = Color(0xFFD32F2F))
                        } else {
                            SekotaShieldCheckVectorIcon(size = 28.dp, tint = Color(0xFFE65100))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (isCriticalDelete) "Konfirmasi Penghapusan Live" else "Konfirmasi Aksi Production",
                    fontFamily = getMontserratFontFamily(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF0F172A)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Anda sedang beroperasi pada lingkungan PRODUCTION aktif. Aksi $actionName untuk $entityName akan langsung berdampak kepada seluruh pembaca dan pengguna live web sekota.",
                    fontFamily = getDmSansFontFamily(),
                    fontSize = 13.sp,
                    lineHeight = 20.sp,
                    color = Color(0xFF64748B)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    color = Color(0xFFF8FAFC),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(if (isCriticalDelete) Color(0xFFD32F2F) else Color(0xFF0288D1), RoundedCornerShape(50))
                        )
                        Text(
                            text = if (isCriticalDelete) "Perhatian: Tindakan ini permanen dan tidak dapat dibatalkan."
                            else "Perubahan akan disiarkan seketika via WebSocket Sync ke seluruh klien publik.",
                            fontFamily = getDmSansFontFamily(),
                            fontSize = 11.sp,
                            color = Color(0xFF475569)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(100.dp),
                        modifier = Modifier.weight(1f).height(44.dp)
                    ) {
                        Text(
                            text = "Batal",
                            fontFamily = getDmSansFontFamily(),
                            color = Color(0xFF0F172A)
                        )
                    }

                    Button(
                        onClick = {
                            onConfirm()
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isCriticalDelete) Color(0xFFD32F2F) else Color(0xFF00B5C8)
                        ),
                        shape = RoundedCornerShape(100.dp),
                        modifier = Modifier.weight(1.3f).height(44.dp)
                    ) {
                        Text(
                            text = if (isCriticalDelete) "Hapus Live" else "Lanjutkan Simpan",
                            fontFamily = getDmSansFontFamily(),
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun ProductionActionConfirmationSavePreview() {
    MaterialTheme {
        ProductionActionConfirmationDialog(
            actionName = "Simpan Perubahan",
            entityName = "Koleksi Buku & E-Book",
            onConfirm = {},
            onDismiss = {},
            isCriticalDelete = false
        )
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun ProductionActionConfirmationDeletePreview() {
    MaterialTheme {
        ProductionActionConfirmationDialog(
            actionName = "Hapus Entitas",
            entityName = "Produk Intelligence Suite 'Veridia'",
            onConfirm = {},
            onDismiss = {},
            isCriticalDelete = true
        )
    }
}
