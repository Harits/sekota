package com.sekota.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sekota.features.auth.domain.model.AuthRequest
import com.sekota.features.auth.domain.model.AuthResponse
import com.sekota.features.auth.domain.model.User
import com.sekota.features.auth.domain.repository.AuthRepository
import com.sekota.features.auth.domain.usecase.LoginUseCase
import com.sekota.getDmSansFontFamily
import com.sekota.getMontserratFontFamily
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    loginUseCase: LoginUseCase,
    onLoginSuccess: () -> Unit,
    onNavigateToSignup: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    val scope = rememberCoroutineScope()

    val scrollState = rememberScrollState()
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7FAFB)) // Surface Alt
            .verticalScroll(scrollState),
        contentAlignment = Alignment.Center
    ) {
        val isMobile = maxWidth < 600.dp
        val cardPadding = if (isMobile) 24.dp else 48.dp
        val cardShape = if (isMobile) RoundedCornerShape(16.dp) else RoundedCornerShape(24.dp)
        val containerModifier = if (isMobile) {
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 32.dp)
        } else {
            Modifier
                .width(440.dp)
                .padding(vertical = 48.dp)
        }

        Card(
            modifier = containerModifier,
            shape = cardShape,
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = if (isMobile) 0.dp else 2.dp),
            border = if (isMobile) androidx.compose.foundation.BorderStroke(1.dp, Color(0x1A0D1F2D)) else null
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(cardPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Sign In",
                    fontFamily = getMontserratFontFamily(),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = if (isMobile) 28.sp else 36.sp,
                    color = Color(0xFF0D1F2D), // Ink Navy
                    letterSpacing = (-1).sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Text(
                    text = "Access Trusted Intelligence.",
                    fontFamily = getDmSansFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = if (isMobile) 14.sp else 16.sp,
                    color = Color(0x99143244), // Ink Secondary
                    modifier = Modifier.padding(bottom = if (isMobile) 32.dp else 40.dp)
                )

                // Email Input
                Column(modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)) {
                    Text(
                        text = "EMAIL ADDRESS",
                        fontFamily = getDmSansFontFamily(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        letterSpacing = 1.sp,
                        color = Color(0xFF0D1F2D)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    TextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = { Text("reader@sekota.com", color = Color(0x330D1F2D)) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFF7FAFB),
                            unfocusedContainerColor = Color(0xFFF7FAFB),
                            focusedIndicatorColor = Color(0xFF00B5C8), // Brand Teal
                            unfocusedIndicatorColor = Color(0x1A0D1F2D) // Whisper Hairline
                        ),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true,
                        textStyle = androidx.compose.ui.text.TextStyle(fontFamily = getDmSansFontFamily(), fontSize = 15.sp, color = Color(0xFF0D1F2D))
                    )
                }

                // Password Input
                Column(modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)) {
                    Text(
                        text = "PASSWORD",
                        fontFamily = getDmSansFontFamily(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        letterSpacing = 1.sp,
                        color = Color(0xFF0D1F2D)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = { Text("Enter your password", color = Color(0x330D1F2D)) },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFF7FAFB),
                            unfocusedContainerColor = Color(0xFFF7FAFB),
                            focusedIndicatorColor = Color(0xFF00B5C8), // Brand Teal
                            unfocusedIndicatorColor = Color(0x1A0D1F2D)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true,
                        textStyle = androidx.compose.ui.text.TextStyle(fontFamily = getDmSansFontFamily(), fontSize = 15.sp, color = Color(0xFF0D1F2D))
                    )
                }

                Button(
                    onClick = {
                        scope.launch {
                            isLoading = true
                            errorMessage = null
                            val result = loginUseCase(AuthRequest(email.trim(), password))
                            isLoading = false
                            if (result.isSuccess) {
                                onLoginSuccess()
                            } else {
                                errorMessage = result.exceptionOrNull()?.message ?: "Authentication failed"
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(9999.dp), // Pill Button
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D1F2D)), // Ink Navy
                    enabled = !isLoading && email.isNotBlank() && password.isNotBlank()
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                    } else {
                        Text(
                            "Sign In",
                            fontFamily = getDmSansFontFamily(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }
                }
                
                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = Color.Red,
                        fontFamily = getDmSansFontFamily(),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

                TextButton(
                    onClick = onNavigateToSignup,
                    modifier = Modifier.padding(top = 20.dp)
                ) {
                    Text(
                        "Don't have an account? Sign Up",
                        fontFamily = getDmSansFontFamily(),
                        color = Color(0xFF00B5C8), // Brand Teal
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

private class DummyAuthRepository : AuthRepository {
    override suspend fun login(request: AuthRequest): Result<AuthResponse> =
        Result.success(AuthResponse(token = "dummy-token", userId = "user-123", role = "READER"))
    override suspend fun signup(request: AuthRequest): Result<AuthResponse> =
        Result.success(AuthResponse(token = "dummy-token", userId = "user-123", role = "READER"))
    override fun getToken(): String? = null
    override fun saveToken(token: String) {}
    override fun clearToken() {}
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreen(
            loginUseCase = LoginUseCase(DummyAuthRepository()),
            onLoginSuccess = {},
            onNavigateToSignup = {}
        )
    }
}
