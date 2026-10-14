package com.sekota.screens.cms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import com.sekota.features.admin.domain.usecase.*
import com.sekota.getDmSansFontFamily
import com.sekota.screens.ValueLoopTab
import com.sekota.screens.WhySekotaTab
import com.sekota.ui.WindowWidth

enum class BrandStorySubTab(val title: String) {
    VALUE_LOOP("Strategic Value Loop (01-05)"),
    WHY_SEKOTA("Why Sekota (Pilar Keunggulan)")
}

/**
 * Brand & Story Workspace: Centralizes marketing narrative content, closed loop steps, and brand value pillars.
 */
@Composable
fun BrandStoryScreen(
    getValueLoopUseCase: GetAdminValueLoopUseCase,
    saveValueLoopUseCase: SaveAdminValueLoopUseCase,
    getWhySekotaUseCase: GetAdminWhySekotaUseCase,
    saveWhySekotaUseCase: SaveAdminWhySekotaUseCase,
    windowWidth: WindowWidth = WindowWidth.Expanded,
    onActionWithProductionGuard: ((String, String, Boolean, () -> Unit) -> Unit)? = null
) {
    var activeSubTab by remember { mutableStateOf(BrandStorySubTab.VALUE_LOOP) }

    Column(modifier = Modifier.fillMaxSize()) {
        CmsActionHeader(
            title = "Brand & Story Workspace",
            subtitle = "Pusat tata kelola narasi pemasaran, siklus terintegrasi Solusi Kami, dan pilar keunggulan Sekota.",
            windowWidth = windowWidth
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Sub-Tab Segmented Switcher with horizontal scroll support on compact screens
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            BrandStorySubTab.values().forEach { tab ->
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
                        fontSize = if (windowWidth.isCompact) 12.sp else 13.sp,
                        color = if (isSelected) Color.White else CmsInkNavy,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            when (activeSubTab) {
                BrandStorySubTab.VALUE_LOOP -> {
                    ValueLoopTab(
                        getValueLoopUseCase = getValueLoopUseCase,
                        saveValueLoopUseCase = saveValueLoopUseCase,
                        windowWidth = windowWidth,
                        onActionWithProductionGuard = onActionWithProductionGuard
                    )
                }
                BrandStorySubTab.WHY_SEKOTA -> {
                    WhySekotaTab(
                        getWhySekotaUseCase = getWhySekotaUseCase,
                        saveWhySekotaUseCase = saveWhySekotaUseCase,
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
fun BrandStoryScreenPreview() {
    MaterialTheme {
        Column(modifier = Modifier.padding(24.dp).fillMaxSize()) {
            CmsActionHeader(
                title = "Brand & Story Workspace",
                subtitle = "Pusat tata kelola narasi pemasaran dan pilar keunggulan Sekota.",
                windowWidth = WindowWidth.Expanded
            )
        }
    }
}
