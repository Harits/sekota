package com.sekota.screens.cms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sekota.components.cms.CmsActionHeader
import com.sekota.components.cms.CmsBrandTeal
import com.sekota.components.cms.CmsInkNavy
import com.sekota.features.admin.domain.usecase.GetAdminFooterUseCase
import com.sekota.features.admin.domain.usecase.GetAdminNavigationItemsUseCase
import com.sekota.features.admin.domain.usecase.SaveAdminFooterUseCase
import com.sekota.features.admin.domain.usecase.SaveAdminNavigationItemsUseCase
import com.sekota.getDmSansFontFamily
import com.sekota.screens.AppBarManagementTab
import com.sekota.screens.FooterManagementTab
import com.sekota.ui.WindowWidth

enum class SiteConfigSubTab(val title: String) {
    APPBAR("AppBar Navigation (Main Menu)"),
    FOOTER("Global Footer & Policies")
}

/**
 * Site Configuration & Global Settings Panel.
 * Consolidates Dynamic AppBar Navigation, Global Footer Links, Legal policies, and Copyrights.
 */
@Composable
fun SiteConfigScreen(
    getNavigationItemsUseCase: GetAdminNavigationItemsUseCase,
    saveNavigationItemsUseCase: SaveAdminNavigationItemsUseCase,
    getFooterUseCase: GetAdminFooterUseCase,
    saveFooterUseCase: SaveAdminFooterUseCase,
    windowWidth: WindowWidth = WindowWidth.Expanded,
    onActionWithProductionGuard: ((String, String, Boolean, () -> Unit) -> Unit)? = null
) {
    var activeSubTab by remember { mutableStateOf(SiteConfigSubTab.APPBAR) }

    Column(modifier = Modifier.fillMaxSize()) {
        CmsActionHeader(
            title = "Site Configuration",
            subtitle = "Pusat pengaturan global: struktur tautan navigasi utama (AppBar), tautan footer, kebijakan legal, dan copyright.",
            windowWidth = windowWidth
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Sub-Tab Switcher
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SiteConfigSubTab.values().forEach { tab ->
                val isSelected = activeSubTab == tab
                Surface(
                    onClick = { activeSubTab = tab },
                    shape = RoundedCornerShape(100.dp),
                    color = if (isSelected) CmsBrandTeal else Color.White,
                    border = BorderStroke(1.dp, if (isSelected) CmsBrandTeal else Color(0xFFE2E8F0)),
                    shadowElevation = if (isSelected) 2.dp else 0.dp
                ) {
                    Text(
                        text = tab.title,
                        fontFamily = getDmSansFontFamily(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = if (isSelected) Color.White else CmsInkNavy,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            when (activeSubTab) {
                SiteConfigSubTab.APPBAR -> {
                    AppBarManagementTab(
                        getNavigationItemsUseCase = getNavigationItemsUseCase,
                        saveNavigationItemsUseCase = saveNavigationItemsUseCase,
                        windowWidth = windowWidth,
                        onActionWithProductionGuard = onActionWithProductionGuard
                    )
                }
                SiteConfigSubTab.FOOTER -> {
                    FooterManagementTab(
                        getFooterUseCase = getFooterUseCase,
                        saveFooterUseCase = saveFooterUseCase,
                        windowWidth = windowWidth,
                        onActionWithProductionGuard = onActionWithProductionGuard
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Previews
// -------------------------------------------------------------

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun SiteConfigScreenPreview() {
    MaterialTheme {
        Column(modifier = Modifier.padding(24.dp).fillMaxSize()) {
            CmsActionHeader(
                title = "Site Configuration",
                subtitle = "Pengaturan navigasi AppBar & Global Footer.",
                windowWidth = WindowWidth.Expanded
            )
        }
    }
}
