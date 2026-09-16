package com.sekota.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sekota.features.profile.domain.model.UserProfile
import com.sekota.features.profile.domain.repository.ProfileRepository
import com.sekota.features.profile.domain.usecase.GetProfileUseCase
import com.sekota.features.profile.domain.usecase.UpdateProfileUseCase
import com.sekota.getDmSansFontFamily
import com.sekota.getMontserratFontFamily
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    getProfileUseCase: GetProfileUseCase,
    updateProfileUseCase: UpdateProfileUseCase,
    onLogout: () -> Unit
) {
    var profile by remember { mutableStateOf<UserProfile?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var isSaving by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    
    var email by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val result = getProfileUseCase()
        if (result.isSuccess) {
            val p = result.getOrNull()
            profile = p
            email = p?.email?.ifBlank { p.username } ?: ""
            fullName = p?.fullName ?: ""
        } else {
            errorMessage = result.exceptionOrNull()?.message ?: "Failed to load profile"
        }
        isLoading = false
    }

    val scrollState = rememberScrollState()
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7FAFB)) // Surface Alt
            .verticalScroll(scrollState)
            .padding(vertical = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        val isMobile = maxWidth < 768.dp
        val horizontalPadding = if (isMobile) 16.dp else 48.dp
        val cardPadding = if (isMobile) 24.dp else 48.dp

        if (isLoading) {
            CircularProgressIndicator(color = Color(0xFF00B5C8))
        } else if (profile != null) {
            Card(
                modifier = Modifier
                    .widthIn(max = 840.dp)
                    .fillMaxWidth()
                    .padding(horizontal = horizontalPadding),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x1A0D1F2D))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(cardPadding)
                ) {
                    // Header Area with Avatar & Role Badge
                    if (isMobile) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            ProfileHeaderInfo(
                                fullName = fullName,
                                email = email,
                                username = profile?.username ?: "",
                                role = profile?.role ?: "READER",
                                isMobile = true
                            )
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 40.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ProfileHeaderInfo(
                                fullName = fullName,
                                email = email,
                                username = profile?.username ?: "",
                                role = profile?.role ?: "READER",
                                isMobile = false
                            )
                        }
                    }

                    HorizontalDivider(color = Color(0x140D1F2D), thickness = 1.dp)
                    Spacer(modifier = Modifier.height(32.dp))

                    if (isMobile) {
                        // Mobile layout: Stacked form & actions
                        Column(modifier = Modifier.fillMaxWidth()) {
                            ProfileFormFields(
                                fullName = fullName,
                                onFullNameChange = { fullName = it },
                                email = email,
                                onEmailChange = { email = it },
                                role = profile?.role ?: "READER"
                            )

                            Spacer(modifier = Modifier.height(32.dp))

                            ProfileActions(
                                isSaving = isSaving,
                                errorMessage = errorMessage,
                                successMessage = successMessage,
                                onSave = {
                                    scope.launch {
                                        isSaving = true
                                        errorMessage = null
                                        successMessage = null
                                        val updatedProfile = profile?.copy(fullName = fullName.trim(), email = email.trim())
                                        if (updatedProfile != null) {
                                            val result = updateProfileUseCase(updatedProfile)
                                            if (result.isSuccess) {
                                                profile = result.getOrNull()
                                                successMessage = "Profile updated successfully"
                                            } else {
                                                errorMessage = result.exceptionOrNull()?.message ?: "Update failed"
                                            }
                                        }
                                        isSaving = false
                                    }
                                },
                                onLogout = onLogout
                            )
                        }
                    } else {
                        // Desktop layout: Two columns
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            ProfileFormFields(
                                fullName = fullName,
                                onFullNameChange = { fullName = it },
                                email = email,
                                onEmailChange = { email = it },
                                role = profile?.role ?: "READER",
                                modifier = Modifier.weight(1.2f)
                            )

                            Spacer(modifier = Modifier.width(48.dp))

                            ProfileActions(
                                isSaving = isSaving,
                                errorMessage = errorMessage,
                                successMessage = successMessage,
                                onSave = {
                                    scope.launch {
                                        isSaving = true
                                        errorMessage = null
                                        successMessage = null
                                        val updatedProfile = profile?.copy(fullName = fullName.trim(), email = email.trim())
                                        if (updatedProfile != null) {
                                            val result = updateProfileUseCase(updatedProfile)
                                            if (result.isSuccess) {
                                                profile = result.getOrNull()
                                                successMessage = "Profile updated successfully"
                                            } else {
                                                errorMessage = result.exceptionOrNull()?.message ?: "Update failed"
                                            }
                                        }
                                        isSaving = false
                                    }
                                },
                                onLogout = onLogout,
                                modifier = Modifier.weight(0.8f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileHeaderInfo(
    fullName: String,
    email: String,
    username: String,
    role: String,
    isMobile: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Avatar circle with initials
        val initial = if (fullName.isNotBlank()) fullName.take(1).uppercase() else (email.take(1).uppercase().ifEmpty { "R" })
        Surface(
            shape = CircleShape,
            color = Color(0xFF0D1F2D),
            modifier = Modifier.size(if (isMobile) 56.dp else 72.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = initial,
                    color = Color(0xFF00B5C8),
                    fontFamily = getMontserratFontFamily(),
                    fontWeight = FontWeight.Bold,
                    fontSize = if (isMobile) 22.sp else 28.sp
                )
            }
        }

        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = if (fullName.isNotBlank()) fullName else username.ifEmpty { "Sekota Member" },
                    fontFamily = getMontserratFontFamily(),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = if (isMobile) 20.sp else 26.sp,
                    color = Color(0xFF0D1F2D),
                    letterSpacing = (-0.5).sp
                )
                // Role Badge (Color based on role)
                val (badgeBg, badgeBorder, badgeText) = when (role.uppercase()) {
                    "ADMIN" -> Triple(Color(0x1AE53935), Color(0xFFE53935), Color(0xFFE53935))
                    "BOD" -> Triple(Color(0x1A7E57C2), Color(0xFF7E57C2), Color(0xFF7E57C2))
                    "BD" -> Triple(Color(0x1AFB8C00), Color(0xFFFB8C00), Color(0xFFFB8C00))
                    else -> Triple(Color(0x1A00B5C8), Color(0xFF00B5C8), Color(0xFF00B5C8))
                }

                Surface(
                    shape = RoundedCornerShape(9999.dp),
                    color = badgeBg,
                    border = androidx.compose.foundation.BorderStroke(1.dp, badgeBorder)
                ) {
                    Text(
                        text = role.uppercase(),
                        fontFamily = getDmSansFontFamily(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        letterSpacing = 1.sp,
                        color = badgeText,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = email.ifEmpty { username },
                fontFamily = getDmSansFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = if (isMobile) 13.sp else 15.sp,
                color = Color(0x99143244)
            )
        }
    }
}

@Composable
private fun ProfileFormFields(
    fullName: String,
    onFullNameChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    role: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        ProfileField(
            label = "FULL NAME",
            value = fullName,
            onValueChange = onFullNameChange,
            placeholder = "e.g. Putu Aan J."
        )
        
        Spacer(modifier = Modifier.height(24.dp))

        ProfileField(
            label = "EMAIL IDENTITY",
            value = email,
            onValueChange = onEmailChange,
            placeholder = "reader@sekota.com"
        )
        
        Spacer(modifier = Modifier.height(24.dp))

        // Role field (Dynamic Role Display)
        Column {
            Text(
                text = "ACCOUNT ROLE",
                fontFamily = getDmSansFontFamily(),
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                letterSpacing = 1.sp,
                color = Color(0xFF0D1F2D)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFF7FAFB),
                modifier = Modifier.fillMaxWidth().height(48.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x1A0D1F2D))
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val roleLabel = when (role.uppercase()) {
                        "ADMIN" -> "ADMIN (Full Platform Access)"
                        "BOD" -> "BOD (Executive Oversight)"
                        "BD" -> "BD (Business Development)"
                        else -> "READER (Standard Access)"
                    }
                    Text(
                        text = roleLabel,
                        fontFamily = getDmSansFontFamily(),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = Color(0xFF0D1F2D)
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileActions(
    isSaving: Boolean,
    errorMessage: String?,
    successMessage: String?,
    onSave: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Button(
            onClick = onSave,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(9999.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D1F2D)), // Ink Navy
            enabled = !isSaving
        ) {
            if (isSaving) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
            } else {
                Text(
                    "Commit Changes",
                    fontFamily = getDmSansFontFamily(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedButton(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(9999.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x260D1F2D)),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF0D1F2D))
        ) {
            Text(
                "Terminate Session",
                fontFamily = getDmSansFontFamily(),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        if (successMessage != null) {
            Text(
                text = successMessage,
                color = Color(0xFF60BD65), // Brand Green
                fontFamily = getDmSansFontFamily(),
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontFamily = getDmSansFontFamily(),
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
fun ProfileField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    Column {
        Text(
            text = label,
            fontFamily = getDmSansFontFamily(),
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            letterSpacing = 1.sp,
            color = Color(0xFF0D1F2D)
        )
        Spacer(modifier = Modifier.height(6.dp))
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Color(0x330D1F2D)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF7FAFB),
                unfocusedContainerColor = Color(0xFFF7FAFB),
                focusedIndicatorColor = Color(0xFF00B5C8), // Brand Teal
                unfocusedIndicatorColor = Color(0x1A0D1F2D)
            ),
            singleLine = true,
            textStyle = androidx.compose.ui.text.TextStyle(fontFamily = getDmSansFontFamily(), fontSize = 15.sp, color = Color(0xFF0D1F2D))
        )
    }
}

private class DummyProfileRepository : ProfileRepository {
    override suspend fun getProfile(): Result<UserProfile> =
        Result.success(UserProfile("reader-1", "reader@sekota.com", "reader@sekota.com", "Sekota Reader"))
    override suspend fun updateProfile(profile: UserProfile): Result<UserProfile> =
        Result.success(profile)
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun ProfileScreenPreview() {
    MaterialTheme {
        ProfileScreen(
            getProfileUseCase = GetProfileUseCase(DummyProfileRepository()),
            updateProfileUseCase = UpdateProfileUseCase(DummyProfileRepository()),
            onLogout = {}
        )
    }
}
