package com.sekota.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import com.sekota.syncService
import com.sekota.utils.selectAndReadImageFile
import com.sekota.utils.decodeBase64ToBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sekota.getDmSansFontFamily
import com.sekota.getMontserratFontFamily
import com.sekota.features.admin.data.repository.AdminRepositoryImpl
import com.sekota.features.admin.domain.model.AdminBook
import com.sekota.features.admin.domain.model.AdminLiveMetrics
import com.sekota.features.admin.domain.model.AdminMerch
import com.sekota.features.admin.domain.model.AdminProduct
import com.sekota.features.admin.domain.model.ClientInquiry
import com.sekota.features.admin.domain.model.StrategicConversationConfig
import com.sekota.features.admin.domain.model.WhySekotaConfig
import com.sekota.features.admin.domain.model.WhySekotaCard
import com.sekota.features.admin.domain.usecase.*
import com.sekota.ui.WindowWidth
import com.sekota.ui.windowWidthOf
import sekota.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import com.sekota.features.admin.domain.model.AppEnvironment
import com.sekota.features.admin.domain.model.EnvironmentConfigResolver
import com.sekota.components.EnvironmentBadge
import com.sekota.components.ContentMappingGuideDialog
import com.sekota.components.ProductionActionConfirmationDialog
import com.sekota.components.SekotaInfoVectorIcon

val InkNavy = Color(0xFF0D1F2D)
val BrandTeal = Color(0xFF00B5C8)

@Composable
fun AdminDashboardScreen(
    onLogout: () -> Unit = {}
) {
    val repository = remember { AdminRepositoryImpl() }
    val getBooksUseCase = remember { GetAdminBooksUseCase(repository) }
    val saveBookUseCase = remember { SaveAdminBookUseCase(repository) }
    val getProductsUseCase = remember { GetAdminProductsUseCase(repository) }
    val saveProductUseCase = remember { SaveAdminProductUseCase(repository) }
    val deleteProductUseCase = remember { DeleteAdminProductUseCase(repository) }
    val getMerchUseCase = remember { GetAdminMerchUseCase(repository) }
    val saveMerchUseCase = remember { SaveAdminMerchUseCase(repository) }
    val deleteMerchUseCase = remember { DeleteAdminMerchUseCase(repository) }
    val getLiveMetricsUseCase = remember { GetAdminLiveMetricsUseCase(repository) }
    val saveLiveMetricsUseCase = remember { SaveAdminLiveMetricsUseCase(repository) }
    val getInquiriesUseCase = remember { GetAdminInquiriesUseCase(repository) }
    val deleteInquiryUseCase = remember { DeleteAdminInquiryUseCase(repository) }
    val getValueLoopUseCase = remember { GetAdminValueLoopUseCase(repository) }
    val saveValueLoopUseCase = remember { SaveAdminValueLoopUseCase(repository) }
    val getCategoriesUseCase = remember { GetAdminCategoriesUseCase(repository) }
    val addCategoryUseCase = remember { AddAdminCategoryUseCase(repository) }
    val deleteCategoryUseCase = remember { DeleteAdminCategoryUseCase(repository) }
    val getWhySekotaUseCase = remember { GetAdminWhySekotaUseCase(repository) }
    val saveWhySekotaUseCase = remember { SaveAdminWhySekotaUseCase(repository) }
    val getStrategicConversationUseCase = remember { GetAdminStrategicConversationUseCase(repository) }
    val saveStrategicConversationUseCase = remember { SaveAdminStrategicConversationUseCase(repository) }
    val getFooterUseCase = remember { GetAdminFooterUseCase(repository) }
    val saveFooterUseCase = remember { SaveAdminFooterUseCase(repository) }
    val getNavigationItemsUseCase = remember { GetAdminNavigationItemsUseCase(repository) }
    val saveNavigationItemsUseCase = remember { SaveAdminNavigationItemsUseCase(repository) }

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Product Registry", "Brand & Story", "Site Config", "Insights & Feedback")

    var currentEnvironment by remember { mutableStateOf(EnvironmentConfigResolver.resolveEnvironment()) }
    var showGuideDialog by remember { mutableStateOf(false) }
    var guideInitialTab by remember { mutableStateOf(0) }

    var pendingAction by remember { mutableStateOf<(() -> Unit)?>(null) }
    var pendingActionName by remember { mutableStateOf("") }
    var pendingEntityName by remember { mutableStateOf("") }
    var isPendingDelete by remember { mutableStateOf(false) }

    val onActionWithProductionGuard: (String, String, Boolean, () -> Unit) -> Unit = { actionName, entityName, isDelete, action ->
        if (currentEnvironment.isProduction) {
            pendingActionName = actionName
            pendingEntityName = entityName
            isPendingDelete = isDelete
            pendingAction = action
        } else {
            action()
        }
    }

    if (showGuideDialog) {
        ContentMappingGuideDialog(
            initialTab = guideInitialTab,
            onDismiss = { showGuideDialog = false }
        )
    }

    if (pendingAction != null) {
        ProductionActionConfirmationDialog(
            actionName = pendingActionName,
            entityName = pendingEntityName,
            isCriticalDelete = isPendingDelete,
            onConfirm = {
                val act = pendingAction
                pendingAction = null
                act?.invoke()
            },
            onDismiss = {
                pendingAction = null
            }
        )
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(Color(0xFFFAFAFA))) {
        // M3 adaptive navigation: NavigationBar below 600dp, NavigationRail from
        // Medium up. The rail collapses to icons-only at Medium so it does not eat
        // 250dp of a 600-840dp workbench.
        val windowWidth = windowWidthOf(maxWidth)
        val isCompact = windowWidth.isCompact
        val railExpanded = windowWidth.isAtLeastExpanded

        if (isCompact) {
            // Mobile (Android Phone) Layout: Top AppBar + Content + Bottom Navigation Bar
            Column(modifier = Modifier.fillMaxSize()) {
                Surface(
                    color = InkNavy,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "SEKOTA CMS",
                            color = BrandTeal,
                            fontFamily = getMontserratFontFamily(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            modifier = Modifier.weight(1f)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            EnvironmentBadge(
                                environment = currentEnvironment,
                                onClick = {
                                    // Cycles through environments for testing/verification
                                    currentEnvironment = when (currentEnvironment) {
                                        AppEnvironment.DEVELOPMENT -> AppEnvironment.STAGING
                                        AppEnvironment.STAGING -> AppEnvironment.PRODUCTION
                                        AppEnvironment.PRODUCTION -> AppEnvironment.DEVELOPMENT
                                    }
                                }
                            )
                            IconButton(
                                onClick = {
                                    guideInitialTab = when (selectedTab) {
                                        0 -> 0 // Product Registry (Books/Suite/Merch)
                                        1 -> 1 // Brand & Story (Value Loop / Why Sekota)
                                        2 -> 6 // Site Config (AppBar & Footer)
                                        3 -> 4 // Insights & Feedback
                                        else -> 0
                                    }
                                    showGuideDialog = true
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                SekotaInfoVectorIcon(size = 18.dp, tint = Color.White.copy(alpha = 0.9f))
                            }
                            IconButton(
                                onClick = onLogout,
                                modifier = Modifier.size(36.dp)
                            ) {
                                com.sekota.components.SekotaExitVectorIcon(size = 18.dp, tint = Color(0xFFFF8A80))
                            }
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    when (selectedTab) {
                        0 -> com.sekota.screens.cms.ProductRegistryScreen(
                            getBooksUseCase, saveBookUseCase, getCategoriesUseCase, addCategoryUseCase, deleteCategoryUseCase,
                            getProductsUseCase, saveProductUseCase, deleteProductUseCase,
                            getMerchUseCase, saveMerchUseCase, deleteMerchUseCase,
                            windowWidth, onActionWithProductionGuard
                        )
                        1 -> com.sekota.screens.cms.BrandStoryScreen(
                            getValueLoopUseCase, saveValueLoopUseCase, getWhySekotaUseCase, saveWhySekotaUseCase,
                            windowWidth, onActionWithProductionGuard
                        )
                        2 -> com.sekota.screens.cms.SiteConfigScreen(
                            getNavigationItemsUseCase, saveNavigationItemsUseCase, getFooterUseCase, saveFooterUseCase,
                            windowWidth, onActionWithProductionGuard
                        )
                        3 -> com.sekota.screens.cms.InsightsFeedbackScreen(
                            getLiveMetricsUseCase, saveLiveMetricsUseCase, getInquiriesUseCase, deleteInquiryUseCase,
                            getStrategicConversationUseCase, saveStrategicConversationUseCase,
                            windowWidth, onActionWithProductionGuard
                        )
                    }
                }

                NavigationBar(
                    containerColor = InkNavy,
                    contentColor = Color.White
                ) {
                    tabs.forEachIndexed { index, title ->
                        NavigationBarItem(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            icon = {
                                com.sekota.components.SekotaCmsNavVectorIcon(
                                    index = index,
                                    size = 20.dp,
                                    tint = if (selectedTab == index) BrandTeal else Color.LightGray
                                )
                            },
                            label = {
                                Text(
                                    text = when (index) {
                                        0 -> "Registry"
                                        1 -> "Story"
                                        2 -> "Config"
                                        3 -> "Insights"
                                        else -> title.split(" ").first()
                                    },
                                    fontFamily = getDmSansFontFamily(),
                                    fontSize = 11.sp,
                                    maxLines = 1,
                                    color = if (selectedTab == index) BrandTeal else Color.LightGray
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = BrandTeal,
                                selectedTextColor = BrandTeal,
                                indicatorColor = InkNavy.copy(alpha = 0.6f),
                                unselectedIconColor = Color.LightGray,
                                unselectedTextColor = Color.LightGray
                            )
                        )
                    }
                }
            }
        } else {
            // Desktop / Tablet Layout: Left Navigation Rail + Right Content Workbench
            Row(modifier = Modifier.fillMaxSize()) {
                NavigationRail(
                    modifier = Modifier.width(if (railExpanded) 250.dp else 88.dp).fillMaxHeight(),
                    containerColor = InkNavy,
                    contentColor = Color.White
                ) {
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = if (railExpanded) "ADMIN PANEL" else "CMS",
                        color = BrandTeal,
                        fontFamily = getMontserratFontFamily(),
                        fontWeight = FontWeight.Bold,
                        fontSize = if (railExpanded) 20.sp else 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = if (railExpanded) 24.dp else 8.dp, vertical = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    tabs.forEachIndexed { index, title ->
                        NavigationRailItem(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            icon = {
                                com.sekota.components.SekotaCmsNavVectorIcon(
                                    index = index,
                                    size = 24.dp,
                                    tint = if (selectedTab == index) BrandTeal else Color.LightGray
                                )
                            },
                            label = {
                                Text(
                                    text = if (railExpanded) title else title.split(" ").first(),
                                    fontFamily = getDmSansFontFamily(),
                                    fontSize = if (railExpanded) 14.sp else 10.sp,
                                    textAlign = TextAlign.Center,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = if (selectedTab == index) BrandTeal else Color.LightGray
                                )
                            },
                            colors = NavigationRailItemDefaults.colors(
                                selectedIconColor = BrandTeal,
                                selectedTextColor = BrandTeal,
                                indicatorColor = InkNavy.copy(alpha = 0.5f),
                                unselectedIconColor = Color.LightGray,
                                unselectedTextColor = Color.LightGray
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp).fillMaxWidth()
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    OutlinedButton(
                        onClick = onLogout,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        shape = RoundedCornerShape(100.dp),
                        contentPadding = if (railExpanded) ButtonDefaults.ContentPadding else PaddingValues(4.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = if (railExpanded) 24.dp else 8.dp, vertical = 24.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            com.sekota.components.SekotaExitVectorIcon(size = 18.dp, tint = Color(0xFFFF8A80))
                            if (railExpanded) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Sign Out",
                                    fontFamily = getDmSansFontFamily(),
                                    fontSize = 13.sp,
                                    maxLines = 1,
                                    color = Color(0xFFFF8A80)
                                )
                            }
                        }
                    }
                }

                Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
                    // Top Workbench Control Bar for Desktop / Tablet
                    Surface(
                        color = Color.White,
                        tonalElevation = 2.dp,
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Text(
                                    text = "Sekota Management Console",
                                    fontFamily = getMontserratFontFamily(),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = InkNavy
                                )
                                EnvironmentBadge(
                                    environment = currentEnvironment,
                                    onClick = {
                                        currentEnvironment = when (currentEnvironment) {
                                            AppEnvironment.DEVELOPMENT -> AppEnvironment.STAGING
                                            AppEnvironment.STAGING -> AppEnvironment.PRODUCTION
                                            AppEnvironment.PRODUCTION -> AppEnvironment.DEVELOPMENT
                                        }
                                    }
                                )
                            }

                            FilledTonalButton(
                                onClick = {
                                    guideInitialTab = when (selectedTab) {
                                        0 -> 0 // Product Registry
                                        1 -> 1 // Brand & Story
                                        2 -> 6 // Site Config
                                        3 -> 4 // Insights & Feedback
                                        else -> 0
                                    }
                                    showGuideDialog = true
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.filledTonalButtonColors(
                                    containerColor = Color(0xFFF1F5F9),
                                    contentColor = InkNavy
                                ),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    SekotaInfoVectorIcon(size = 16.dp, tint = BrandTeal)
                                    Text(
                                        text = "Panduan Konten & Mapping",
                                        fontFamily = getDmSansFontFamily(),
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }
                    }

                    Box(modifier = Modifier.weight(1f).fillMaxWidth().padding(32.dp)) {
                        when (selectedTab) {
                            0 -> com.sekota.screens.cms.ProductRegistryScreen(
                                getBooksUseCase, saveBookUseCase, getCategoriesUseCase, addCategoryUseCase, deleteCategoryUseCase,
                                getProductsUseCase, saveProductUseCase, deleteProductUseCase,
                                getMerchUseCase, saveMerchUseCase, deleteMerchUseCase,
                                windowWidth, onActionWithProductionGuard
                            )
                            1 -> com.sekota.screens.cms.BrandStoryScreen(
                                getValueLoopUseCase, saveValueLoopUseCase, getWhySekotaUseCase, saveWhySekotaUseCase,
                                windowWidth, onActionWithProductionGuard
                            )
                            2 -> com.sekota.screens.cms.SiteConfigScreen(
                                getNavigationItemsUseCase, saveNavigationItemsUseCase, getFooterUseCase, saveFooterUseCase,
                                windowWidth, onActionWithProductionGuard
                            )
                            3 -> com.sekota.screens.cms.InsightsFeedbackScreen(
                                getLiveMetricsUseCase, saveLiveMetricsUseCase, getInquiriesUseCase, deleteInquiryUseCase,
                                getStrategicConversationUseCase, saveStrategicConversationUseCase,
                                windowWidth, onActionWithProductionGuard
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview(device = DESKTOP, showBackground = true)
@Composable
fun AdminDashboardPreview() {
    MaterialTheme {
        AdminDashboardScreen()
    }
}
