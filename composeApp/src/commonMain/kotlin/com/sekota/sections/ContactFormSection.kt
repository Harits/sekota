package com.sekota.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.sekota.*
import com.sekota.features.admin.domain.model.StrategicConversationConfig
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import sekota.composeapp.generated.resources.Res
import sekota.composeapp.generated.resources.*

import com.sekota.ui.contentHorizontalPadding
import com.sekota.ui.windowWidthOf
import com.sekota.ui.sectionVerticalPadding

@Composable
fun ContactFormSection(
    config: StrategicConversationConfig? = null
) {
    var liveConfig by remember { mutableStateOf(config ?: StrategicConversationConfig()) }

    LaunchedEffect(config) {
        if (config != null) {
            liveConfig = config
        } else {
            liveConfig = com.sekota.getStrategicConversationUseCase()
            com.sekota.syncService.syncEventFlow.collect {
                liveConfig = com.sekota.getStrategicConversationUseCase()
            }
        }
    }

    BoxWithConstraints {
        val windowWidth = windowWidthOf(maxWidth)
        val isCompact = windowWidth.isAtMostMedium
        val horizontalPadding = contentHorizontalPadding(maxWidth)
        val verticalPadding = if (isCompact) 60.dp else 120.dp

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF8FAFB))
        ) {
            if (isCompact) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = verticalPadding, horizontal = horizontalPadding),
                    verticalArrangement = Arrangement.spacedBy(48.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = liveConfig.title,
                            fontSize = 36.sp,
                            lineHeight = 44.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = getMontserratFontFamily(),
                            color = Color(0xFF0F172A)
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = liveConfig.subtitle,
                            fontSize = 18.sp,
                            lineHeight = 28.sp,
                            color = Color(0xFF64748B),
                            fontFamily = getDmSansFontFamily()
                        )
                        Spacer(modifier = Modifier.height(48.dp))
                        
                        ContactInfoItem(
                            icon = Res.drawable.map_pin,
                            text = liveConfig.address
                        )
                        ContactInfoItem(
                            icon = Res.drawable.envelope_simple,
                            text = liveConfig.email
                        )
                        ContactInfoItem(
                            icon = Res.drawable.globe_simple,
                            text = liveConfig.website
                        )
                    }

                    ContactFormCard(
                        isCompact = true,
                        config = liveConfig
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = verticalPadding, horizontal = horizontalPadding),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left: Contact Info
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = liveConfig.title,
                            fontSize = 56.sp,
                            lineHeight = 64.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = getMontserratFontFamily(),
                            color = Color(0xFF0F172A)
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = liveConfig.subtitle,
                            fontSize = 18.sp,
                            lineHeight = 28.sp,
                            color = Color(0xFF64748B),
                            fontFamily = getDmSansFontFamily()
                        )
                        Spacer(modifier = Modifier.height(48.dp))
                        
                        ContactInfoItem(
                            icon = Res.drawable.map_pin,
                            text = liveConfig.address
                        )
                        ContactInfoItem(
                            icon = Res.drawable.envelope_simple,
                            text = liveConfig.email
                        )
                        ContactInfoItem(
                            icon = Res.drawable.globe_simple,
                            text = liveConfig.website
                        )
                    }

                    // Right: Form
                    Box(modifier = Modifier.weight(1.2f).padding(start = 100.dp)) {
                        ContactFormCard(
                            isCompact = false,
                            config = liveConfig
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ContactFormCard(
    isCompact: Boolean,
    config: StrategicConversationConfig = StrategicConversationConfig(),
    initialName: String = "",
    initialEmail: String = "",
    initialMessage: String = "",
    initialValidationError: String? = null,
    initialSuccess: Boolean = false,
    initialIsSubmitting: Boolean = false
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(48.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(if (isCompact) 32.dp else 64.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            var name by remember { mutableStateOf(initialName) }
            var email by remember { mutableStateOf(initialEmail) }
            var message by remember { mutableStateOf(initialMessage) }
            var submissionSuccess by remember { mutableStateOf(initialSuccess) }
            var validationError by remember { mutableStateOf(initialValidationError) }
            var isSubmitting by remember { mutableStateOf(initialIsSubmitting) }
            val coroutineScope = rememberCoroutineScope()

            if (submissionSuccess) {
                Surface(
                    color = Color(0xFFE8F5E9),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        com.sekota.components.SekotaCheckmarkVectorIcon(size = 24.dp, tint = Color(0xFF2E7D32))
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = "Permintaan Konsultasi Diterima",
                                fontFamily = getDmSansFontFamily(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color(0xFF2E7D32)
                            )
                            Text(
                                text = "Tim analis strategis kami akan menghubungi Anda dalam 1x24 jam kerja.",
                                fontFamily = getDmSansFontFamily(),
                                fontSize = 14.sp,
                                color = Color(0xFF388E3C)
                            )
                        }
                    }
                }
            }

            validationError?.let {
                Surface(
                    color = Color(0xFFFFEBEE),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        com.sekota.components.SekotaWarningVectorIcon(size = 18.dp, tint = Color(0xFFC62828))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = it,
                            color = Color(0xFFC62828),
                            fontSize = 13.sp,
                            fontFamily = getDmSansFontFamily(),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            if (isCompact) {
                Column(verticalArrangement = Arrangement.spacedBy(32.dp)) {
                    FormTextField(
                        value = name,
                        onValueChange = { name = it; validationError = null },
                        label = config.nameLabel,
                        placeholder = config.namePlaceholder,
                        modifier = Modifier.fillMaxWidth()
                    )
                    FormTextField(
                        value = email,
                        onValueChange = { email = it; validationError = null },
                        label = config.emailLabel,
                        placeholder = config.emailPlaceholder,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    FormTextField(
                        value = name,
                        onValueChange = { name = it; validationError = null },
                        label = config.nameLabel,
                        placeholder = config.namePlaceholder,
                        modifier = Modifier.weight(1f)
                    )
                    FormTextField(
                        value = email,
                        onValueChange = { email = it; validationError = null },
                        label = config.emailLabel,
                        placeholder = config.emailPlaceholder,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            FormTextField(
                value = message,
                onValueChange = { message = it; validationError = null },
                label = config.messageLabel,
                placeholder = config.messagePlaceholder,
                modifier = Modifier.fillMaxWidth(),
                isTextArea = true
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Button(
                onClick = {
                    if (isSubmitting) return@Button
                    if (name.isBlank()) {
                        validationError = "Mohon isi nama lengkap Anda."
                        return@Button
                    }
                    if (email.isBlank() || !email.contains("@")) {
                        validationError = "Mohon isi email bisnis yang valid (contoh: nama@instansi.go.id)."
                        return@Button
                    }
                    if (message.isBlank()) {
                        validationError = "Mohon tuliskan pesan atau kebutuhan strategis Anda."
                        return@Button
                    }

                    coroutineScope.launch {
                        isSubmitting = true
                        try {
                            val inquiry = com.sekota.features.admin.domain.model.ClientInquiry(
                                id = "inq-${kotlin.random.Random.nextInt(10000, 99999)}",
                                name = name.trim(),
                                email = email.trim(),
                                message = message.trim(),
                                timestamp = "Baru Saja",
                                status = "New"
                            )
                            val success = syncService.submitInquiry(inquiry)
                            if (success) {
                                submissionSuccess = true
                                validationError = null
                                name = ""
                                email = ""
                                message = ""
                            } else {
                                validationError = "Gagal mengirimkan pesan konsultasi. Silakan coba beberapa saat lagi."
                            }
                        } catch (e: Exception) {
                            validationError = "Terjadi kesalahan: ${e.message ?: "Gagal mengirim formulir"}"
                        } finally {
                            isSubmitting = false
                        }
                    }
                },
                enabled = !isSubmitting,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp),
                shape = RoundedCornerShape(36.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0F172A),
                    disabledContainerColor = Color(0xFF0F172A).copy(alpha = 0.6f)
                )
            ) {
                if (isSubmitting) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.5.dp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Mengirim...",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontFamily = getDmSansFontFamily()
                        )
                    }
                } else {
                    Text(
                        text = config.submitButtonLabel,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontFamily = getDmSansFontFamily()
                    )
                }
            }
        }
    }
}

@Composable
fun ContactInfoItem(icon: org.jetbrains.compose.resources.DrawableResource, text: String) {
    Row(
        modifier = Modifier.padding(bottom = 32.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Color(0xFFF1F5F9), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = Color(0xFF475569)
            )
        }
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = text,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            color = Color(0xFF475569),
            fontFamily = getDmSansFontFamily()
        )
    }
}

@Composable
fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    isTextArea: Boolean = false
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF64748B),
            fontFamily = getDmSansFontFamily()
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { 
                Text(
                    text = placeholder, 
                    color = Color(0xFF94A3B8), 
                    fontFamily = getDmSansFontFamily(),
                    fontSize = 16.sp
                ) 
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color(0xFF0F172A)
            ),
            textStyle = TextStyle(
                fontSize = 16.sp,
                fontFamily = getDmSansFontFamily(),
                color = Color(0xFF0F172A)
            ),
            minLines = if (isTextArea) 4 else 1,
            maxLines = if (isTextArea) 10 else 1
        )
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun ContactFormSectionPreview() {
    MaterialTheme {
        ContactFormSection(config = StrategicConversationConfig())
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun ContactFormSectionCompactPreview() {
    MaterialTheme {
        Box(modifier = Modifier.width(420.dp)) {
            ContactFormSection(config = StrategicConversationConfig())
        }
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun ContactFormCardValidationErrorPreview() {
    MaterialTheme {
        Box(modifier = Modifier.padding(32.dp)) {
            ContactFormCard(
                isCompact = false,
                initialName = "Budi Santoso",
                initialEmail = "budisentosa",
                initialMessage = "",
                initialValidationError = "Mohon isi email bisnis yang valid (contoh: nama@instansi.go.id)."
            )
        }
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun ContactFormCardSubmissionSuccessPreview() {
    MaterialTheme {
        Box(modifier = Modifier.padding(32.dp)) {
            ContactFormCard(
                isCompact = false,
                initialSuccess = true
            )
        }
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun ContactFormCardSubmittingPreview() {
    MaterialTheme {
        Box(modifier = Modifier.padding(32.dp)) {
            ContactFormCard(
                isCompact = false,
                initialName = "Budi Santoso",
                initialEmail = "budi@instansi.go.id",
                initialMessage = "Kebutuhan analisis kebijakan publik",
                initialIsSubmitting = true
            )
        }
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun ContactFormCardStatesPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF8FAFB))
                .padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(48.dp)
        ) {
            // State 1: Empty Form
            Text(
                "State 1: Form Kosong (Empty Form)",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = getMontserratFontFamily(),
                color = Color(0xFF0F172A)
            )
            ContactFormCard(
                isCompact = false
            )

            // State 2: Filled Form
            Text(
                "State 2: Form Terisi (Filled Form)",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = getMontserratFontFamily(),
                color = Color(0xFF0F172A)
            )
            ContactFormCard(
                isCompact = false,
                initialName = "Dr. Raden Mas Arya",
                initialEmail = "arya@bappenas.go.id",
                initialMessage = "Kami memerlukan analisis simulasi kebijakan transisi energi dan evaluasi dampak sosial untuk program RPJMN 2025-2029."
            )

            // State 3: Validation Error State
            Text(
                "State 3: Error Validasi (Validation Error)",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = getMontserratFontFamily(),
                color = Color(0xFF0F172A)
            )
            ContactFormCard(
                isCompact = false,
                initialName = "Budi Santoso",
                initialEmail = "invalid-email-format",
                initialMessage = "",
                initialValidationError = "Mohon isi email bisnis yang valid (contoh: nama@instansi.go.id)."
            )

            // State 4: Success Submission State
            Text(
                "State 4: Permintaan Terkirim (Submission Success)",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = getMontserratFontFamily(),
                color = Color(0xFF0F172A)
            )
            ContactFormCard(
                isCompact = false,
                initialSuccess = true
            )
        }
    }
}
