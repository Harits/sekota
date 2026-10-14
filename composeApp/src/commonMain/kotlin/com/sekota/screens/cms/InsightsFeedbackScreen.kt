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
import com.sekota.features.admin.domain.usecase.*
import com.sekota.getDmSansFontFamily
import com.sekota.screens.ClientInquiriesTab
import com.sekota.screens.LiveMetricsTab
import com.sekota.ui.WindowWidth

enum class InsightsFeedbackSubTab(val title: String) {
    LIVE_METRICS("Live Telemetry & Reader Insights"),
    INQUIRIES("Client Inquiries & Consultation Leads")
}

/**
 * Insights & Feedback Integrated Dashboard.
 * Groups telemetry live metrics alongside prospect client inquiries and consultation forms.
 */
@Composable
fun InsightsFeedbackScreen(
    getLiveMetricsUseCase: GetAdminLiveMetricsUseCase,
    saveLiveMetricsUseCase: SaveAdminLiveMetricsUseCase,
    getInquiriesUseCase: GetAdminInquiriesUseCase,
    deleteInquiryUseCase: DeleteAdminInquiryUseCase,
    getStrategicConversationUseCase: GetAdminStrategicConversationUseCase,
    saveStrategicConversationUseCase: SaveAdminStrategicConversationUseCase,
    windowWidth: WindowWidth = WindowWidth.Expanded,
    onActionWithProductionGuard: ((String, String, Boolean, () -> Unit) -> Unit)? = null
) {
    var activeSubTab by remember { mutableStateOf(InsightsFeedbackSubTab.LIVE_METRICS) }

    Column(modifier = Modifier.fillMaxSize()) {
        CmsActionHeader(
            title = "Insights & Feedback Dashboard",
            subtitle = "Pusat analisis interaksi: pantau metrik keterbacaan live dan tinjau pesan konsultasi (leads) dari klien.",
            windowWidth = windowWidth
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Sub-Tab Switcher
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            InsightsFeedbackSubTab.values().forEach { tab ->
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
                InsightsFeedbackSubTab.LIVE_METRICS -> {
                    LiveMetricsTab(
                        getLiveMetricsUseCase = getLiveMetricsUseCase,
                        saveLiveMetricsUseCase = saveLiveMetricsUseCase,
                        windowWidth = windowWidth,
                        onActionWithProductionGuard = onActionWithProductionGuard
                    )
                }
                InsightsFeedbackSubTab.INQUIRIES -> {
                    ClientInquiriesTab(
                        getInquiriesUseCase = getInquiriesUseCase,
                        deleteInquiryUseCase = deleteInquiryUseCase,
                        getStrategicConversationUseCase = getStrategicConversationUseCase,
                        saveStrategicConversationUseCase = saveStrategicConversationUseCase,
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
fun InsightsFeedbackScreenPreview() {
    MaterialTheme {
        Column(modifier = Modifier.padding(24.dp).fillMaxSize()) {
            CmsActionHeader(
                title = "Insights & Feedback Dashboard",
                subtitle = "Analisis keterbacaan live & tinjauan pesan prospek.",
                windowWidth = WindowWidth.Expanded
            )
        }
    }
}
