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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sekota.features.auth.domain.model.AuthRequest
import com.sekota.features.auth.domain.usecase.LoginUseCase
import com.sekota.features.admin.domain.usecase.ValidateAdminRoleUseCase
import com.sekota.getDmSansFontFamily
import com.sekota.getMontserratFontFamily
import kotlinx.coroutines.launch

@Composable
fun AdminLoginScreen(
    loginUseCase: LoginUseCase,
    validateAdminRoleUseCase: ValidateAdminRoleUseCase = ValidateAdminRoleUseCase(),
    onLoginSuccess: (role: String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1F2D)), // Ink Navy
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .widthIn(max = 440.dp)
                .fillMaxWidth()
                .padding(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Badge
                Surface(
                    color = Color(0xFF00B5C8).copy(alpha = 0.12f),
                    shape = RoundedCornerShape(100.dp),
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Text(
                        text = "RESTRICTED ACCESS",
                        color = Color(0xFF00B5C8),
                        fontFamily = getDmSansFontFamily(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        letterSpacing = 1.2.sp,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                    )
                }

                Text(
                    text = "Sekota Backoffice CMS",
                    fontFamily = getMontserratFontFamily(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = Color(0xFF0D1F2D),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Masuk menggunakan kredensial akun Administrator atau Board of Directors (BOD)",
                    fontFamily = getDmSansFontFamily(),
                    fontSize = 13.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp, bottom = 28.dp)
                )

                // Email Input
                Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                    Text(
                        text = "ADMIN EMAIL",
                        fontFamily = getDmSansFontFamily(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        letterSpacing = 1.sp,
                        color = Color(0xFF0D1F2D)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = { Text("admin@sekota.com", color = Color.LightGray) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                // Password Input
                Column(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
                    Text(
                        text = "PASSWORD",
                        fontFamily = getDmSansFontFamily(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        letterSpacing = 1.sp,
                        color = Color(0xFF0D1F2D)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = { Text("Masukkan kata sandi", color = Color.LightGray) },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                if (errorMessage != null) {
                    Surface(
                        color = Color(0xFFFFEBEE),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)
                    ) {
                        Text(
                            text = errorMessage!!,
                            color = Color(0xFFC62828),
                            fontFamily = getDmSansFontFamily(),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(12.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Button(
                    onClick = {
                        scope.launch {
                            isLoading = true
                            errorMessage = null
                            val result = loginUseCase(AuthRequest(email.trim(), password))
                            isLoading = false
                            if (result.isSuccess) {
                                val authResponse = result.getOrNull()
                                val role = authResponse?.role ?: "READER"
                                if (validateAdminRoleUseCase(role)) {
                                    onLoginSuccess(role)
                                } else {
                                    errorMessage = "Akses Ditolak: Akun Anda memiliki role '${role.uppercase()}'. Akses Backoffice CMS terbatas hanya untuk ADMIN atau BOD."
                                }
                            } else {
                                errorMessage = result.exceptionOrNull()?.message ?: "Autentikasi gagal. Silakan periksa kredensial Anda."
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(100.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D1F2D)),
                    enabled = !isLoading && email.isNotBlank() && password.isNotBlank()
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(22.dp), strokeWidth = 2.dp)
                    } else {
                        Text(
                            text = "Verifikasi & Masuk CMS",
                            fontFamily = getDmSansFontFamily(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Preview(device = DESKTOP)
@Composable
fun AdminLoginScreenPreview() {
    MaterialTheme {
        AdminLoginScreen(
            loginUseCase = LoginUseCase(object : com.sekota.features.auth.domain.repository.AuthRepository {
                override suspend fun login(request: AuthRequest) = Result.success(
                    com.sekota.features.auth.domain.model.AuthResponse("dummy", "u1", "ADMIN")
                )
                override suspend fun signup(request: AuthRequest) = Result.success(
                    com.sekota.features.auth.domain.model.AuthResponse("dummy", "u1", "ADMIN")
                )
                override fun getToken(): String? = null
                override fun saveToken(token: String) {}
                override fun clearToken() {}
            }),
            onLoginSuccess = {}
        )
    }
}
