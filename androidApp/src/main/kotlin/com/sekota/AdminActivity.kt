package com.sekota

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.sekota.core.storage.TokenStorage
import com.sekota.features.admin.domain.usecase.ValidateAdminRoleUseCase
import com.sekota.features.auth.data.repository.AuthRepositoryImpl
import com.sekota.features.auth.domain.usecase.ClearTokenUseCase
import com.sekota.features.auth.domain.usecase.GetTokenUseCase
import com.sekota.features.auth.domain.usecase.LoginUseCase
import com.sekota.screens.AdminDashboardScreen
import com.sekota.screens.AdminLoginScreen

class AdminActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val tokenStorage = remember { TokenStorage() }
            val authRepository = remember { AuthRepositoryImpl(tokenStorage) }
            val loginUseCase = remember { LoginUseCase(authRepository) }
            val getTokenUseCase = remember { GetTokenUseCase(authRepository) }
            val clearTokenUseCase = remember { ClearTokenUseCase(authRepository) }
            val validateAdminRoleUseCase = remember { ValidateAdminRoleUseCase() }

            var isAuthenticated by remember {
                mutableStateOf(getTokenUseCase() != null)
            }

            if (!isAuthenticated) {
                AdminLoginScreen(
                    loginUseCase = loginUseCase,
                    validateAdminRoleUseCase = validateAdminRoleUseCase,
                    onLoginSuccess = { role ->
                        isAuthenticated = true
                    }
                )
            } else {
                AdminDashboardScreen(
                    onLogout = {
                        clearTokenUseCase()
                        isAuthenticated = false
                    }
                )
            }
        }
    }
}
