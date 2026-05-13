package com.sekota

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun App() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFAFAFA)) // Clean off-white background
        ) {
            // Sticky Navbar
            Navbar()

            Row(modifier = Modifier.fillMaxSize().weight(1f)) {
                // Main Content Area
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    HeroSection()
                    PartnerSection()
                    // Add more content here later
                }

                // Sidebar
                SidebarFilter()
            }
        }
    }
}
