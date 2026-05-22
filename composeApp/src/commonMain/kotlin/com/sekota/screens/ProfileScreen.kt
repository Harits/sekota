package com.sekota.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sekota.features.profile.domain.model.UserProfile
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
    
    var email by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val result = getProfileUseCase()
        if (result.isSuccess) {
            val p = result.getOrNull()
            profile = p
            email = p?.email ?: ""
            fullName = p?.fullName ?: ""
        } else {
            errorMessage = result.exceptionOrNull()?.message ?: "Failed to load profile"
        }
        isLoading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "User Profile",
            fontFamily = getMontserratFontFamily(),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 32.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        if (isLoading) {
            CircularProgressIndicator()
        } else if (profile != null) {
            OutlinedTextField(
                value = profile?.username ?: "",
                onValueChange = { },
                label = { Text("Username", fontFamily = getDmSansFontFamily()) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                enabled = false,
                singleLine = true
            )
            
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Full Name", fontFamily = getDmSansFontFamily()) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                singleLine = true
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email", fontFamily = getDmSansFontFamily()) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
                singleLine = true
            )

            Button(
                onClick = {
                    scope.launch {
                        isSaving = true
                        errorMessage = null
                        val updatedProfile = profile?.copy(fullName = fullName, email = email)
                        if (updatedProfile != null) {
                            val result = updateProfileUseCase(updatedProfile)
                            if (result.isSuccess) {
                                profile = result.getOrNull()
                            } else {
                                errorMessage = result.exceptionOrNull()?.message ?: "Failed to save profile"
                            }
                        }
                        isSaving = false
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF02B6CF)),
                enabled = !isSaving
            ) {
                if (isSaving) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        "Save Profile",
                        fontFamily = getDmSansFontFamily(),
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text(
                    "Logout",
                    fontFamily = getDmSansFontFamily(),
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            
            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        } else {
            Text("Failed to load profile. Please try again.")
            Button(onClick = onLogout, modifier = Modifier.padding(top = 16.dp)) {
                Text("Logout")
            }
        }
    }
}
