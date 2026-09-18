package com.sekota

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import sekota.composeapp.generated.resources.Res
import sekota.composeapp.generated.resources.icon_sekota

enum class NavbarActiveSection {
    SOLUSI, PRODUK, KONTAK, NONE
}

private val InkNavy = Color(0xFF0D1F2D)
private val BrandTeal = Color(0xFF00B5C8)
private val BrandGreen = Color(0xFF60BD65)

@Composable
fun SekotaBrandLogo(
    iconSize: androidx.compose.ui.unit.Dp = 34.dp,
    textSize: androidx.compose.ui.unit.TextUnit = 24.sp,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        com.sekota.components.SekotaIconMark(
            size = iconSize
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "sekota",
                fontFamily = getMontserratFontFamily(),
                fontWeight = FontWeight.Black,
                fontSize = textSize,
                letterSpacing = (-0.5).sp,
                color = InkNavy
            )
            Box(
                modifier = Modifier
                    .padding(start = 2.dp, top = 6.dp)
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.linearGradient(
                            listOf(BrandTeal, BrandGreen)
                        )
                    )
            )
        }
    }
}

@Composable
fun Navbar(
    currentScreen: Screen = Screen.Landing,
    activeLandingSection: NavbarActiveSection = NavbarActiveSection.SOLUSI,
    isLoggedIn: Boolean = false,
    syncState: String = "Connected",
    modifier: Modifier = Modifier,
    onNavigate: (Screen) -> Unit,
    onSolusiClick: () -> Unit = {},
    onProdukClick: () -> Unit = {},
    onKontakClick: () -> Unit = {},
    onConsultationClick: () -> Unit = {}
) {
    // Rule 10: Interactive state hoisted outside nested BoxWithConstraints for Wasm canvas determinism
    var isMobileMenuOpen by remember { mutableStateOf(false) }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        val isCompact = maxWidth < 840.dp

        LaunchedEffect(isCompact) {
            if (!isCompact) {
                isMobileMenuOpen = false
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isCompact) {
                // Mobile Compact Navbar (< 840dp) with M3 App Bar, live sync indicator, and Hamburger menu
                Row(
                    modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Brand Logo (Native Compose + icon_sekota)
                Box(
                    modifier = Modifier.clickable {
                        isMobileMenuOpen = false
                        onNavigate(Screen.Landing)
                    }
                ) {
                    SekotaBrandLogo(
                        iconSize = 30.dp,
                        textSize = 21.sp
                    )
                }

                // Status Pill + Hamburger Toggle Button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Live sync pill
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (syncState == "Connected") Color(0xFFE8F5E9) else Color(0xFFFFF3E0))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(if (syncState == "Connected") BrandGreen else Color(0xFFFF9800))
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (syncState == "Connected") "LIVE" else "OFFLINE",
                            fontFamily = getDmSansFontFamily(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = if (syncState == "Connected") Color(0xFF2E7D32) else Color(0xFFE65100)
                        )
                    }

                    // Hamburger Toggle
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clickable {
                                isMobileMenuOpen = !isMobileMenuOpen
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        androidx.compose.foundation.Canvas(modifier = Modifier.size(24.dp)) {
                            if (isMobileMenuOpen) {
                                // Draw 'X'
                                val strokeWidth = 2.5.dp.toPx()
                                drawLine(
                                    color = InkNavy,
                                    start = androidx.compose.ui.geometry.Offset(4.dp.toPx(), 4.dp.toPx()),
                                    end = androidx.compose.ui.geometry.Offset(size.width - 4.dp.toPx(), size.height - 4.dp.toPx()),
                                    strokeWidth = strokeWidth,
                                    cap = androidx.compose.ui.graphics.StrokeCap.Round
                                )
                                drawLine(
                                    color = InkNavy,
                                    start = androidx.compose.ui.geometry.Offset(size.width - 4.dp.toPx(), 4.dp.toPx()),
                                    end = androidx.compose.ui.geometry.Offset(4.dp.toPx(), size.height - 4.dp.toPx()),
                                    strokeWidth = strokeWidth,
                                    cap = androidx.compose.ui.graphics.StrokeCap.Round
                                )
                            } else {
                                // Draw 3 horizontal bars (hamburger)
                                val strokeWidth = 2.5.dp.toPx()
                                val barWidth = size.width - 4.dp.toPx()
                                val startX = 2.dp.toPx()
                                val endX = startX + barWidth

                                // Top bar
                                drawLine(
                                    color = InkNavy,
                                    start = androidx.compose.ui.geometry.Offset(startX, 5.dp.toPx()),
                                    end = androidx.compose.ui.geometry.Offset(endX, 5.dp.toPx()),
                                    strokeWidth = strokeWidth,
                                    cap = androidx.compose.ui.graphics.StrokeCap.Round
                                )
                                // Middle bar
                                drawLine(
                                    color = InkNavy,
                                    start = androidx.compose.ui.geometry.Offset(startX, 12.dp.toPx()),
                                    end = androidx.compose.ui.geometry.Offset(endX, 12.dp.toPx()),
                                    strokeWidth = strokeWidth,
                                    cap = androidx.compose.ui.graphics.StrokeCap.Round
                                )
                                // Bottom bar
                                drawLine(
                                    color = InkNavy,
                                    start = androidx.compose.ui.geometry.Offset(startX, 19.dp.toPx()),
                                    end = androidx.compose.ui.geometry.Offset(endX, 19.dp.toPx()),
                                    strokeWidth = strokeWidth,
                                    cap = androidx.compose.ui.graphics.StrokeCap.Round
                                )
                            }
                        }
                    }
                }
            }

            // Mobile Dropdown Menu Drawer — OUTSIDE BoxWithConstraints for reliable Wasm recomposition
            AnimatedVisibility(
                visible = isMobileMenuOpen,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Solusi
                    MobileNavItem(
                        title = "Solusi",
                        isSelected = currentScreen == Screen.Landing && activeLandingSection == NavbarActiveSection.SOLUSI,
                        onClick = {
                            isMobileMenuOpen = false
                            onSolusiClick()
                        }
                    )

                    // Produk
                    MobileNavItem(
                        title = "Produk",
                        isSelected = currentScreen == Screen.Landing && activeLandingSection == NavbarActiveSection.PRODUK,
                        onClick = {
                            isMobileMenuOpen = false
                            onProdukClick()
                        }
                    )

                    // E-Book Catalog
                    MobileNavItem(
                        title = "E-Book",
                        isSelected = currentScreen == Screen.Catalog,
                        onClick = {
                            isMobileMenuOpen = false
                            onNavigate(Screen.Catalog)
                        }
                    )

                    // Merchandise
                    MobileNavItem(
                        title = "Merchandise",
                        isSelected = currentScreen == Screen.Merchandise,
                        onClick = {
                            isMobileMenuOpen = false
                            onNavigate(Screen.Merchandise)
                        }
                    )

                    // Kontak
                    MobileNavItem(
                        title = "Kontak",
                        isSelected = currentScreen == Screen.Landing && activeLandingSection == NavbarActiveSection.KONTAK,
                        onClick = {
                            isMobileMenuOpen = false
                            onKontakClick()
                        }
                    )

                    // Profile or Login
                    if (isLoggedIn) {
                        MobileNavItem(
                            title = "Profil Saya",
                            isSelected = currentScreen == Screen.Profile,
                            onClick = {
                                isMobileMenuOpen = false
                                onNavigate(Screen.Profile)
                            }
                        )
                    } else {
                        MobileNavItem(
                            title = "Masuk / Login",
                            isSelected = currentScreen == Screen.Login,
                            onClick = {
                                isMobileMenuOpen = false
                                onNavigate(Screen.Login)
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // CTA Button
                    Button(
                        onClick = {
                            isMobileMenuOpen = false
                            onConsultationClick()
                        },
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues(),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(BrandTeal, BrandGreen)
                                    ),
                                    shape = RoundedCornerShape(50)
                                )
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Konsultasi Strategis",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontFamily = getDmSansFontFamily(),
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }
        } else {
            // Desktop Navbar (>= 840dp) with Row and Active Indicators
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(horizontal = 48.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Logo Section (Rule 10: SekotaBrandLogo)
                Box(
                    modifier = Modifier.clickable { onNavigate(Screen.Landing) }
                ) {
                    SekotaBrandLogo(
                        iconSize = 38.dp,
                        textSize = 26.sp
                    )
                }

                // Desktop Navigation Links
                Row(
                    horizontalArrangement = Arrangement.spacedBy(28.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Solusi
                    DesktopNavItem(
                        title = "Solusi",
                        isSelected = currentScreen == Screen.Landing && activeLandingSection == NavbarActiveSection.SOLUSI,
                        onClick = onSolusiClick
                    )

                    // Produk
                    DesktopNavItem(
                        title = "Produk",
                        isSelected = currentScreen == Screen.Landing && activeLandingSection == NavbarActiveSection.PRODUK,
                        onClick = onProdukClick
                    )

                    // E-Book
                    DesktopNavItem(
                        title = "E-Book",
                        isSelected = currentScreen == Screen.Catalog,
                        onClick = { onNavigate(Screen.Catalog) }
                    )

                    // Merchandise
                    DesktopNavItem(
                        title = "Merchandise",
                        isSelected = currentScreen == Screen.Merchandise,
                        onClick = { onNavigate(Screen.Merchandise) }
                    )

                    // Kontak
                    DesktopNavItem(
                        title = "Kontak",
                        isSelected = currentScreen == Screen.Landing && activeLandingSection == NavbarActiveSection.KONTAK,
                        onClick = onKontakClick
                    )

                    // Login / Profile
                    if (isLoggedIn) {
                        DesktopNavItem(
                            title = "Profil",
                            isSelected = currentScreen == Screen.Profile,
                            onClick = { onNavigate(Screen.Profile) }
                        )
                    } else {
                        DesktopNavItem(
                            title = "Login",
                            isSelected = currentScreen == Screen.Login,
                            onClick = { onNavigate(Screen.Login) }
                        )
                    }
                }

                // Right Section: Status Pill & CTA
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Integrated Live Sync Status
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (syncState == "Connected") Color(0xFFE8F5E9) else Color(0xFFFFF3E0))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (syncState == "Connected") BrandGreen else Color(0xFFFF9800))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (syncState == "Connected") "LIVE SYNC" else "OFFLINE",
                            fontFamily = getDmSansFontFamily(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = if (syncState == "Connected") Color(0xFF2E7D32) else Color(0xFFE65100)
                        )
                    }

                    // Action Button
                    Button(
                        onClick = onConsultationClick,
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues()
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(BrandTeal, BrandGreen)
                                    ),
                                    shape = RoundedCornerShape(50)
                                )
                                .padding(horizontal = 24.dp, vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Konsultasi Strategis",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontFamily = getDmSansFontFamily(),
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
}


@Composable
private fun DesktopNavItem(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = title,
            fontFamily = getDmSansFontFamily(),
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            fontSize = 15.sp,
            color = if (isSelected) BrandTeal else Color(0xFF4A5568)
        )
        Spacer(modifier = Modifier.height(3.dp))
        Box(
            modifier = Modifier
                .height(3.dp)
                .width(if (isSelected) 24.dp else 0.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(if (isSelected) BrandTeal else Color.Transparent)
        )
    }
}

@Composable
private fun MobileNavItem(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) BrandTeal.copy(alpha = 0.1f) else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(if (isSelected) BrandTeal else Color.Transparent)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = title,
            fontFamily = getDmSansFontFamily(),
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            fontSize = 16.sp,
            color = if (isSelected) BrandTeal else InkNavy
        )
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun NavbarPreview() {
    MaterialTheme {
        Navbar(onNavigate = {})
    }
}
