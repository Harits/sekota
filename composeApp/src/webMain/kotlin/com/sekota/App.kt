package com.sekota

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun App() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFAFAFA)) // Clean off-white background
        ) {
            val scrollState = rememberScrollState()
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                // Spacer for Sticky Navbar height
                Spacer(modifier = Modifier.height(80.dp))
                
                BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                    val isMobile = maxWidth < 768.dp
                    
                    Row(modifier = Modifier.fillMaxWidth()) {
                        // Main Content Area
                        Column(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            HeroSection()
                            PartnerSection()
                            // Add more content here later
                            
                            // Adding extra space to demonstrate scrolling
                            Spacer(modifier = Modifier.height(1000.dp))
                        }

                        // Sidebar - Hidden on mobile
                        if (!isMobile) {
                            SidebarFilter()
                        }
                    }
                }
            }

            // Sticky Navbar at the top
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
            ) {
                Navbar()
            }
        }
    }
}
