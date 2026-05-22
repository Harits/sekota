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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sekota.features.auth.domain.model.AuthRequest
import com.sekota.features.auth.domain.usecase.SignupUseCase
import com.sekota.getDmSansFontFamily
import com.sekota.getMontserratFontFamily
import kotlinx.coroutines.launch

@Composable
fun SignupScreen(
    signupUseCase: SignupUseCase,
    onSignupSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Create Account",
            fontFamily = getMontserratFontFamily(),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 32.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username", fontFamily = getDmSansFontFamily()) },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password", fontFamily = getDmSansFontFamily()) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
            singleLine = true
        )

        Button(
            onClick = {
                scope.launch {
                    isLoading = true
                    errorMessage = null
                    val result = signupUseCase(AuthRequest(username, password))
                    isLoading = false
                    if (result.isSuccess) {
                        onSignupSuccess()
                    } else {
                        errorMessage = result.exceptionOrNull()?.message ?: "Signup failed"
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text(
                    "Sign Up",
                    fontFamily = getDmSansFontFamily(),
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
        
        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = Color.Red,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onNavigateToLogin) {
            Text(
                "Already have an account? Log in",
                fontFamily = getDmSansFontFamily(),
                color = Color.Black
            )
        }
    }
}
