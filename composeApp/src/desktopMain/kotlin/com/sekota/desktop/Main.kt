package com.sekota.desktop

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.sekota.screens.AdminDashboardScreen

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Sekota CMS - Desktop Administration",
        state = WindowState(width = 1200.dp, height = 800.dp)
    ) {
        AdminDashboardScreen()
    }
}
