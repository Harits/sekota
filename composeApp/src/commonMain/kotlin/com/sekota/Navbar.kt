package com.sekota

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.layout.onSizeChanged
import com.sekota.components.SekotaButton
import com.sekota.components.SekotaButtonSize
import com.sekota.components.SekotaButtonVariant
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import sekota.composeapp.generated.resources.Res
import sekota.composeapp.generated.resources.icon_sekota

enum class NavbarActiveSection {
    SOLUSI, PRODUK, KONTAK, NONE
}

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
                color = SekotaColors.Azure11
            )
            Box(
                modifier = Modifier
                    .padding(start = 2.dp, top = 6.dp)
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.linearGradient(
                            listOf(SekotaColors.Cyan39, SekotaColors.BrandGreen)
                        )
                    )
            )
        }
    }
}

@Composable
fun Navbar(
    items: List<com.sekota.features.navigation.domain.model.NavigationItem> = com.sekota.features.navigation.domain.model.NavigationItem.CANONICAL_ITEMS,
    currentScreen: Screen = Screen.Landing,
    activeLandingSection: NavbarActiveSection = NavbarActiveSection.SOLUSI,
    isLoggedIn: Boolean = false,
    syncState: String = "Connected",
    selectedLanguage: String = "BAHASA INDONESIA",
    onLanguageChange: (String) -> Unit = {},
    modifier: Modifier = Modifier,
    onNavigate: (Screen) -> Unit,
    onSolusiClick: () -> Unit = {},
    onProdukClick: () -> Unit = {},
    onKontakClick: () -> Unit = {},
    onConsultationClick: () -> Unit = {}
) {
    // Rule 10: Interactive state hoisted outside nested BoxWithConstraints for Wasm canvas determinism
    var isMobileMenuOpen by remember { mutableStateOf(false) }

    val isEn = selectedLanguage.equals("ENGLISH", ignoreCase = true)

    val resolveNavLabel: (com.sekota.features.navigation.domain.model.NavigationItem) -> String = { item ->
        val byId = com.sekota.localization.LocalizationManager.getString(item.id)
        if (byId != item.id) byId else com.sekota.localization.LocalizationManager.getString(item.label)
    }

    val sortedNavItems = remember(items, selectedLanguage) {
        items.filter { it.isVisible }.sortedBy { it.order }
    }

    val handleNavClick: (String) -> Unit = { route ->
        when (val dest = com.sekota.features.navigation.domain.model.NavigationRouter.parseDestination(route)) {
            is com.sekota.features.navigation.domain.model.AppDestination.SolusiSection -> onSolusiClick()
            is com.sekota.features.navigation.domain.model.AppDestination.ProdukSection -> onProdukClick()
            is com.sekota.features.navigation.domain.model.AppDestination.KontakSection -> onKontakClick()
            is com.sekota.features.navigation.domain.model.AppDestination.Catalog -> onNavigate(Screen.Catalog)
            is com.sekota.features.navigation.domain.model.AppDestination.Merchandise -> onNavigate(Screen.Merchandise)
            is com.sekota.features.navigation.domain.model.AppDestination.Profile -> onNavigate(Screen.Profile)
            is com.sekota.features.navigation.domain.model.AppDestination.Login -> onNavigate(Screen.Login)
            is com.sekota.features.navigation.domain.model.AppDestination.Signup -> onNavigate(Screen.Signup)
            is com.sekota.features.navigation.domain.model.AppDestination.Admin -> onNavigate(Screen.Admin)
            is com.sekota.features.navigation.domain.model.AppDestination.Landing -> onNavigate(Screen.Landing)
            is com.sekota.features.navigation.domain.model.AppDestination.External -> {}
            is com.sekota.features.navigation.domain.model.AppDestination.BookReader -> onNavigate(Screen.BookReader(dest.bookId, dest.chapterIndex))
            is com.sekota.features.navigation.domain.model.AppDestination.Unknown -> onNavigate(Screen.Landing)
        }
    }

    val isRouteActive: (String) -> Boolean = { route ->
        when (com.sekota.features.navigation.domain.model.NavigationRouter.parseDestination(route)) {
            is com.sekota.features.navigation.domain.model.AppDestination.SolusiSection ->
                currentScreen == Screen.Landing && activeLandingSection == NavbarActiveSection.SOLUSI
            is com.sekota.features.navigation.domain.model.AppDestination.ProdukSection ->
                currentScreen == Screen.Landing && activeLandingSection == NavbarActiveSection.PRODUK
            is com.sekota.features.navigation.domain.model.AppDestination.KontakSection ->
                currentScreen == Screen.Landing && activeLandingSection == NavbarActiveSection.KONTAK
            is com.sekota.features.navigation.domain.model.AppDestination.Catalog ->
                currentScreen == Screen.Catalog
            is com.sekota.features.navigation.domain.model.AppDestination.Merchandise ->
                currentScreen == Screen.Merchandise
            is com.sekota.features.navigation.domain.model.AppDestination.Profile ->
                currentScreen == Screen.Profile
            is com.sekota.features.navigation.domain.model.AppDestination.Login ->
                currentScreen == Screen.Login
            is com.sekota.features.navigation.domain.model.AppDestination.BookReader ->
                false
            else -> false
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.88f))
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                drawLine(
                    color = SekotaColors.Navy05,
                    start = Offset(0f, size.height - strokeWidth / 2),
                    end = Offset(size.width, size.height - strokeWidth / 2),
                    strokeWidth = strokeWidth
                )
            }
    ) {
        val containerWidth = maxWidth
        val isCompact = containerWidth < 840.dp

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
                                .background(if (syncState == "Connected") SekotaColors.BrandGreen else Color(0xFFFF9800))
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
                                    color = SekotaColors.Azure11,
                                    start = androidx.compose.ui.geometry.Offset(4.dp.toPx(), 4.dp.toPx()),
                                    end = androidx.compose.ui.geometry.Offset(size.width - 4.dp.toPx(), size.height - 4.dp.toPx()),
                                    strokeWidth = strokeWidth,
                                    cap = androidx.compose.ui.graphics.StrokeCap.Round
                                )
                                drawLine(
                                    color = SekotaColors.Azure11,
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
                                    color = SekotaColors.Azure11,
                                    start = androidx.compose.ui.geometry.Offset(startX, 5.dp.toPx()),
                                    end = androidx.compose.ui.geometry.Offset(endX, 5.dp.toPx()),
                                    strokeWidth = strokeWidth,
                                    cap = androidx.compose.ui.graphics.StrokeCap.Round
                                )
                                // Middle bar
                                drawLine(
                                    color = SekotaColors.Azure11,
                                    start = androidx.compose.ui.geometry.Offset(startX, 12.dp.toPx()),
                                    end = androidx.compose.ui.geometry.Offset(endX, 12.dp.toPx()),
                                    strokeWidth = strokeWidth,
                                    cap = androidx.compose.ui.graphics.StrokeCap.Round
                                )
                                // Bottom bar
                                drawLine(
                                    color = SekotaColors.Azure11,
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
                        .background(Color.White.copy(alpha = 0.96f))
                        .drawBehind {
                            val strokeWidth = 1.dp.toPx()
                            drawLine(
                                color = SekotaColors.Navy05,
                                start = Offset(0f, size.height - strokeWidth / 2),
                                end = Offset(size.width, size.height - strokeWidth / 2),
                                strokeWidth = strokeWidth
                            )
                        }
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Dynamic Navigation Items
                    sortedNavItems.forEach { navItem ->
                        MobileNavItem(
                            title = resolveNavLabel(navItem),
                            isSelected = isRouteActive(navItem.route),
                            onClick = {
                                isMobileMenuOpen = false
                                handleNavClick(navItem.route)
                            }
                        )
                    }

                    // Profile or Login
                    if (isLoggedIn) {
                        MobileNavItem(
                            title = com.sekota.localization.LocalizationManager.getString("nav_profile"),
                            isSelected = currentScreen == Screen.Profile,
                            onClick = {
                                isMobileMenuOpen = false
                                onNavigate(Screen.Profile)
                            }
                        )
                    } else {
                        MobileNavItem(
                            title = com.sekota.localization.LocalizationManager.getString("nav_login"),
                            isSelected = currentScreen == Screen.Login,
                            onClick = {
                                isMobileMenuOpen = false
                                onNavigate(Screen.Login)
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Language Switcher in Mobile Drawer
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF1F5F9))
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isEn) "Language" else "Bahasa",
                            fontSize = 13.sp,
                            fontFamily = getDmSansFontFamily(),
                            fontWeight = FontWeight.Medium,
                            color = SekotaColors.Azure11
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "ID",
                                fontSize = 12.sp,
                                fontWeight = if (!isEn) FontWeight.Bold else FontWeight.Medium,
                                color = if (!isEn) SekotaColors.Cyan39 else Color(0xFF64748B),
                                fontFamily = getDmSansFontFamily(),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .clickable {
                                        com.sekota.localization.LocalizationManager.setLocale(com.sekota.localization.AppLocale.ID)
                                        onLanguageChange("BAHASA INDONESIA")
                                    }
                                    .padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                            Text(
                                text = "|",
                                fontSize = 12.sp,
                                color = Color(0xFFCBD5E1),
                                modifier = Modifier.padding(horizontal = 2.dp)
                            )
                            Text(
                                text = "EN",
                                fontSize = 12.sp,
                                fontWeight = if (isEn) FontWeight.Bold else FontWeight.Medium,
                                color = if (isEn) SekotaColors.Cyan39 else Color(0xFF64748B),
                                fontFamily = getDmSansFontFamily(),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .clickable {
                                        com.sekota.localization.LocalizationManager.setLocale(com.sekota.localization.AppLocale.EN)
                                        onLanguageChange("ENGLISH")
                                    }
                                    .padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // CTA Button
                    SekotaButton(
                        text = com.sekota.localization.LocalizationManager.getString("nav_cta_consultation"),
                        variant = SekotaButtonVariant.Gradient,
                        size = SekotaButtonSize.Medium,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            isMobileMenuOpen = false
                            onConsultationClick()
                        }
                    )
                }
            }
        } else {
            // Desktop Navbar (>= 840dp) with Row and Active Indicators
            val isMediumDesktop = containerWidth < 1120.dp
            val horizontalNavPadding = if (isMediumDesktop) 20.dp else 48.dp
            val navItemSpacing = if (isMediumDesktop) 14.dp else 28.dp

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(horizontal = horizontalNavPadding),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Logo Section (Rule 10: SekotaBrandLogo)
                Box(
                    modifier = Modifier.clickable { onNavigate(Screen.Landing) }
                ) {
                    SekotaBrandLogo(
                        iconSize = if (isMediumDesktop) 32.dp else 38.dp,
                        textSize = if (isMediumDesktop) 22.sp else 26.sp
                    )
                }

                // Desktop Navigation Links (Dynamic)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(navItemSpacing),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    sortedNavItems.forEach { navItem ->
                        DesktopNavItem(
                            title = resolveNavLabel(navItem),
                            isSelected = isRouteActive(navItem.route),
                            onClick = { handleNavClick(navItem.route) }
                        )
                    }

                    // Login / Profile
                    if (isLoggedIn) {
                        DesktopNavItem(
                            title = com.sekota.localization.LocalizationManager.getString("nav_profile_short"),
                            isSelected = currentScreen == Screen.Profile,
                            onClick = { onNavigate(Screen.Profile) }
                        )
                    } else {
                        DesktopNavItem(
                            title = com.sekota.localization.LocalizationManager.getString("nav_login_short"),
                            isSelected = currentScreen == Screen.Login,
                            onClick = { onNavigate(Screen.Login) }
                        )
                    }
                }

                // Right Section: Language Switcher, Status Pill & CTA
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(if (isMediumDesktop) 8.dp else 12.dp)
                ) {
                    // Language Switcher (ID | EN)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFF1F5F9))
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "ID",
                            fontSize = 11.sp,
                            fontWeight = if (!isEn) FontWeight.Bold else FontWeight.Medium,
                            color = if (!isEn) SekotaColors.Cyan39 else Color(0xFF64748B),
                            fontFamily = getDmSansFontFamily(),
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .clickable {
                                    com.sekota.localization.LocalizationManager.setLocale(com.sekota.localization.AppLocale.ID)
                                    onLanguageChange("BAHASA INDONESIA")
                                }
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                        Text(
                            text = "|",
                            fontSize = 11.sp,
                            color = Color(0xFFCBD5E1),
                            modifier = Modifier.padding(horizontal = 2.dp)
                        )
                        Text(
                            text = "EN",
                            fontSize = 11.sp,
                            fontWeight = if (isEn) FontWeight.Bold else FontWeight.Medium,
                            color = if (isEn) SekotaColors.Cyan39 else Color(0xFF64748B),
                            fontFamily = getDmSansFontFamily(),
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .clickable {
                                    com.sekota.localization.LocalizationManager.setLocale(com.sekota.localization.AppLocale.EN)
                                    onLanguageChange("ENGLISH")
                                }
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }

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
                                .background(if (syncState == "Connected") SekotaColors.BrandGreen else Color(0xFFFF9800))
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

                    // Action Button with nowrap guarantee and responsive padding
                    SekotaButton(
                        text = com.sekota.localization.LocalizationManager.getString("nav_cta_consultation"),
                        variant = SekotaButtonVariant.Gradient,
                        size = if (isMediumDesktop) SekotaButtonSize.Small else SekotaButtonSize.Medium,
                        onClick = onConsultationClick
                    )
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
            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
            fontSize = 15.sp,
            color = if (isSelected) SekotaColors.CyanActive else SekotaColors.Ink70
        )
        Spacer(modifier = Modifier.height(3.dp))
        Box(
            modifier = Modifier
                .height(3.dp)
                .width(if (isSelected) 24.dp else 0.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(if (isSelected) SekotaColors.CyanActive else Color.Transparent)
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
            .background(if (isSelected) SekotaColors.CyanActive.copy(alpha = 0.08f) else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(if (isSelected) SekotaColors.CyanActive else Color.Transparent)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = title,
            fontFamily = getDmSansFontFamily(),
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            fontSize = 16.sp,
            color = if (isSelected) SekotaColors.CyanActive else SekotaColors.Azure11
        )
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun NavbarPreview() {
    SekotaTheme {
        Navbar(onNavigate = {})
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun NavbarGlassmorphicScrolledPreview() {
    SekotaTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(SekotaColors.Cyan39.copy(alpha = 0.25f), SekotaColors.PageSurface)
                    )
                )
                .padding(bottom = 80.dp)
        ) {
            Navbar(
                currentScreen = Screen.Landing,
                activeLandingSection = NavbarActiveSection.SOLUSI,
                onNavigate = {}
            )
        }
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun NavbarDesktopPreview() {
    SekotaTheme {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            // BERANDA active
            Navbar(
                currentScreen = Screen.Landing,
                activeLandingSection = NavbarActiveSection.NONE,
                onNavigate = {}
            )
            // SOLUSI active
            Navbar(
                currentScreen = Screen.Landing,
                activeLandingSection = NavbarActiveSection.SOLUSI,
                onNavigate = {}
            )
            // PRODUK active
            Navbar(
                currentScreen = Screen.Landing,
                activeLandingSection = NavbarActiveSection.PRODUK,
                onNavigate = {}
            )
            // E-BOOK active
            Navbar(
                currentScreen = Screen.Catalog,
                onNavigate = {}
            )
            // KONTAK active
            Navbar(
                currentScreen = Screen.Landing,
                activeLandingSection = NavbarActiveSection.KONTAK,
                onNavigate = {}
            )
        }
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun NavbarDynamicVariationsDesktopPreview() {
    val threeItems = listOf(
        com.sekota.features.navigation.domain.model.NavigationItem("1", "Solusi", "#solusi", order = 1),
        com.sekota.features.navigation.domain.model.NavigationItem("2", "Produk", "#produk", order = 2),
        com.sekota.features.navigation.domain.model.NavigationItem("3", "Kontak", "#kontak", order = 3)
    )
    val fiveItems = com.sekota.features.navigation.domain.model.NavigationItem.CANONICAL_ITEMS
    val eightItems = listOf(
        com.sekota.features.navigation.domain.model.NavigationItem("1", "Solusi", "#solusi", order = 1),
        com.sekota.features.navigation.domain.model.NavigationItem("2", "Produk", "#produk", order = 2),
        com.sekota.features.navigation.domain.model.NavigationItem("3", "E-Book", "/catalog", order = 3),
        com.sekota.features.navigation.domain.model.NavigationItem("4", "Merchandise", "/merchandise", order = 4),
        com.sekota.features.navigation.domain.model.NavigationItem("5", "Insights", "#insights", order = 5),
        com.sekota.features.navigation.domain.model.NavigationItem("6", "Komunitas", "#community", order = 6),
        com.sekota.features.navigation.domain.model.NavigationItem("7", "Karir", "#careers", order = 7),
        com.sekota.features.navigation.domain.model.NavigationItem("8", "Kontak", "#kontak", order = 8)
    )

    SekotaTheme {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("3 Navigation Items:", fontWeight = FontWeight.Bold, modifier = Modifier.padding(8.dp))
            Navbar(items = threeItems, onNavigate = {})
            
            Text("5 Canonical Navigation Items:", fontWeight = FontWeight.Bold, modifier = Modifier.padding(8.dp))
            Navbar(items = fiveItems, onNavigate = {})

            Text("8 Extended Navigation Items (Overflow Stress Test):", fontWeight = FontWeight.Bold, modifier = Modifier.padding(8.dp))
            Navbar(items = eightItems, onNavigate = {})
        }
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun NavbarMobileDrawerPreview() {
    SekotaTheme {
        Box(modifier = Modifier.width(360.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.96f))
                    .border(1.dp, SekotaColors.Navy05)
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                MobileNavItem(
                    title = "Solusi",
                    isSelected = true,
                    onClick = {}
                )
                MobileNavItem(
                    title = "Produk",
                    isSelected = false,
                    onClick = {}
                )
                MobileNavItem(
                    title = "E-Book",
                    isSelected = false,
                    onClick = {}
                )
                MobileNavItem(
                    title = "Merchandise",
                    isSelected = false,
                    onClick = {}
                )
                MobileNavItem(
                    title = "Kontak",
                    isSelected = false,
                    onClick = {}
                )
                MobileNavItem(
                    title = "Masuk / Login",
                    isSelected = false,
                    onClick = {}
                )
                Spacer(modifier = Modifier.height(4.dp))
                SekotaButton(
                    text = "Konsultasi Strategis",
                    variant = SekotaButtonVariant.Gradient,
                    size = SekotaButtonSize.Medium,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {}
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NavItemPreview() {
    SekotaTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Desktop Nav Items:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                DesktopNavItem(
                    title = "Selected Item",
                    isSelected = true,
                    onClick = {}
                )
                DesktopNavItem(
                    title = "Unselected Item",
                    isSelected = false,
                    onClick = {}
                )
            }
            Text("Mobile Nav Items:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                MobileNavItem(
                    title = "Selected Mobile Item",
                    isSelected = true,
                    onClick = {}
                )
                MobileNavItem(
                    title = "Unselected Mobile Item",
                    isSelected = false,
                    onClick = {}
                )
            }
        }
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun NavbarMultiLanguageDesktopPreview() {
    SekotaTheme {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("Bahasa Indonesia (Default):", fontWeight = FontWeight.Bold, modifier = Modifier.padding(8.dp))
            Navbar(
                selectedLanguage = "BAHASA INDONESIA",
                onNavigate = {}
            )

            Text("English (Localized):", fontWeight = FontWeight.Bold, modifier = Modifier.padding(8.dp))
            Navbar(
                selectedLanguage = "ENGLISH",
                onNavigate = {}
            )
        }
    }
}


