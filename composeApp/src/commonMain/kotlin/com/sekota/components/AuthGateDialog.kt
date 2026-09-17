package com.sekota.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.sekota.getDmSansFontFamily
import com.sekota.getMontserratFontFamily
import com.sekota.features.auth.domain.model.AuthRequest
import com.sekota.features.auth.domain.usecase.LoginUseCase
import com.sekota.features.auth.domain.usecase.SignupUseCase
import kotlinx.coroutines.launch

enum class AuthGateMode {
    SIGN_IN,
    CREATE_IDENTITY
}

/**
 * UC-GATE-01: Inline Modal Dialog Authentication Gating
 * Appears over any interactive action (e.g. Read Now, Add to Library, Order Merchandise)
 * when a guest user attempts a gated action.
 */
@Composable
fun AuthGateDialog(
    title: String = "Authentication Required",
    subtitle: String = "Please sign in or create an identity to proceed.",
    loginUseCase: LoginUseCase,
    signupUseCase: SignupUseCase,
    onDismissRequest: () -> Unit,
    onAuthSuccess: () -> Unit
) {
    var mode by remember { mutableStateOf(AuthGateMode.SIGN_IN) }
    var emailOrUsername by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 460.dp)
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Eyebrow
                Text(
                    text = "SEKOTA ACCESS GATE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    fontFamily = getDmSansFontFamily(),
                    color = Color(0xFF00B5C8) // Brand Teal
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Title
                Text(
                    text = title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = getMontserratFontFamily(),
                    color = Color(0xFF0D1F2D) // Ink Navy
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    fontFamily = getDmSansFontFamily(),
                    color = Color(0xFF71717A),
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Segmented Tabs (Sign In / Create Identity)
                Surface(
                    shape = RoundedCornerShape(9999.dp),
                    color = Color(0xFFF7FAFB),
                    modifier = Modifier.fillMaxWidth().height(42.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x1A0D1F2D))
                ) {
                    Row(modifier = Modifier.fillMaxSize().padding(3.dp)) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .background(
                                    if (mode == AuthGateMode.SIGN_IN) Color(0xFF0D1F2D) else Color.Transparent,
                                    RoundedCornerShape(9999.dp)
                                )
                                .clickable { 
                                    mode = AuthGateMode.SIGN_IN
                                    errorMessage = null 
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Sign In",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = getDmSansFontFamily(),
                                color = if (mode == AuthGateMode.SIGN_IN) Color.White else Color(0xFF0D1F2D)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .background(
                                    if (mode == AuthGateMode.CREATE_IDENTITY) Color(0xFF0D1F2D) else Color.Transparent,
                                    RoundedCornerShape(9999.dp)
                                )
                                .clickable { 
                                    mode = AuthGateMode.CREATE_IDENTITY
                                    errorMessage = null 
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Create Identity",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = getDmSansFontFamily(),
                                color = if (mode == AuthGateMode.CREATE_IDENTITY) Color.White else Color(0xFF0D1F2D)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Fields
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = if (mode == AuthGateMode.SIGN_IN) "EMAIL OR USERNAME" else "USERNAME",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        fontFamily = getDmSansFontFamily(),
                        color = Color(0xFF0D1F2D)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = emailOrUsername,
                        onValueChange = { emailOrUsername = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text(if (mode == AuthGateMode.SIGN_IN) "reader@sekota.id" else "choose username", fontSize = 13.sp) },
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "PASSWORD",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        fontFamily = getDmSansFontFamily(),
                        color = Color(0xFF0D1F2D)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("••••••••", fontSize = 13.sp) },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp)
                    )
                }

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        fontFamily = getDmSansFontFamily()
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Submit Pill Button
                Button(
                    onClick = {
                        scope.launch {
                            isLoading = true
                            errorMessage = null
                            val req = AuthRequest(email = emailOrUsername.trim(), password = password)
                            val result = if (mode == AuthGateMode.SIGN_IN) {
                                loginUseCase(req)
                            } else {
                                signupUseCase(req)
                            }
                            isLoading = false
                            if (result.isSuccess) {
                                onAuthSuccess()
                            } else {
                                errorMessage = result.exceptionOrNull()?.message ?: "Authentication failed"
                            }
                        }
                    },
                    shape = RoundedCornerShape(9999.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D1F2D)),
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    enabled = !isLoading && emailOrUsername.isNotBlank() && password.isNotBlank()
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                    } else {
                        Text(
                            text = if (mode == AuthGateMode.SIGN_IN) "Sign In & Continue" else "Register & Continue",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            fontFamily = getDmSansFontFamily()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Cancel link
                TextButton(onClick = onDismissRequest) {
                    Text(
                        text = "Cancel",
                        fontSize = 13.sp,
                        fontFamily = getDmSansFontFamily(),
                        color = Color(0xFF71717A)
                    )
                }
            }
        }
    }
}
