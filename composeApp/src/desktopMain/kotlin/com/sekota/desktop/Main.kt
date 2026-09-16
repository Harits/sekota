package com.sekota.desktop

import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.sekota.core.storage.TokenStorage
import com.sekota.features.admin.domain.usecase.ValidateAdminRoleUseCase
import com.sekota.features.auth.data.repository.AuthRepositoryImpl
import com.sekota.features.auth.domain.usecase.ClearTokenUseCase
import com.sekota.features.auth.domain.usecase.GetTokenUseCase
import com.sekota.features.auth.domain.usecase.LoginUseCase
import com.sekota.screens.AdminDashboardScreen
import com.sekota.screens.AdminLoginScreen

fun main() = application {
    val tokenStorage = remember { TokenStorage() }
    val authRepository = remember { AuthRepositoryImpl(tokenStorage) }
    val loginUseCase = remember { LoginUseCase(authRepository) }
    val getTokenUseCase = remember { GetTokenUseCase(authRepository) }
    val clearTokenUseCase = remember { ClearTokenUseCase(authRepository) }
    val validateAdminRoleUseCase = remember { ValidateAdminRoleUseCase() }

    var isAuthenticated by remember {
        mutableStateOf(getTokenUseCase() != null)
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = if (isAuthenticated) "Sekota CMS - Desktop Administration" else "Sekota CMS - Admin Authentication",
        state = WindowState(width = 1200.dp, height = 800.dp)
    ) {
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
