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

/**
 * CMS workbench section header. A 32sp title next to a pill button needs roughly
 * 520dp; below that the action drops onto its own full-width row so neither the
 * heading nor the button is clipped.
 */
@Composable
private fun WorkbenchHeader(
    title: String,
    subtitle: String,
    windowWidth: WindowWidth,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    secondaryLabel: String? = null,
    onSecondaryAction: (() -> Unit)? = null
) {
    val isCompact = windowWidth.isCompact
    val heading = @Composable { modifier: Modifier ->
        Column(modifier = modifier) {
            Text(
                text = title,
                fontFamily = getMontserratFontFamily(),
                fontWeight = FontWeight.Bold,
                fontSize = if (isCompact) 24.sp else 32.sp,
                lineHeight = if (isCompact) 30.sp else 40.sp,
                color = InkNavy
            )
            Text(
                text = subtitle,
                fontFamily = getDmSansFontFamily(),
                color = Color.Gray,
                fontSize = if (isCompact) 13.sp else 14.sp
            )
        }
    }
    val action = @Composable { modifier: Modifier ->
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (secondaryLabel != null && onSecondaryAction != null) {
                OutlinedButton(
                    onClick = onSecondaryAction,
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = InkNavy),
                    modifier = if (isCompact) Modifier.weight(1f) else Modifier
                ) {
                    Text(secondaryLabel, fontFamily = getDmSansFontFamily(), fontSize = 13.sp, maxLines = 1)
                }
            }
            if (actionLabel != null && onAction != null) {
                Button(
                    onClick = onAction,
                    colors = ButtonDefaults.buttonColors(containerColor = BrandTeal),
                    shape = RoundedCornerShape(50),
                    modifier = if (isCompact) Modifier.weight(1f) else Modifier
                ) {
                    Text("+", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(actionLabel, fontFamily = getDmSansFontFamily(), maxLines = 1)
                }
            }
        }
    }

    if (isCompact) {
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            heading(Modifier.fillMaxWidth())
            action(Modifier.fillMaxWidth())
        }
    } else {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // weight(1f) keeps the heading from starving the button of width.
            heading(Modifier.weight(1f).padding(end = 16.dp))
            action(Modifier)
        }
    }
}

@Composable
fun BooksRegistryTab(
    getBooksUseCase: GetAdminBooksUseCase,
    saveBookUseCase: SaveAdminBookUseCase,
    getCategoriesUseCase: GetAdminCategoriesUseCase = remember { GetAdminCategoriesUseCase(com.sekota.features.admin.data.repository.AdminRepositoryImpl()) },
    addCategoryUseCase: AddAdminCategoryUseCase = remember { AddAdminCategoryUseCase(com.sekota.features.admin.data.repository.AdminRepositoryImpl()) },
    deleteCategoryUseCase: DeleteAdminCategoryUseCase = remember { DeleteAdminCategoryUseCase(com.sekota.features.admin.data.repository.AdminRepositoryImpl()) },
    windowWidth: WindowWidth = WindowWidth.Expanded,
    onActionWithProductionGuard: ((String, String, Boolean, () -> Unit) -> Unit)? = null
) {
    var books by remember { mutableStateOf<List<AdminBook>>(emptyList()) }
    var categories by remember { mutableStateOf<List<String>>(emptyList()) }
    var newCategoryInput by remember { mutableStateOf("") }

    // UC-CMS-03 "Kelola Tampilan Web": operator-owned presentation metadata.
    // Book identity (title/author/ISBN/cover) and lifecycle (create/delete) are
    // owned by bookinteractiontool; this tab is read-only for those.
    var showWebDialog by remember { mutableStateOf(false) }
    var webBook by remember { mutableStateOf<AdminBook?>(null) }
    var webCategory by remember { mutableStateOf("") }
    var webDescription by remember { mutableStateOf("") }
    var webPdfUrl by remember { mutableStateOf("") }
    var webReadingTime by remember { mutableStateOf("") }
    var webPages by remember { mutableStateOf("") }
    var webYear by remember { mutableStateOf("") }
    var webLanguage by remember { mutableStateOf("") }
    var webPublishedDate by remember { mutableStateOf("") }
    var webWhatsInside by remember { mutableStateOf<List<com.sekota.features.admin.domain.model.AdminWhatsInsideItem>>(emptyList()) }
    var webError by remember { mutableStateOf<String?>(null) }
    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        books = getBooksUseCase()
        categories = getCategoriesUseCase()
    }

    // UC-CMS-03 / WP-026: web presentation metadata editor.
    if (showWebDialog && webBook != null) {
        val target = webBook!!
        AlertDialog(
            onDismissRequest = { showWebDialog = false; webBook = null },
            title = {
                Column {
                    Text(
                        text = "\u270F\uFE0F Kelola Tampilan Web",
                        fontFamily = getMontserratFontFamily(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = InkNavy
                    )
                    Text(
                        text = target.title,
                        fontFamily = getDmSansFontFamily(),
                        fontSize = 13.sp,
                        color = Color.Gray,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    webError?.let {
                        Text(it, color = Color(0xFFE53E3E), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Text(
                        "KATEGORI KATALOG",
                        fontFamily = getDmSansFontFamily(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = Color.Gray,
                        letterSpacing = 1.sp
                    )
                    @OptIn(ExperimentalLayoutApi::class)
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        categories.forEach { cat ->
                            InputChip(
                                selected = webCategory.equals(cat, ignoreCase = true),
                                onClick = { webCategory = cat; webError = null },
                                label = { Text(cat, fontFamily = getDmSansFontFamily(), fontSize = 12.sp) },
                                trailingIcon = {
                                    IconButton(
                                        onClick = {
                                            scope.launch {
                                                val res = deleteCategoryUseCase(cat)
                                                if (res.isSuccess) {
                                                    categories = res.getOrDefault(categories)
                                                    if (webCategory.equals(cat, ignoreCase = true)) {
                                                        webCategory = categories.firstOrNull() ?: ""
                                                    }
                                                }
                                            }
                                        },
                                        modifier = Modifier.size(16.dp)
                                    ) {
                                        Text("\u2715", fontSize = 11.sp, color = Color.Gray)
                                    }
                                },
                                colors = InputChipDefaults.inputChipColors(
                                    containerColor = Color(0xFFF1F5F9),
                                    labelColor = Color(0xFF334155),
                                    selectedContainerColor = BrandTeal.copy(alpha = 0.18f),
                                    selectedLabelColor = InkNavy
                                )
                            )
                        }
                    }

                    // Add Category Input Field
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = newCategoryInput,
                            onValueChange = { newCategoryInput = it },
                            placeholder = { Text("Tambah kategori baru...", fontSize = 12.sp, fontFamily = getDmSansFontFamily()) },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        Button(
                            onClick = {
                                val trimmed = newCategoryInput.trim()
                                if (trimmed.isNotBlank()) {
                                    scope.launch {
                                        val res = addCategoryUseCase(trimmed)
                                        if (res.isSuccess) {
                                            categories = res.getOrDefault(categories)
                                            webCategory = trimmed
                                            newCategoryInput = ""
                                        }
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = BrandTeal),
                            shape = RoundedCornerShape(8.dp),
                            enabled = newCategoryInput.isNotBlank()
                        ) {
                            Text("+ Tambah", fontFamily = getDmSansFontFamily(), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    OutlinedTextField(
                        value = webDescription,
                        onValueChange = { webDescription = it; webError = null },
                        label = { Text("Deskripsi Editorial", fontFamily = getDmSansFontFamily()) },
                        minLines = 2,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = webPdfUrl,
                        onValueChange = { webPdfUrl = it; webError = null },
                        label = { Text("URL PDF / Reader", fontFamily = getDmSansFontFamily()) },
                        placeholder = { Text("https://sekota.id/reader/${target.id}") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    val durationField = @Composable { modifier: Modifier ->
                        OutlinedTextField(
                            value = webReadingTime,
                            onValueChange = { webReadingTime = it; webError = null },
                            label = { Text("Durasi Baca", fontFamily = getDmSansFontFamily()) },
                            placeholder = { Text("3H 45M") },
                            singleLine = true,
                            modifier = modifier
                        )
                    }
                    val pagesField = @Composable { modifier: Modifier ->
                        OutlinedTextField(
                            value = webPages,
                            onValueChange = { webPages = it.filter { c -> c.isDigit() }; webError = null },
                            label = { Text("Jumlah Halaman", fontFamily = getDmSansFontFamily()) },
                            singleLine = true,
                            isError = webPages.isNotBlank() && webPages.toIntOrNull() == null,
                            modifier = modifier
                        )
                    }
                    if (windowWidth.isCompact) {
                        durationField(Modifier.fillMaxWidth())
                        pagesField(Modifier.fillMaxWidth())
                    } else {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            durationField(Modifier.weight(1f))
                            pagesField(Modifier.weight(1f))
                        }
                    }

                    val yearField = @Composable { modifier: Modifier ->
                        OutlinedTextField(
                            value = webYear,
                            onValueChange = { webYear = it.filter { c -> c.isDigit() }.take(4); webError = null },
                            label = { Text("Tahun Terbit", fontFamily = getDmSansFontFamily()) },
                            singleLine = true,
                            modifier = modifier
                        )
                    }
                    val languageField = @Composable { modifier: Modifier ->
                        OutlinedTextField(
                            value = webLanguage,
                            onValueChange = { webLanguage = it; webError = null },
                            label = { Text("Bahasa", fontFamily = getDmSansFontFamily()) },
                            singleLine = true,
                            modifier = modifier
                        )
                    }
                    if (windowWidth.isCompact) {
                        yearField(Modifier.fillMaxWidth())
                        languageField(Modifier.fillMaxWidth())
                    } else {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            yearField(Modifier.weight(1f))
                            languageField(Modifier.weight(1f))
                        }
                    }

                    OutlinedTextField(
                        value = webPublishedDate,
                        onValueChange = { webPublishedDate = it; webError = null },
                        label = { Text("Tanggal Terbit", fontFamily = getDmSansFontFamily()) },
                        placeholder = { Text("Nov 2025") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    HorizontalDivider(color = Color(0xFFE2E8F0))

                    // What's Inside CMS Editor (Scenario 2 AC)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "WHAT'S INSIDE (FITUR & MODUL)",
                            fontFamily = getDmSansFontFamily(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = Color.Gray,
                            letterSpacing = 1.sp
                        )
                        TextButton(
                            onClick = {
                                webWhatsInside = webWhatsInside + com.sekota.features.admin.domain.model.AdminWhatsInsideItem(
                                    title = "Poin Analisis Baru",
                                    description = "Deskripsi materi atau modul buku",
                                    iconName = "sparkle"
                                )
                            },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("+ Tambah Poin", fontFamily = getDmSansFontFamily(), fontSize = 12.sp, color = BrandTeal, fontWeight = FontWeight.Bold)
                        }
                    }

                    webWhatsInside.forEachIndexed { itemIndex, item ->
                        Surface(
                            color = Color(0xFFF8FAFC),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        "Poin #${itemIndex + 1}",
                                        fontFamily = getMontserratFontFamily(),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = InkNavy
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        // Icon selector chips with official Compose resources
                                        listOf(
                                            "sparkle" to painterResource(Res.drawable.icon_6),
                                            "frame" to painterResource(Res.drawable.icon_7),
                                            "gear" to painterResource(Res.drawable.icon_8),
                                            "leaf" to painterResource(Res.drawable.icon_5)
                                        ).forEach { (icKey, icPainter) ->
                                            val isSel = item.iconName.equals(icKey, ignoreCase = true)
                                            Surface(
                                                color = if (isSel) BrandTeal.copy(alpha = 0.2f) else Color.Transparent,
                                                shape = RoundedCornerShape(4.dp),
                                                border = BorderStroke(1.dp, if (isSel) BrandTeal else Color(0xFFCBD5E1)),
                                                modifier = Modifier.size(24.dp)
                                            ) {
                                                IconButton(
                                                    onClick = {
                                                        val list = webWhatsInside.toMutableList()
                                                        list[itemIndex] = item.copy(iconName = icKey)
                                                        webWhatsInside = list
                                                    },
                                                    modifier = Modifier.fillMaxSize().padding(2.dp)
                                                ) {
                                                    Image(
                                                        painter = icPainter,
                                                        contentDescription = icKey,
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                }
                                            }
                                        }
                                        Spacer(modifier = Modifier.width(4.dp))
                                        IconButton(
                                            onClick = {
                                                val list = webWhatsInside.toMutableList()
                                                list.removeAt(itemIndex)
                                                webWhatsInside = list
                                            },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Text("✕", fontSize = 12.sp, color = Color(0xFFE53E3E), fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                                OutlinedTextField(
                                    value = item.title,
                                    onValueChange = { newT ->
                                        val list = webWhatsInside.toMutableList()
                                        list[itemIndex] = item.copy(title = newT)
                                        webWhatsInside = list
                                    },
                                    label = { Text("Judul Poin", fontFamily = getDmSansFontFamily(), fontSize = 11.sp) },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                OutlinedTextField(
                                    value = item.description,
                                    onValueChange = { newD ->
                                        val list = webWhatsInside.toMutableList()
                                        list[itemIndex] = item.copy(description = newD)
                                        webWhatsInside = list
                                    },
                                    label = { Text("Deskripsi Poin", fontFamily = getDmSansFontFamily(), fontSize = 11.sp) },
                                    minLines = 1,
                                    maxLines = 3,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                    HorizontalDivider(color = Color(0xFFE2E8F0))

                    // Architecture rule 6: telemetry is engine-owned. Shown for
                    // context, deliberately not editable.
                    Text(
                        "TELEMETRI (READ-ONLY)",
                        fontFamily = getDmSansFontFamily(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = Color.Gray,
                        letterSpacing = 1.sp
                    )
                    Surface(
                        color = Color(0xFFF1F5F9),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            val hasRating = target.rating > 0.0
                            Text(
                                text = if (hasRating) "\u2605 ${target.rating}  \u2022  ${target.ratingCount} pembaca terdaftar"
                                       else "Publikasi Baru \u2014 belum ada rating",
                                fontFamily = getDmSansFontFamily(),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (hasRating) Color(0xFF60BD65) else Color.Gray
                            )
                            Text(
                                text = "${target.interactions} interaksi / penggunaan tool",
                                fontFamily = getDmSansFontFamily(),
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = "Dihitung oleh mesin telemetri bookinteractiontool; tidak dapat diubah operator.",
                                fontFamily = getDmSansFontFamily(),
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val pages = webPages.trim().toIntOrNull()
                        if (webPages.isNotBlank() && (pages == null || pages <= 0)) {
                            webError = "Jumlah halaman harus berupa angka lebih dari 0"
                            return@Button
                        }
                        val performSave = {
                            scope.launch {
                                val updated = target.copy(
                                    category = webCategory.trim().ifBlank { target.category },
                                    description = webDescription.trim(),
                                    pdfUrl = webPdfUrl.trim().ifBlank { null },
                                    readingTime = webReadingTime.trim().ifBlank { target.readingTime },
                                    pages = pages ?: target.pages,
                                    year = webYear.trim().ifBlank { target.year },
                                    language = webLanguage.trim().ifBlank { target.language },
                                    publishedDate = webPublishedDate.trim().ifBlank { target.publishedDate },
                                    whatsInside = webWhatsInside.filter { it.title.isNotBlank() }
                                )
                                val result = saveBookUseCase(updated)
                                if (result.isSuccess) {
                                    books = getBooksUseCase()
                                    showWebDialog = false
                                    webBook = null
                                    webError = null
                                } else {
                                    webError = result.exceptionOrNull()?.message
                                }
                            }
                        }

                        if (onActionWithProductionGuard != null) {
                            onActionWithProductionGuard("Simpan Tampilan Web Buku", target.title, false) {
                                performSave()
                            }
                        } else {
                            performSave()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandTeal),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Simpan Tampilan Web", fontFamily = getDmSansFontFamily(), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showWebDialog = false; webBook = null; webError = null }) {
                    Text("Batal", fontFamily = getDmSansFontFamily(), color = Color.Gray)
                }
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        WorkbenchHeader(
            title = "Books Registry",
            subtitle = "Master naskah disinkronkan dari bookinteractiontool \u2014 kelola tampilan web di sini (${books.size} terdaftar)",
            windowWidth = windowWidth,
            secondaryLabel = if (isRefreshing) "Memuat..." else "\u21BB Refresh Live Data",
            onSecondaryAction = {
                if (!isRefreshing) {
                    scope.launch {
                        isRefreshing = true
                        books = getBooksUseCase()
                        isRefreshing = false
                    }
                }
            },
        )
        Spacer(modifier = Modifier.height(24.dp))
        
        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(books, key = { it.id }) { book ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    // Metadata and the two action buttons need ~560dp side by side;
                    // on compact the actions move to their own row underneath.
                    val bookRowStacked = windowWidth.isCompact
                    val bookMeta = @Composable { modifier: Modifier ->
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
                            Surface(
                                modifier = Modifier.size(60.dp, 80.dp),
                                color = if (book.coverImage != null) BrandTeal.copy(alpha = 0.15f) else InkNavy.copy(alpha = 0.08f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(if (book.coverImage != null) "🖼️" else "📖", fontSize = 24.sp)
                                }
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    book.title, 
                                    fontFamily = getMontserratFontFamily(), 
                                    fontWeight = FontWeight.Bold, 
                                    fontSize = 16.sp,
                                    color = InkNavy
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "Penulis: ${book.author}  •  ISBN: ${book.isbn}",
                                    fontFamily = getDmSansFontFamily(),
                                    color = Color.Gray,
                                    fontSize = 13.sp,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        "ID: ${book.id}", 
                                        fontFamily = getDmSansFontFamily(), 
                                        color = BrandTeal,
                                        fontSize = 11.sp
                                    )
                                    if (book.coverImage != null) {
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            "• Cover Set", 
                                            fontFamily = getDmSansFontFamily(), 
                                            color = Color(0xFF60BD65),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                    val bookActions = @Composable { modifier: Modifier ->
                        @OptIn(ExperimentalLayoutApi::class)
                        FlowRow(
                            modifier = modifier,
                            horizontalArrangement = Arrangement.End,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            TextButton(
                                onClick = {
                                    webBook = book
                                    webCategory = book.category
                                    webDescription = book.description
                                    webPdfUrl = book.pdfUrl ?: ""
                                    webReadingTime = book.readingTime
                                    webPages = book.pages.toString()
                                    webYear = book.year
                                    webLanguage = book.language
                                    webPublishedDate = book.publishedDate
                                    webWhatsInside = if (book.whatsInside.isNotEmpty()) {
                                        book.whatsInside
                                    } else {
                                        listOf(
                                            com.sekota.features.admin.domain.model.AdminWhatsInsideItem("Poin Analisis 1", "Deskripsi fitur dan modul buku", "sparkle"),
                                            com.sekota.features.admin.domain.model.AdminWhatsInsideItem("Poin Analisis 2", "Metodologi dan arsitektur bahasan", "frame"),
                                            com.sekota.features.admin.domain.model.AdminWhatsInsideItem("Poin Analisis 3", "Integrasi operasional dan eksekusi", "gear"),
                                            com.sekota.features.admin.domain.model.AdminWhatsInsideItem("Poin Analisis 4", "Rekomendasi implementasi terukur", "leaf")
                                        )
                                    }
                                    webError = null
                                    showWebDialog = true
                                }
                            ) {
                                Text(
                                    "\u270F\uFE0F Tampilan Web",
                                    color = Color(0xFF60BD65),
                                    fontFamily = getDmSansFontFamily(),
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                            }
                        }
                    }

                    if (bookRowStacked) {
                        Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                            bookMeta(Modifier.fillMaxWidth())
                            bookActions(Modifier.fillMaxWidth())
                        }
                    } else {
                        Row(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            bookMeta(Modifier.weight(1f))
                            bookActions(Modifier)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun IntelligenceSuiteTab(
    getProductsUseCase: GetAdminProductsUseCase,
    saveProductUseCase: SaveAdminProductUseCase,
    deleteProductUseCase: DeleteAdminProductUseCase,
    windowWidth: WindowWidth = WindowWidth.Expanded,
    onActionWithProductionGuard: ((String, String, Boolean, () -> Unit) -> Unit)? = null
) {
    var products by remember { mutableStateOf<List<AdminProduct>>(emptyList()) }
    var showDialog by remember { mutableStateOf(false) }
    var editingProduct by remember { mutableStateOf<AdminProduct?>(null) }
    var prodCode by remember { mutableStateOf("") }
    var prodName by remember { mutableStateOf("") }
    var prodCategory by remember { mutableStateOf("Intelligence Suite") }
    var prodDesc by remember { mutableStateOf("") }
    var prodFeaturesText by remember { mutableStateOf("") }
    var prodIconUrlOrBase64 by remember { mutableStateOf<String?>(null) }
    var prodAccentColorHex by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        products = getProductsUseCase()
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false; editingProduct = null },
            title = {
                Text(
                    text = if (editingProduct == null) "Tambah Produk Intelligence Suite" else "Edit Modul: ${editingProduct?.name}",
                    fontFamily = getMontserratFontFamily(),
                    fontWeight = FontWeight.Bold,
                    color = InkNavy
                )
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    errorMessage?.let {
                        Text(it, color = Color(0xFFE53E3E), fontSize = 12.sp)
                    }
                    val codeField = @Composable { modifier: Modifier ->
                        OutlinedTextField(
                            value = prodCode,
                            onValueChange = { prodCode = it; errorMessage = null },
                            label = { Text("Kode (e.g. VRD, ASC)") },
                            singleLine = true,
                            modifier = modifier
                        )
                    }
                    val nameField = @Composable { modifier: Modifier ->
                        OutlinedTextField(
                            value = prodName,
                            onValueChange = { prodName = it; errorMessage = null },
                            label = { Text("Nama Produk") },
                            singleLine = true,
                            modifier = modifier
                        )
                    }
                    // Two fields on one line leave the code field unreadable on a phone.
                    if (windowWidth.isCompact) {
                        codeField(Modifier.fillMaxWidth())
                        nameField(Modifier.fillMaxWidth())
                    } else {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            codeField(Modifier.weight(0.4f))
                            nameField(Modifier.weight(0.6f))
                        }
                    }
                    OutlinedTextField(
                        value = prodCategory,
                        onValueChange = { prodCategory = it; errorMessage = null },
                        label = { Text("Kategori / Eyebrow") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = prodDesc,
                        onValueChange = { prodDesc = it; errorMessage = null },
                        label = { Text("Deskripsi Editorial") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = prodFeaturesText,
                        onValueChange = { prodFeaturesText = it; errorMessage = null },
                        label = { Text("Core Modules / Features (pisahkan dengan koma)") },
                        placeholder = { Text("e.g. Sentiment Analytics, ESG Metric Tracker") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = prodAccentColorHex,
                        onValueChange = { prodAccentColorHex = it; errorMessage = null },
                        label = { Text("Warna Aksen Hex (opsional, contoh: #00BFA5)") },
                        placeholder = { Text("#00BFA5") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Custom Icon / Logo Picker
                    Text(
                        text = "Ikon / Logo Produk",
                        fontFamily = getDmSansFontFamily(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = InkNavy
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = {
                                selectAndReadImageFile(
                                    onFileRead = { base64Data ->
                                        prodIconUrlOrBase64 = base64Data
                                    }
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = BrandTeal.copy(alpha = 0.15f))
                        ) {
                            Text(
                                text = if (prodIconUrlOrBase64.isNullOrBlank()) "📁 Unggah Ikon / Logo" else "🔄 Ganti Ikon",
                                color = BrandTeal,
                                fontFamily = getDmSansFontFamily(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }

                        if (!prodIconUrlOrBase64.isNullOrBlank()) {
                            TextButton(
                                onClick = { prodIconUrlOrBase64 = null }
                            ) {
                                Text("Hapus Ikon Kustom", color = Color(0xFFE53E3E), fontSize = 12.sp)
                            }
                        }
                    }

                    // Preview Icon
                    if (!prodIconUrlOrBase64.isNullOrBlank()) {
                        val customBitmap = remember(prodIconUrlOrBase64) {
                            decodeBase64ToBitmap(prodIconUrlOrBase64!!)
                        }
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFF8FAFB),
                            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                            modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                if (customBitmap != null) {
                                    Image(
                                        bitmap = customBitmap,
                                        contentDescription = "Preview Ikon",
                                        modifier = Modifier.size(48.dp)
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier.size(48.dp).background(BrandTeal.copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("🖼️", fontSize = 20.sp)
                                    }
                                }
                                Column {
                                    Text("Ikon Kustom Siap Ditampilkan", fontFamily = getDmSansFontFamily(), fontWeight = FontWeight.Bold, fontSize = 13.sp, color = InkNavy)
                                    Text("Akan otomatis tersinkronisasi dan tampil di Web Sekota.", fontFamily = getDmSansFontFamily(), fontSize = 11.sp, color = Color.Gray)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (prodName.isBlank()) {
                            errorMessage = "Nama produk tidak boleh kosong"
                            return@Button
                        }
                        if (prodCode.isBlank()) {
                            errorMessage = "Kode produk tidak boleh kosong"
                            return@Button
                        }
                        val performSave = {
                            scope.launch {
                                val id = editingProduct?.id ?: prodCode.lowercase().trim().ifBlank { "prod-${kotlin.random.Random.nextInt(1000, 9999)}" }
                                val features = prodFeaturesText.split(",").map { it.trim() }.filter { it.isNotBlank() }
                                val productToSave = AdminProduct(
                                    id = id,
                                    code = prodCode.trim().uppercase(),
                                    name = prodName.trim(),
                                    categoryEyebrow = prodCategory.trim().ifBlank { "Intelligence Suite" },
                                    description = prodDesc.trim(),
                                    features = if (features.isNotEmpty()) features else listOf("Core Intelligence"),
                                    iconUrlOrBase64 = prodIconUrlOrBase64,
                                    accentColorHex = prodAccentColorHex.trim().ifBlank { null }
                                )
                                val result = saveProductUseCase(productToSave)
                                if (result.isSuccess) {
                                    products = getProductsUseCase()
                                    syncService.triggerLocalUpdate()
                                    showDialog = false
                                    editingProduct = null
                                    prodCode = ""
                                    prodName = ""
                                    prodDesc = ""
                                    prodFeaturesText = ""
                                    prodIconUrlOrBase64 = null
                                    prodAccentColorHex = ""
                                    errorMessage = null
                                } else {
                                    errorMessage = result.exceptionOrNull()?.message
                                }
                            }
                        }

                        if (onActionWithProductionGuard != null) {
                            onActionWithProductionGuard(
                                if (editingProduct == null) "Tambah Produk" else "Edit Produk",
                                prodName.trim(),
                                false
                            ) {
                                performSave()
                            }
                        } else {
                            performSave()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandTeal)
                ) {
                    Text(if (editingProduct == null) "Simpan" else "Perbarui", fontFamily = getDmSansFontFamily(), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false; editingProduct = null }) {
                    Text("Batal", color = Color.Gray)
                }
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        WorkbenchHeader(
            title = "Intelligence Suite",
            subtitle = "Kelola produk kecerdasan bisnis Sekota (${products.size} modul terdaftar)",
            windowWidth = windowWidth,
            actionLabel = "Add Product",
            onAction = {
                editingProduct = null
                prodCode = ""
                prodName = ""
                prodCategory = "Intelligence Suite"
                prodDesc = ""
                prodFeaturesText = ""
                prodIconUrlOrBase64 = null
                prodAccentColorHex = ""
                errorMessage = null
                showDialog = true
            }
        )
        Spacer(modifier = Modifier.height(24.dp))
        
        // Rule 11: Adaptive grid for CMS Intelligence Suite
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = if (windowWidth.isCompact) 240.dp else 320.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(products, key = { it.id }) { prod ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = prod.name, 
                                fontFamily = getMontserratFontFamily(), 
                                fontWeight = FontWeight.Bold, 
                                fontSize = 22.sp,
                                color = InkNavy
                            )
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = BrandTeal.copy(alpha = 0.12f)
                            ) {
                                Text(
                                    text = prod.code,
                                    color = BrandTeal,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(prod.description, fontFamily = getDmSansFontFamily(), color = Color.Gray, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Core Modules:", fontFamily = getDmSansFontFamily(), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        prod.features.forEach { feat ->
                            Text("• $feat", fontFamily = getDmSansFontFamily(), color = Color.DarkGray, fontSize = 12.sp)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            TextButton(
                                onClick = {
                                    editingProduct = prod
                                    prodCode = prod.code
                                    prodName = prod.name
                                    prodCategory = prod.categoryEyebrow
                                    prodDesc = prod.description
                                    prodFeaturesText = prod.features.joinToString(", ")
                                    prodIconUrlOrBase64 = prod.iconUrlOrBase64
                                    prodAccentColorHex = prod.accentColorHex ?: ""
                                    errorMessage = null
                                    showDialog = true
                                }
                            ) {
                                Text("✏️ Edit", color = BrandTeal, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            TextButton(
                                onClick = {
                                    val performDelete = {
                                        products = products.filter { it.id != prod.id }
                                        scope.launch {
                                            deleteProductUseCase(prod.id)
                                            syncService.triggerLocalUpdate()
                                        }
                                    }

                                    if (onActionWithProductionGuard != null) {
                                        onActionWithProductionGuard(
                                            "Hapus Modul Produk",
                                            "${prod.name} (${prod.code})",
                                            true
                                        ) {
                                            performDelete()
                                        }
                                    } else {
                                        performDelete()
                                    }
                                }
                            ) {
                                Text("🗑 Delete", color = Color(0xFFE53E3E), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MerchandiseTab(
    getMerchUseCase: GetAdminMerchUseCase,
    saveMerchUseCase: SaveAdminMerchUseCase,
    deleteMerchUseCase: DeleteAdminMerchUseCase,
    windowWidth: WindowWidth = WindowWidth.Expanded,
    onActionWithProductionGuard: ((String, String, Boolean, () -> Unit) -> Unit)? = null
) {
    var merchList by remember { mutableStateOf<List<AdminMerch>>(emptyList()) }
    var showDialog by remember { mutableStateOf(false) }
    var editingMerchId by remember { mutableStateOf<String?>(null) }
    var itemTitle by remember { mutableStateOf("") }
    var itemCategory by remember { mutableStateOf("Apparel") }
    var itemPrice by remember { mutableStateOf("") }
    var itemImageUrl by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        merchList = getMerchUseCase()
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    text = if (editingMerchId == null) "Tambah Merchandise Baru" else "Edit Merchandise",
                    fontFamily = getMontserratFontFamily(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = InkNavy
                )
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    errorMessage?.let {
                        Text(it, color = Color(0xFFE53E3E), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    OutlinedTextField(
                        value = itemTitle,
                        onValueChange = { itemTitle = it; errorMessage = null },
                        label = { Text("Nama Item", fontFamily = getDmSansFontFamily()) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = itemCategory,
                        onValueChange = { itemCategory = it; errorMessage = null },
                        label = { Text("Kategori (e.g. Apparel, Accessories)", fontFamily = getDmSansFontFamily()) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = itemPrice,
                        onValueChange = { itemPrice = it; errorMessage = null },
                        label = { Text("Harga (USD / $)", fontFamily = getDmSansFontFamily()) },
                        placeholder = { Text("Contoh: 45.0") },
                        singleLine = true,
                        isError = errorMessage != null && itemPrice.toDoubleOrNull() == null,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = itemImageUrl,
                        onValueChange = { itemImageUrl = it; errorMessage = null },
                        label = { Text("Image URL / Path", fontFamily = getDmSansFontFamily()) },
                        placeholder = { Text("e.g. /images/merch-hoodie.png") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Image thumbnail preview
                    if (itemImageUrl.isNotBlank()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF1F5F9), RoundedCornerShape(8.dp))
                                .padding(8.dp)
                        ) {
                            Surface(
                                modifier = Modifier.size(40.dp, 40.dp),
                                shape = RoundedCornerShape(4.dp),
                                color = BrandTeal.copy(alpha = 0.2f)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("🖼️", fontSize = 18.sp)
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Image Ready", fontFamily = getDmSansFontFamily(), fontWeight = FontWeight.Bold, fontSize = 12.sp, color = InkNavy)
                                Text(itemImageUrl, fontFamily = getDmSansFontFamily(), fontSize = 11.sp, color = Color.Gray, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val parsedPrice = itemPrice.trim().toDoubleOrNull()
                        if (itemTitle.isBlank()) {
                            errorMessage = "Nama item tidak boleh kosong"
                            return@Button
                        }
                        if (parsedPrice == null || parsedPrice <= 0.0) {
                            errorMessage = "Harga harus berupa angka valid lebih dari 0 (contoh: 45.0)"
                            return@Button
                        }
                        val performSave = {
                            scope.launch {
                                val id = editingMerchId ?: "m-${kotlin.random.Random.nextInt(1000, 9999)}"
                                val img = itemImageUrl.trim().ifBlank { null }
                                val result = saveMerchUseCase(AdminMerch(id, itemTitle.trim(), itemCategory.trim(), "The Urban Collaborator", parsedPrice, 5.0, img))
                                if (result.isSuccess) {
                                    merchList = getMerchUseCase()
                                    showDialog = false
                                    editingMerchId = null
                                    itemTitle = ""
                                    itemCategory = "Apparel"
                                    itemPrice = ""
                                    itemImageUrl = ""
                                    errorMessage = null
                                } else {
                                    errorMessage = result.exceptionOrNull()?.message
                                }
                            }
                        }

                        if (onActionWithProductionGuard != null) {
                            onActionWithProductionGuard(
                                if (editingMerchId == null) "Tambah Merchandise" else "Edit Merchandise",
                                itemTitle.trim(),
                                false
                            ) {
                                performSave()
                            }
                        } else {
                            performSave()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandTeal),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(if (editingMerchId == null) "Simpan" else "Perbarui", fontFamily = getDmSansFontFamily(), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false; editingMerchId = null }) {
                    Text("Batal", fontFamily = getDmSansFontFamily(), color = Color.Gray)
                }
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        WorkbenchHeader(
            title = "Merchandise Store",
            subtitle = "Kelola inventaris suvenir dan pernak-pernik resmi (${merchList.size} item)",
            windowWidth = windowWidth,
            actionLabel = "Add Item",
            onAction = {
                editingMerchId = null
                itemTitle = ""
                itemCategory = "Apparel"
                itemPrice = ""
                itemImageUrl = ""
                errorMessage = null
                showDialog = true
            }
        )
        Spacer(modifier = Modifier.height(24.dp))
        
        // Rule 11: Adaptive grid for CMS Merchandise
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = if (windowWidth.isCompact) 200.dp else 260.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(merchList, key = { it.id }) { item ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Surface(
                            modifier = Modifier.fillMaxWidth().height(120.dp),
                            color = if (item.imageUrl != null) BrandTeal.copy(alpha = 0.1f) else InkNavy.copy(alpha = 0.05f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(if (item.imageUrl != null) "🖼️" else "👕", fontSize = 36.sp)
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(item.title, fontFamily = getMontserratFontFamily(), fontWeight = FontWeight.Bold, color = InkNavy, fontSize = 16.sp)
                        Text("$${item.price}", fontFamily = getDmSansFontFamily(), color = BrandTeal, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(item.category, fontFamily = getDmSansFontFamily(), color = Color.Gray, fontSize = 12.sp)
                            Text("★ ${item.rating}", fontFamily = getDmSansFontFamily(), color = Color(0xFF60BD65), fontSize = 12.sp)
                        }
                        if (item.imageUrl != null) {
                            Text(
                                "Image: ${item.imageUrl}",
                                fontFamily = getDmSansFontFamily(),
                                color = Color.Gray,
                                fontSize = 11.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            TextButton(
                                onClick = {
                                    editingMerchId = item.id
                                    itemTitle = item.title
                                    itemCategory = item.category
                                    itemPrice = item.price.toString()
                                    itemImageUrl = item.imageUrl ?: ""
                                    errorMessage = null
                                    showDialog = true
                                }
                            ) { 
                                Text("✏️ Edit", color = BrandTeal, fontSize = 13.sp, fontWeight = FontWeight.Bold) 
                            }
                            TextButton(
                                onClick = {
                                    val performDelete = {
                                        merchList = merchList.filter { it.id != item.id }
                                        scope.launch {
                                            deleteMerchUseCase(item.id)
                                        }
                                    }

                                    if (onActionWithProductionGuard != null) {
                                        onActionWithProductionGuard(
                                            "Hapus Item Merchandise",
                                            item.title,
                                            true
                                        ) {
                                            performDelete()
                                        }
                                    } else {
                                        performDelete()
                                    }
                                }
                            ) { 
                                Text("🗑 Delete", color = Color(0xFFE53E3E), fontSize = 13.sp, fontWeight = FontWeight.Bold) 
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LiveMetricsTab(
    getLiveMetricsUseCase: GetAdminLiveMetricsUseCase,
    saveLiveMetricsUseCase: SaveAdminLiveMetricsUseCase,
    windowWidth: WindowWidth = WindowWidth.Expanded,
    onActionWithProductionGuard: ((String, String, Boolean, () -> Unit) -> Unit)? = null
) {
    var accuracy by remember { mutableStateOf("99.8%") }
    var totalClients by remember { mutableStateOf("100+") }
    var establishedYear by remember { mutableStateOf("2021") }
    var statusFeedback by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val metrics = getLiveMetricsUseCase()
        accuracy = metrics.dataAccuracy
        totalClients = metrics.totalClients
        establishedYear = metrics.establishedYear
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        WorkbenchHeader(
            title = "Live Metrics Editor",
            subtitle = "Konfigurasi angka metrik yang tampil langsung pada Hero & Trust section Landing Page",
            windowWidth = windowWidth
        )
        Spacer(modifier = Modifier.height(12.dp))

        statusFeedback?.let {
            Surface(
                color = Color(0xFFE8F5E9),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Text(
                    text = it,
                    color = Color(0xFF2E7D32),
                    fontFamily = getDmSansFontFamily(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        
        // Rule 11: Adaptive wrapping for Live Metrics Workbench
        @OptIn(ExperimentalLayoutApi::class)
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val saveMetric: (String, String) -> Unit = { metricName, feedback ->
                val performSave = {
                    scope.launch {
                        val result = saveLiveMetricsUseCase(AdminLiveMetrics(accuracy.trim(), totalClients.trim(), establishedYear.trim()))
                        if (result.isSuccess) {
                            statusFeedback = feedback
                        }
                    }
                }

                if (onActionWithProductionGuard != null) {
                    onActionWithProductionGuard("Update Live Metrics ($metricName)", "Landing Page Metrics", false) {
                        performSave()
                    }
                } else {
                    performSave()
                }
            }

            MetricCardEditor(
                modifier = if (windowWidth.isCompact) Modifier.fillMaxWidth() else Modifier,
                label = "Data Accuracy",
                value = accuracy,
                onValueChange = { accuracy = it; statusFeedback = null },
                onSave = { saveMetric("Data Accuracy", "✅ Data Accuracy diperbarui ke '$accuracy'") }
            )
            MetricCardEditor(
                modifier = if (windowWidth.isCompact) Modifier.fillMaxWidth() else Modifier,
                label = "Total Clients",
                value = totalClients,
                onValueChange = { totalClients = it; statusFeedback = null },
                onSave = { saveMetric("Total Clients", "✅ Total Clients diperbarui ke '$totalClients'") }
            )
            MetricCardEditor(
                modifier = if (windowWidth.isCompact) Modifier.fillMaxWidth() else Modifier,
                label = "Established",
                value = establishedYear,
                onValueChange = { establishedYear = it; statusFeedback = null },
                onSave = { saveMetric("Established Year", "✅ Tahun Didirikan diperbarui ke '$establishedYear'") }
            )
        }
    }
}

@Composable
fun MetricCardEditor(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.widthIn(min = 180.dp, max = 260.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(label, fontFamily = getDmSansFontFamily(), color = Color.Gray, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontFamily = getMontserratFontFamily(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = BrandTeal,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onSave,
                colors = ButtonDefaults.buttonColors(containerColor = InkNavy),
                shape = RoundedCornerShape(100.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Update Metrik", fontFamily = getDmSansFontFamily(), fontSize = 13.sp)
            }
        }
    }
}

@Composable
fun ClientInquiriesTab(
    getInquiriesUseCase: GetAdminInquiriesUseCase,
    deleteInquiryUseCase: DeleteAdminInquiryUseCase,
    getStrategicConversationUseCase: GetAdminStrategicConversationUseCase = remember { GetAdminStrategicConversationUseCase(AdminRepositoryImpl()) },
    saveStrategicConversationUseCase: SaveAdminStrategicConversationUseCase = remember { SaveAdminStrategicConversationUseCase(AdminRepositoryImpl()) },
    windowWidth: WindowWidth,
    onActionWithProductionGuard: ((String, String, Boolean, () -> Unit) -> Unit)? = null
) {
    val scope = rememberCoroutineScope()
    var inquiries by remember { mutableStateOf<List<ClientInquiry>>(emptyList()) }
    var config by remember { mutableStateOf(StrategicConversationConfig()) }
    var statusFeedback by remember { mutableStateOf<String?>(null) }
    var isSaving by remember { mutableStateOf(false) }
    var activeSubTab by remember { mutableStateOf(0) } // 0: "Pengaturan Konten Formulir", 1: "Daftar Pesan Masuk (Leads)"

    LaunchedEffect(Unit) {
        inquiries = getInquiriesUseCase()
        config = getStrategicConversationUseCase()
        com.sekota.syncService.syncEventFlow.collect {
            inquiries = getInquiriesUseCase()
            config = getStrategicConversationUseCase()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        WorkbenchHeader(
            title = "Contact & Inquiries",
            subtitle = "Kelola konten formulir percakapan strategis publik dan tinjau pesan konsultasi (leads) yang masuk.",
            windowWidth = windowWidth
        )

        // Sub-Tab Switcher (Segmented Button Pills)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(top = 16.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                onClick = { activeSubTab = 0; statusFeedback = null },
                shape = RoundedCornerShape(100.dp),
                color = if (activeSubTab == 0) BrandTeal else Color.White,
                border = BorderStroke(1.dp, if (activeSubTab == 0) BrandTeal else Color(0xFFE2E8F0)),
                shadowElevation = if (activeSubTab == 0) 2.dp else 0.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    com.sekota.components.SekotaCmsNavVectorIcon(
                        index = 1,
                        size = 16.dp,
                        tint = if (activeSubTab == 0) Color.White else InkNavy
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Pengaturan Konten Formulir",
                        fontFamily = getDmSansFontFamily(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = if (activeSubTab == 0) Color.White else InkNavy
                    )
                }
            }

            Surface(
                onClick = { activeSubTab = 1; statusFeedback = null },
                shape = RoundedCornerShape(100.dp),
                color = if (activeSubTab == 1) BrandTeal else Color.White,
                border = BorderStroke(1.dp, if (activeSubTab == 1) BrandTeal else Color(0xFFE2E8F0)),
                shadowElevation = if (activeSubTab == 1) 2.dp else 0.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    com.sekota.components.SekotaCmsNavVectorIcon(
                        index = 6,
                        size = 16.dp,
                        tint = if (activeSubTab == 1) Color.White else InkNavy
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Daftar Pesan Masuk (${inquiries.size})",
                        fontFamily = getDmSansFontFamily(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = if (activeSubTab == 1) Color.White else InkNavy
                    )
                }
            }
        }

        statusFeedback?.let {
            Spacer(modifier = Modifier.height(12.dp))
            Surface(
                color = if (it.startsWith("✅")) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = it,
                    fontFamily = getDmSansFontFamily(),
                    color = if (it.startsWith("✅")) Color(0xFF2E7D32) else Color(0xFFC62828),
                    modifier = Modifier.padding(14.dp),
                    fontSize = 14.sp
                )
            }
        }

        if (activeSubTab == 0) {
            // "Pengaturan Konten Formulir" View
            val formScrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(formScrollState),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Card 1: Informasi Kontak & Headline
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Informasi Kontak & Headline",
                            fontFamily = getMontserratFontFamily(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = InkNavy
                        )

                        OutlinedTextField(
                            value = config.title,
                            onValueChange = {
                                config = config.copy(title = it)
                                statusFeedback = null
                            },
                            label = { Text("Judul Utama / Headline", fontFamily = getDmSansFontFamily()) },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2,
                            maxLines = 3
                        )

                        OutlinedTextField(
                            value = config.subtitle,
                            onValueChange = {
                                config = config.copy(subtitle = it)
                                statusFeedback = null
                            },
                            label = { Text("Subjudul / SLA Respons", fontFamily = getDmSansFontFamily()) },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2,
                            maxLines = 3
                        )

                        OutlinedTextField(
                            value = config.address,
                            onValueChange = {
                                config = config.copy(address = it)
                                statusFeedback = null
                            },
                            label = { Text("Alamat Kantor", fontFamily = getDmSansFontFamily()) },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2,
                            maxLines = 3
                        )

                        if (windowWidth.isCompact) {
                            OutlinedTextField(
                                value = config.email,
                                onValueChange = {
                                    config = config.copy(email = it)
                                    statusFeedback = null
                                },
                                label = { Text("Email Kontak Resmi", fontFamily = getDmSansFontFamily()) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = config.website,
                                onValueChange = {
                                    config = config.copy(website = it)
                                    statusFeedback = null
                                },
                                label = { Text("Website", fontFamily = getDmSansFontFamily()) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                OutlinedTextField(
                                    value = config.email,
                                    onValueChange = {
                                        config = config.copy(email = it)
                                        statusFeedback = null
                                    },
                                    label = { Text("Email Kontak Resmi", fontFamily = getDmSansFontFamily()) },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = config.website,
                                    onValueChange = {
                                        config = config.copy(website = it)
                                        statusFeedback = null
                                    },
                                    label = { Text("Website", fontFamily = getDmSansFontFamily()) },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }

                // Card 2: Label & Placeholder Formulir Klien
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Konfigurasi Label & Placeholder Formulir",
                            fontFamily = getMontserratFontFamily(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = InkNavy
                        )

                        // Nama
                        if (windowWidth.isCompact) {
                            OutlinedTextField(
                                value = config.nameLabel,
                                onValueChange = {
                                    config = config.copy(nameLabel = it)
                                    statusFeedback = null
                                },
                                label = { Text("Label Kolom Nama", fontFamily = getDmSansFontFamily()) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = config.namePlaceholder,
                                onValueChange = {
                                    config = config.copy(namePlaceholder = it)
                                    statusFeedback = null
                                },
                                label = { Text("Placeholder Nama", fontFamily = getDmSansFontFamily()) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                OutlinedTextField(
                                    value = config.nameLabel,
                                    onValueChange = {
                                        config = config.copy(nameLabel = it)
                                        statusFeedback = null
                                    },
                                    label = { Text("Label Kolom Nama", fontFamily = getDmSansFontFamily()) },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = config.namePlaceholder,
                                    onValueChange = {
                                        config = config.copy(namePlaceholder = it)
                                        statusFeedback = null
                                    },
                                    label = { Text("Placeholder Nama", fontFamily = getDmSansFontFamily()) },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        // Email
                        if (windowWidth.isCompact) {
                            OutlinedTextField(
                                value = config.emailLabel,
                                onValueChange = {
                                    config = config.copy(emailLabel = it)
                                    statusFeedback = null
                                },
                                label = { Text("Label Kolom Email", fontFamily = getDmSansFontFamily()) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = config.emailPlaceholder,
                                onValueChange = {
                                    config = config.copy(emailPlaceholder = it)
                                    statusFeedback = null
                                },
                                label = { Text("Placeholder Email", fontFamily = getDmSansFontFamily()) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                OutlinedTextField(
                                    value = config.emailLabel,
                                    onValueChange = {
                                        config = config.copy(emailLabel = it)
                                        statusFeedback = null
                                    },
                                    label = { Text("Label Kolom Email", fontFamily = getDmSansFontFamily()) },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = config.emailPlaceholder,
                                    onValueChange = {
                                        config = config.copy(emailPlaceholder = it)
                                        statusFeedback = null
                                    },
                                    label = { Text("Placeholder Email", fontFamily = getDmSansFontFamily()) },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        // Pesan
                        if (windowWidth.isCompact) {
                            OutlinedTextField(
                                value = config.messageLabel,
                                onValueChange = {
                                    config = config.copy(messageLabel = it)
                                    statusFeedback = null
                                },
                                label = { Text("Label Kolom Pesan", fontFamily = getDmSansFontFamily()) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = config.messagePlaceholder,
                                onValueChange = {
                                    config = config.copy(messagePlaceholder = it)
                                    statusFeedback = null
                                },
                                label = { Text("Placeholder Pesan", fontFamily = getDmSansFontFamily()) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                OutlinedTextField(
                                    value = config.messageLabel,
                                    onValueChange = {
                                        config = config.copy(messageLabel = it)
                                        statusFeedback = null
                                    },
                                    label = { Text("Label Kolom Pesan", fontFamily = getDmSansFontFamily()) },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = config.messagePlaceholder,
                                    onValueChange = {
                                        config = config.copy(messagePlaceholder = it)
                                        statusFeedback = null
                                    },
                                    label = { Text("Placeholder Pesan", fontFamily = getDmSansFontFamily()) },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        // Tombol CTA
                        OutlinedTextField(
                            value = config.submitButtonLabel,
                            onValueChange = {
                                config = config.copy(submitButtonLabel = it)
                                statusFeedback = null
                            },
                            label = { Text("Label Tombol CTA Kirim", fontFamily = getDmSansFontFamily()) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            val performSave = {
                                scope.launch {
                                    isSaving = true
                                    val result = saveStrategicConversationUseCase(config)
                                    isSaving = false
                                    if (result.isSuccess) {
                                        statusFeedback = "Konten formulir percakapan strategis berhasil disimpan dan disinkronkan!"
                                    } else {
                                        statusFeedback = "Gagal menyimpan: ${result.exceptionOrNull()?.message}"
                                    }
                                }
                            }

                            if (onActionWithProductionGuard != null) {
                                onActionWithProductionGuard("Simpan Pengaturan Formulir Klien", "Contact Form Configuration", false) {
                                    performSave()
                                }
                            } else {
                                performSave()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BrandTeal),
                        shape = RoundedCornerShape(100.dp),
                        enabled = !isSaving
                    ) {
                        Text(
                            text = if (isSaving) "Menyimpan..." else "Simpan Perubahan",
                            fontFamily = getDmSansFontFamily(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            config = StrategicConversationConfig()
                            statusFeedback = "Formulir dikembalikan ke nilai default. Klik 'Simpan Perubahan' untuk menerapkan."
                        },
                        shape = RoundedCornerShape(100.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = InkNavy)
                    ) {
                        Text(
                            text = "Reset ke Default",
                            fontFamily = getDmSansFontFamily(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        } else {
            // "Daftar Pesan Masuk (Leads)" View
            if (inquiries.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color.White, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    com.sekota.components.SekotaCmsNavVectorIcon(index = 6, size = 48.dp, tint = Color.LightGray)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Belum ada pesan konsultasi masuk.",
                        fontFamily = getDmSansFontFamily(),
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(inquiries, key = { it.id }) { inquiry ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        color = BrandTeal.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = inquiry.status.uppercase(),
                                            fontFamily = getDmSansFontFamily(),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            color = BrandTeal,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = inquiry.timestamp,
                                        fontFamily = getDmSansFontFamily(),
                                        color = Color.Gray,
                                        fontSize = 13.sp
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        val performDelete = {
                                            scope.launch {
                                                val res = deleteInquiryUseCase(inquiry.id)
                                                if (res.isSuccess) {
                                                    inquiries = getInquiriesUseCase()
                                                    statusFeedback = "Pesan dari ${inquiry.name} berhasil dihapus"
                                                } else {
                                                    statusFeedback = "Gagal menghapus pesan"
                                                }
                                            }
                                        }

                                        if (onActionWithProductionGuard != null) {
                                            onActionWithProductionGuard(
                                                "Hapus Pesan Konsultasi",
                                                "Inquiry dari ${inquiry.name} (${inquiry.email})",
                                                true
                                            ) {
                                                performDelete()
                                            }
                                        } else {
                                            performDelete()
                                        }
                                    }
                                ) {
                                    com.sekota.components.SekotaTrashVectorIcon(size = 18.dp, tint = Color(0xFFE53E3E))
                                }
                            }

                            Text(
                                text = inquiry.name,
                                fontFamily = getMontserratFontFamily(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = InkNavy
                            )

                            Text(
                                text = inquiry.email,
                                fontFamily = getDmSansFontFamily(),
                                color = BrandTeal,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                            )

                            Surface(
                                color = Color(0xFFF8FAFC),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = inquiry.message,
                                    fontFamily = getDmSansFontFamily(),
                                    color = Color(0xFF334155),
                                    fontSize = 14.sp,
                                    lineHeight = 22.sp,
                                    modifier = Modifier.padding(16.dp)
                                 )
                             }
                         }
                     }
                 }
             }
         }
        }
    }
}

@Composable
fun ValueLoopTab(
    getValueLoopUseCase: GetAdminValueLoopUseCase,
    saveValueLoopUseCase: SaveAdminValueLoopUseCase,
    windowWidth: WindowWidth,
    onActionWithProductionGuard: ((String, String, Boolean, () -> Unit) -> Unit)? = null
) {
    val scope = rememberCoroutineScope()
    var config by remember { mutableStateOf(com.sekota.features.admin.domain.model.ValueLoopConfig()) }
    var statusFeedback by remember { mutableStateOf<String?>(null) }
    var isSaving by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        config = getValueLoopUseCase()
        com.sekota.syncService.syncEventFlow.collect {
            config = getValueLoopUseCase()
        }
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        WorkbenchHeader(
            title = "Strategic Value Loop",
            subtitle = "Kelola judul, deskripsi, dan tahapan siklus tertutup 01-05 Solusi Kami di portal publik Sekota.",
            windowWidth = windowWidth
        )

        statusFeedback?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                color = if (it.startsWith("✅")) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = it,
                    fontFamily = getDmSansFontFamily(),
                    color = if (it.startsWith("✅")) Color(0xFF2E7D32) else Color(0xFFC62828),
                    modifier = Modifier.padding(16.dp),
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Header Editor Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Pengaturan Header Section",
                    fontFamily = getMontserratFontFamily(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = InkNavy
                )

                OutlinedTextField(
                    value = config.sectionEyebrow,
                    onValueChange = {
                        config = config.copy(sectionEyebrow = it)
                        statusFeedback = null
                    },
                    label = { Text("Eyebrow (Label Kecil Atas)", fontFamily = getDmSansFontFamily()) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = config.title,
                    onValueChange = {
                        config = config.copy(title = it)
                        statusFeedback = null
                    },
                    label = { Text("Judul Utama Section", fontFamily = getDmSansFontFamily()) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = config.description,
                    onValueChange = {
                        config = config.copy(description = it)
                        statusFeedback = null
                    },
                    label = { Text("Deskripsi / Subjudul", fontFamily = getDmSansFontFamily()) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 4
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Steps Editor Section
        Text(
            text = "Langkah-Langkah Siklus (01 - 05)",
            fontFamily = getMontserratFontFamily(),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = InkNavy
        )
        Spacer(modifier = Modifier.height(12.dp))

        @OptIn(ExperimentalLayoutApi::class)
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            config.steps.forEachIndexed { index, step ->
                Card(
                    modifier = if (windowWidth.isCompact) Modifier.fillMaxWidth() else Modifier.widthIn(min = 280.dp, max = 340.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Surface(
                                color = BrandTeal,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = step.stepNumber,
                                    fontFamily = getDmSansFontFamily(),
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                            Text(
                                text = "Langkah ${index + 1}",
                                fontFamily = getMontserratFontFamily(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = InkNavy
                            )
                        }

                        OutlinedTextField(
                            value = step.title,
                            onValueChange = { newTitle ->
                                val updatedSteps = config.steps.toMutableList()
                                updatedSteps[index] = step.copy(title = newTitle)
                                config = config.copy(steps = updatedSteps)
                                statusFeedback = null
                            },
                            label = { Text("Judul Tahap (e.g. INTENT)", fontFamily = getDmSansFontFamily()) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = step.subtitle,
                            onValueChange = { newSubtitle ->
                                val updatedSteps = config.steps.toMutableList()
                                updatedSteps[index] = step.copy(subtitle = newSubtitle)
                                config = config.copy(steps = updatedSteps)
                                statusFeedback = null
                            },
                            label = { Text("Subjudul Tahap (e.g. Definisi Strategis)", fontFamily = getDmSansFontFamily()) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Action Buttons Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    val performSave = {
                        scope.launch {
                            isSaving = true
                            val result = saveValueLoopUseCase(config)
                            isSaving = false
                            if (result.isSuccess) {
                                statusFeedback = "✅ Strategic Value Loop berhasil diperbarui dan disinkronkan!"
                            } else {
                                statusFeedback = "❌ Gagal memperbarui: ${result.exceptionOrNull()?.message}"
                            }
                        }
                    }

                    if (onActionWithProductionGuard != null) {
                        onActionWithProductionGuard("Simpan Strategic Value Loop", "Landing Page Solusi Kami", false) {
                            performSave()
                        }
                    } else {
                        performSave()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = BrandTeal),
                shape = RoundedCornerShape(100.dp),
                enabled = !isSaving,
                modifier = Modifier.height(48.dp)
            ) {
                Text(
                    text = if (isSaving) "Menyimpan..." else "💾 Simpan Perubahan",
                    fontFamily = getDmSansFontFamily(),
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            OutlinedButton(
                onClick = {
                    config = com.sekota.features.admin.domain.model.ValueLoopConfig()
                    statusFeedback = "ℹ️ Reset ke konfigurasi bawaan (Tekan Simpan untuk menerapkan)"
                },
                shape = RoundedCornerShape(100.dp),
                modifier = Modifier.height(48.dp)
            ) {
                Text(
                    text = "↺ Reset Bawaan",
                    fontFamily = getDmSansFontFamily(),
                    color = InkNavy
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun WhySekotaTab(
    getWhySekotaUseCase: GetAdminWhySekotaUseCase,
    saveWhySekotaUseCase: SaveAdminWhySekotaUseCase,
    windowWidth: WindowWidth,
    onActionWithProductionGuard: ((String, String, Boolean, () -> Unit) -> Unit)? = null
) {
    val scope = rememberCoroutineScope()
    var config by remember { mutableStateOf(WhySekotaConfig()) }
    var statusFeedback by remember { mutableStateOf<String?>(null) }
    var isSaving by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        config = getWhySekotaUseCase()
        com.sekota.syncService.syncEventFlow.collect {
            config = getWhySekotaUseCase()
        }
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        WorkbenchHeader(
            title = "Why Sekota (Nilai Keunggulan)",
            subtitle = "Kelola pre-title, headline, deskripsi, tombol CTA, serta daftar kartu pilar 'Mengapa Sekota' di portal publik.",
            windowWidth = windowWidth
        )

        statusFeedback?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                color = if (it.startsWith("✅")) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = it,
                    fontFamily = getDmSansFontFamily(),
                    color = if (it.startsWith("✅")) Color(0xFF2E7D32) else Color(0xFFC62828),
                    modifier = Modifier.padding(16.dp),
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Main Content / Header Editor Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Konten Utama Section",
                    fontFamily = getMontserratFontFamily(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = InkNavy
                )

                OutlinedTextField(
                    value = config.preTitle,
                    onValueChange = {
                        config = config.copy(preTitle = it)
                        statusFeedback = null
                    },
                    label = { Text("Pre-title / Eyebrow (e.g. MENGAPA SEKOTA)", fontFamily = getDmSansFontFamily()) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = config.title,
                    onValueChange = {
                        config = config.copy(title = it)
                        statusFeedback = null
                    },
                    label = { Text("Judul Utama / Headline", fontFamily = getDmSansFontFamily()) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 4
                )

                OutlinedTextField(
                    value = config.description,
                    onValueChange = {
                        config = config.copy(description = it)
                        statusFeedback = null
                    },
                    label = { Text("Deskripsi / Subjudul", fontFamily = getDmSansFontFamily()) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 4
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = config.ctaLabel,
                        onValueChange = {
                            config = config.copy(ctaLabel = it)
                            statusFeedback = null
                        },
                        label = { Text("Label Tombol CTA", fontFamily = getDmSansFontFamily()) },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = config.ctaTarget,
                        onValueChange = {
                            config = config.copy(ctaTarget = it)
                            statusFeedback = null
                        },
                        label = { Text("Target Link / Aksi CTA (e.g. #kontak)", fontFamily = getDmSansFontFamily()) },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Pillar / Value Cards Management
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Kartu Pilar Keunggulan (${config.cards.size} kartu)",
                fontFamily = getMontserratFontFamily(),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = InkNavy
            )

            Button(
                onClick = {
                    val nextNum = config.cards.size + 1
                    val newCard = WhySekotaCard(
                        id = "card_${kotlin.random.Random.nextInt(1000, 9999)}",
                        title = "Keunggulan Baru #$nextNum",
                        description = "Deskripsi nilai keunggulan atau dampak strategis bagi mitra.",
                        iconName = "sparkle"
                    )
                    config = config.copy(cards = config.cards + newCard)
                    statusFeedback = null
                },
                colors = ButtonDefaults.buttonColors(containerColor = BrandTeal),
                shape = RoundedCornerShape(100.dp)
            ) {
                Text(
                    text = "➕ Tambah Kartu Pilar",
                    fontFamily = getDmSansFontFamily(),
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        val availableIcons = listOf("trending", "chip", "shield", "sparkle")

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            config.cards.forEachIndexed { index, card ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Surface(
                                    color = BrandTeal,
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "#${index + 1}",
                                        fontFamily = getDmSansFontFamily(),
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                                Text(
                                    text = "Kartu Pilar ${index + 1}",
                                    fontFamily = getMontserratFontFamily(),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = InkNavy
                                )
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                // Move Up Button
                                IconButton(
                                    onClick = {
                                        if (index > 0) {
                                            val mutable = config.cards.toMutableList()
                                            val item = mutable.removeAt(index)
                                            mutable.add(index - 1, item)
                                            config = config.copy(cards = mutable)
                                            statusFeedback = null
                                        }
                                    },
                                    enabled = index > 0
                                ) {
                                    com.sekota.components.SekotaArrowUpVectorIcon(
                                        size = 18.dp,
                                        tint = if (index > 0) InkNavy else Color.LightGray
                                    )
                                }

                                // Move Down Button
                                IconButton(
                                    onClick = {
                                        if (index < config.cards.lastIndex) {
                                            val mutable = config.cards.toMutableList()
                                            val item = mutable.removeAt(index)
                                            mutable.add(index + 1, item)
                                            config = config.copy(cards = mutable)
                                            statusFeedback = null
                                        }
                                    },
                                    enabled = index < config.cards.lastIndex
                                ) {
                                    com.sekota.components.SekotaArrowDownVectorIcon(
                                        size = 18.dp,
                                        tint = if (index < config.cards.lastIndex) InkNavy else Color.LightGray
                                    )
                                }

                                // Delete Button
                                IconButton(
                                    onClick = {
                                        if (config.cards.size > 1) {
                                            val mutable = config.cards.toMutableList()
                                            mutable.removeAt(index)
                                            config = config.copy(cards = mutable)
                                            statusFeedback = null
                                        } else {
                                            statusFeedback = "❌ Minimal harus ada 1 kartu pilar 'Why Sekota'"
                                        }
                                    },
                                    enabled = config.cards.size > 1
                                ) {
                                    com.sekota.components.SekotaTrashVectorIcon(
                                        size = 18.dp,
                                        tint = if (config.cards.size > 1) Color(0xFFE53E3E) else Color.LightGray
                                    )
                                }
                            }
                        }

                        // Icon Selector Chips with real Compose drawables
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "Pilih Ikon Pilar:",
                                fontFamily = getDmSansFontFamily(),
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                listOf(
                                    Triple("trending", "Tren", Res.drawable.icon_3),
                                    Triple("chip", "Chip", Res.drawable.icon_4),
                                    Triple("shield", "Shield", Res.drawable.shield),
                                    Triple("sparkle", "Sparkle", Res.drawable.icon_6)
                                ).forEach { (iconKey, iconLabel, iconRes) ->
                                    val isSelected = card.iconName.equals(iconKey, ignoreCase = true)
                                    Surface(
                                        color = if (isSelected) BrandTeal.copy(alpha = 0.12f) else Color(0xFFF1F5F9),
                                        shape = RoundedCornerShape(8.dp),
                                        border = BorderStroke(1.dp, if (isSelected) BrandTeal else Color(0xFFCBD5E1)),
                                        modifier = Modifier.clickable {
                                            val mutable = config.cards.toMutableList()
                                            mutable[index] = card.copy(iconName = iconKey)
                                            config = config.copy(cards = mutable)
                                            statusFeedback = null
                                        }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Image(
                                                painter = painterResource(iconRes),
                                                contentDescription = iconLabel,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Text(
                                                text = iconLabel,
                                                fontFamily = getDmSansFontFamily(),
                                                fontSize = 12.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isSelected) BrandTeal else InkNavy
                                            )
                                        }
                                    }
                                }
                            }
                        }


                        OutlinedTextField(
                            value = card.title,
                            onValueChange = { newTitle ->
                                val mutable = config.cards.toMutableList()
                                mutable[index] = card.copy(title = newTitle)
                                config = config.copy(cards = mutable)
                                statusFeedback = null
                            },
                            label = { Text("Judul Kartu Pilar", fontFamily = getDmSansFontFamily()) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = card.description,
                            onValueChange = { newDesc ->
                                val mutable = config.cards.toMutableList()
                                mutable[index] = card.copy(description = newDesc)
                                config = config.copy(cards = mutable)
                                statusFeedback = null
                            },
                            label = { Text("Deskripsi Kartu", fontFamily = getDmSansFontFamily()) },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2,
                            maxLines = 4
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Action Buttons Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    if (config.title.isBlank()) {
                        statusFeedback = "❌ Judul Utama / Headline tidak boleh kosong"
                        return@Button
                    }
                    if (config.cards.any { it.title.isBlank() }) {
                        statusFeedback = "❌ Judul pada setiap kartu pilar tidak boleh kosong"
                        return@Button
                    }
                    val performSave = {
                        scope.launch {
                            isSaving = true
                            val result = saveWhySekotaUseCase(config)
                            isSaving = false
                            if (result.isSuccess) {
                                statusFeedback = "✅ 'Why Sekota' berhasil diperbarui dan disinkronkan ke Web!"
                            } else {
                                statusFeedback = "❌ Gagal memperbarui: ${result.exceptionOrNull()?.message}"
                            }
                        }
                    }

                    if (onActionWithProductionGuard != null) {
                        onActionWithProductionGuard("Simpan Why Sekota", "Landing Page Pilar Keunggulan", false) {
                            performSave()
                        }
                    } else {
                        performSave()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = BrandTeal),
                shape = RoundedCornerShape(100.dp),
                enabled = !isSaving,
                modifier = Modifier.height(48.dp)
            ) {
                Text(
                    text = if (isSaving) "Menyimpan..." else "💾 Simpan Perubahan",
                    fontFamily = getDmSansFontFamily(),
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            OutlinedButton(
                onClick = {
                    config = WhySekotaConfig()
                    statusFeedback = "ℹ️ Reset ke konfigurasi bawaan (Tekan Simpan untuk menerapkan)"
                },
                shape = RoundedCornerShape(100.dp),
                modifier = Modifier.height(48.dp)
            ) {
                Text(
                    text = "↺ Reset Bawaan",
                    fontFamily = getDmSansFontFamily(),
                    color = InkNavy
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun FooterManagementTab(
    getFooterUseCase: GetAdminFooterUseCase,
    saveFooterUseCase: SaveAdminFooterUseCase,
    windowWidth: WindowWidth,
    onActionWithProductionGuard: ((String, String, Boolean, () -> Unit) -> Unit)? = null
) {
    val scope = rememberCoroutineScope()
    var config by remember { mutableStateOf(com.sekota.features.admin.domain.model.FooterConfig()) }
    var statusFeedback by remember { mutableStateOf<String?>(null) }
    var isSaving by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        config = getFooterUseCase()
        com.sekota.syncService.syncEventFlow.collect {
            config = getFooterUseCase()
        }
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        WorkbenchHeader(
            title = "Footer Content Management",
            subtitle = "Kelola tagline, afiliasi, hak cipta, tautan media sosial, serta kolom tautan Produk, Perusahaan, dan Legal secara dinamis.",
            windowWidth = windowWidth
        )

        statusFeedback?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                color = if (it.startsWith("✅")) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = it,
                    fontFamily = getDmSansFontFamily(),
                    color = if (it.startsWith("✅")) Color(0xFF2E7D32) else Color(0xFFC62828),
                    modifier = Modifier.padding(16.dp),
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Card 1: Branding, Tagline, & Afiliasi
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Branding, Tagline & Afiliasi",
                    fontFamily = getMontserratFontFamily(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = InkNavy
                )

                OutlinedTextField(
                    value = config.tagline,
                    onValueChange = { config = config.copy(tagline = it); statusFeedback = null },
                    label = { Text("Tagline Sekota", fontFamily = getDmSansFontFamily()) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 4
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = config.affiliationText,
                        onValueChange = { config = config.copy(affiliationText = it); statusFeedback = null },
                        label = { Text("Teks Afiliasi", fontFamily = getDmSansFontFamily()) },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = config.affiliationSubtext,
                        onValueChange = { config = config.copy(affiliationSubtext = it); statusFeedback = null },
                        label = { Text("Subteks Afiliasi", fontFamily = getDmSansFontFamily()) },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                OutlinedTextField(
                    value = config.copyrightText,
                    onValueChange = { config = config.copy(copyrightText = it); statusFeedback = null },
                    label = { Text("Teks Hak Cipta (Copyright)", fontFamily = getDmSansFontFamily()) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Card 2: Kolom Produk
        FooterColumnEditorCard(
            columnTitle = "Kolom Produk",
            columnConfig = config.productColumn,
            onColumnChange = { updated ->
                config = config.copy(productColumn = updated)
                statusFeedback = null
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Card 3: Kolom Perusahaan
        FooterColumnEditorCard(
            columnTitle = "Kolom Perusahaan",
            columnConfig = config.companyColumn,
            onColumnChange = { updated ->
                config = config.copy(companyColumn = updated)
                statusFeedback = null
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Card 4: Kolom Legal
        FooterColumnEditorCard(
            columnTitle = "Kolom Legal",
            columnConfig = config.legalColumn,
            onColumnChange = { updated ->
                config = config.copy(legalColumn = updated)
                statusFeedback = null
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Card 5: Media Sosial
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Tautan Media Sosial",
                    fontFamily = getMontserratFontFamily(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = InkNavy
                )

                config.socialLinks.forEachIndexed { sIdx, social ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = social.platform,
                            onValueChange = { newPlat ->
                                val list = config.socialLinks.toMutableList()
                                list[sIdx] = social.copy(platform = newPlat)
                                config = config.copy(socialLinks = list)
                                statusFeedback = null
                            },
                            label = { Text("Platform", fontFamily = getDmSansFontFamily()) },
                            modifier = Modifier.width(140.dp),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = social.url,
                            onValueChange = { newUrl ->
                                val list = config.socialLinks.toMutableList()
                                list[sIdx] = social.copy(url = newUrl)
                                config = config.copy(socialLinks = list)
                                statusFeedback = null
                            },
                            label = { Text("URL Tautan Eksternal", fontFamily = getDmSansFontFamily()) },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = social.isVisible,
                                onCheckedChange = { checked ->
                                    val list = config.socialLinks.toMutableList()
                                    list[sIdx] = social.copy(isVisible = checked)
                                    config = config.copy(socialLinks = list)
                                    statusFeedback = null
                                }
                            )
                            Text("Tampil", fontFamily = getDmSansFontFamily(), fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Action Buttons Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    if (config.tagline.isBlank()) {
                        statusFeedback = "❌ Tagline tidak boleh kosong"
                        return@Button
                    }
                    if (config.copyrightText.isBlank()) {
                        statusFeedback = "❌ Teks hak cipta tidak boleh kosong"
                        return@Button
                    }
                    val performSave = {
                        scope.launch {
                            isSaving = true
                            val result = saveFooterUseCase(config)
                            isSaving = false
                            if (result.isSuccess) {
                                statusFeedback = "✅ Konten Footer berhasil diperbarui dan disinkronkan ke Web!"
                            } else {
                                statusFeedback = "❌ Gagal memperbarui: ${result.exceptionOrNull()?.message}"
                            }
                        }
                    }

                    if (onActionWithProductionGuard != null) {
                        onActionWithProductionGuard("Simpan Konten Footer", "Footer Global Web", false) {
                            performSave()
                        }
                    } else {
                        performSave()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = BrandTeal),
                shape = RoundedCornerShape(100.dp),
                enabled = !isSaving,
                modifier = Modifier.height(48.dp)
            ) {
                Text(
                    text = if (isSaving) "Menyimpan..." else "💾 Simpan Perubahan Footer",
                    fontFamily = getDmSansFontFamily(),
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            OutlinedButton(
                onClick = {
                    config = com.sekota.features.admin.domain.model.FooterConfig()
                    statusFeedback = "ℹ️ Reset ke konfigurasi bawaan (Tekan Simpan untuk menerapkan)"
                },
                shape = RoundedCornerShape(100.dp),
                modifier = Modifier.height(48.dp)
            ) {
                Text(
                    text = "↺ Reset Bawaan",
                    fontFamily = getDmSansFontFamily(),
                    color = InkNavy
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun FooterColumnEditorCard(
    columnTitle: String,
    columnConfig: com.sekota.features.admin.domain.model.FooterColumnConfig,
    onColumnChange: (com.sekota.features.admin.domain.model.FooterColumnConfig) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = columnTitle,
                    fontFamily = getMontserratFontFamily(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = InkNavy
                )
                Button(
                    onClick = {
                        val nextIdx = columnConfig.items.size + 1
                        val newItem = com.sekota.features.admin.domain.model.FooterLinkItem(
                            id = "item_${kotlin.random.Random.nextInt(1000, 9999)}",
                            label = "Menu Baru #$nextIdx",
                            urlOrCode = "",
                            sortOrder = nextIdx
                        )
                        onColumnChange(columnConfig.copy(items = columnConfig.items + newItem))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandTeal),
                    shape = RoundedCornerShape(100.dp)
                ) {
                    Text("➕ Tambah Tautan", fontFamily = getDmSansFontFamily(), fontSize = 12.sp, color = Color.White)
                }
            }

            OutlinedTextField(
                value = columnConfig.title,
                onValueChange = { onColumnChange(columnConfig.copy(title = it)) },
                label = { Text("Judul Kolom", fontFamily = getDmSansFontFamily()) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            columnConfig.items.forEachIndexed { index, item ->
                Surface(
                    color = Color(0xFFF8FAFC),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = item.label,
                            onValueChange = { newLabel ->
                                val list = columnConfig.items.toMutableList()
                                list[index] = item.copy(label = newLabel)
                                onColumnChange(columnConfig.copy(items = list))
                            },
                            label = { Text("Label", fontFamily = getDmSansFontFamily(), fontSize = 11.sp) },
                            modifier = Modifier.weight(1.2f),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = item.urlOrCode,
                            onValueChange = { newTarget ->
                                val list = columnConfig.items.toMutableList()
                                list[index] = item.copy(urlOrCode = newTarget)
                                onColumnChange(columnConfig.copy(items = list))
                            },
                            label = { Text("Target / Kode / URL", fontFamily = getDmSansFontFamily(), fontSize = 11.sp) },
                            modifier = Modifier.weight(1.5f),
                            singleLine = true
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = item.isVisible,
                                onCheckedChange = { checked ->
                                    val list = columnConfig.items.toMutableList()
                                    list[index] = item.copy(isVisible = checked)
                                    onColumnChange(columnConfig.copy(items = list))
                                }
                            )
                            Text("Aktif", fontFamily = getDmSansFontFamily(), fontSize = 12.sp)
                        }

                        IconButton(
                            onClick = {
                                val list = columnConfig.items.toMutableList()
                                list.removeAt(index)
                                onColumnChange(columnConfig.copy(items = list))
                            }
                        ) {
                            com.sekota.components.SekotaTrashVectorIcon(size = 18.dp)
                        }
                    }
                }
            }
        }
    }
}

@Preview(device = DESKTOP)
@Composable
fun AdminDashboardPreview() {
    MaterialTheme {
        AdminDashboardScreen()
    }
}

@Preview(device = DESKTOP)
@Composable
fun ValueLoopTabPreview() {
    MaterialTheme {
        val repo = remember { com.sekota.features.admin.data.repository.AdminRepositoryImpl() }
        ValueLoopTab(
            getValueLoopUseCase = remember { GetAdminValueLoopUseCase(repo) },
            saveValueLoopUseCase = remember { SaveAdminValueLoopUseCase(repo) },
            windowWidth = WindowWidth.Expanded
        )
    }
}

@Preview(device = DESKTOP)
@Composable
fun WhySekotaTabPreview() {
    MaterialTheme {
        val repo = remember { com.sekota.features.admin.data.repository.AdminRepositoryImpl() }
        WhySekotaTab(
            getWhySekotaUseCase = remember { GetAdminWhySekotaUseCase(repo) },
            saveWhySekotaUseCase = remember { SaveAdminWhySekotaUseCase(repo) },
            windowWidth = WindowWidth.Expanded
        )
    }
}

@Preview(device = DESKTOP)
@Composable
fun BooksRegistryTabPreview() {
    MaterialTheme {
        val repo = remember { com.sekota.features.admin.data.repository.AdminRepositoryImpl() }
        BooksRegistryTab(
            getBooksUseCase = remember { GetAdminBooksUseCase(repo) },
            saveBookUseCase = remember { SaveAdminBookUseCase(repo) },
            getCategoriesUseCase = remember { GetAdminCategoriesUseCase(repo) },
            addCategoryUseCase = remember { AddAdminCategoryUseCase(repo) },
            deleteCategoryUseCase = remember { DeleteAdminCategoryUseCase(repo) },
            windowWidth = WindowWidth.Expanded
        )
    }
}

@Preview(device = DESKTOP)
@Composable
fun ClientInquiriesTabPreview() {
    MaterialTheme {
        val repo = remember { com.sekota.features.admin.data.repository.AdminRepositoryImpl() }
        ClientInquiriesTab(
            getInquiriesUseCase = remember { GetAdminInquiriesUseCase(repo) },
            deleteInquiryUseCase = remember { DeleteAdminInquiryUseCase(repo) },
            getStrategicConversationUseCase = remember { GetAdminStrategicConversationUseCase(repo) },
            saveStrategicConversationUseCase = remember { SaveAdminStrategicConversationUseCase(repo) },
            windowWidth = WindowWidth.Expanded
        )
    }
}

@Preview(device = DESKTOP)
@Composable
fun FooterManagementTabPreview() {
    MaterialTheme {
        val repo = remember { com.sekota.features.admin.data.repository.AdminRepositoryImpl() }
        FooterManagementTab(
            getFooterUseCase = remember { GetAdminFooterUseCase(repo) },
            saveFooterUseCase = remember { SaveAdminFooterUseCase(repo) },
            windowWidth = WindowWidth.Expanded
        )
    }
}

@Composable
fun AppBarManagementTab(
    getNavigationItemsUseCase: GetAdminNavigationItemsUseCase,
    saveNavigationItemsUseCase: SaveAdminNavigationItemsUseCase,
    windowWidth: WindowWidth,
    onActionWithProductionGuard: ((String, String, Boolean, () -> Unit) -> Unit)? = null
) {
    val scope = rememberCoroutineScope()
    var items by remember { mutableStateOf<List<com.sekota.features.navigation.domain.model.NavigationItem>>(emptyList()) }
    var statusFeedback by remember { mutableStateOf<String?>(null) }
    var isSaving by remember { mutableStateOf(false) }

    var editingItem by remember { mutableStateOf<com.sekota.features.navigation.domain.model.NavigationItem?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        items = getNavigationItemsUseCase()
        com.sekota.syncService.syncEventFlow.collect {
            items = getNavigationItemsUseCase()
        }
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        WorkbenchHeader(
            title = "AppBar Navigation Management",
            subtitle = "Kelola struktur tautan navigasi utama (AppBar), visibilitas, rute, dan urutan tampilan secara dinamis.",
            windowWidth = windowWidth,
            actionLabel = "+ Tambah Item Navigasi",
            onAction = {
                val maxOrder = items.maxOfOrNull { it.order } ?: 0
                editingItem = com.sekota.features.navigation.domain.model.NavigationItem(
                    id = "nav_" + kotlin.random.Random.nextInt(1000, 9999),
                    label = "",
                    route = "#",
                    order = maxOrder + 1,
                    isVisible = true
                )
                showDialog = true
            }
        )

        statusFeedback?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                color = if (it.startsWith("✅")) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = it,
                    fontFamily = getDmSansFontFamily(),
                    color = if (it.startsWith("✅")) Color(0xFF2E7D32) else Color(0xFFC62828),
                    modifier = Modifier.padding(16.dp),
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Reset to Canonical Action Card
        Surface(
            color = Color.White,
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Reset ke Navigasi Bawaan (Canonical)",
                        fontFamily = getMontserratFontFamily(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = InkNavy
                    )
                    Text(
                        text = "Kembalikan 5 tautan standar: Solusi (#solusi), Produk (#produk), E-Book (/catalog), Merchandise (/merchandise), dan Kontak (#kontak).",
                        fontFamily = getDmSansFontFamily(),
                        fontSize = 13.sp,
                        color = Color(0xFF64748B)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                OutlinedButton(
                    onClick = {
                        val executeReset: () -> Unit = {
                            scope.launch {
                                isSaving = true
                                val canonical = com.sekota.features.navigation.domain.model.NavigationItem.CANONICAL_ITEMS
                                val res = saveNavigationItemsUseCase(canonical)
                                if (res.isSuccess) {
                                    items = res.getOrThrow()
                                    statusFeedback = "✅ Berhasil mereset navigasi ke 5 tautan standar canonical!"
                                    syncService.triggerLocalUpdate()
                                } else {
                                    statusFeedback = "❌ Gagal reset: ${res.exceptionOrNull()?.message}"
                                }
                                isSaving = false
                            }
                        }
                        if (onActionWithProductionGuard != null) {
                            onActionWithProductionGuard("Reset Navigasi Bawaan", "AppBar Navigation", false, executeReset)
                        } else {
                            executeReset()
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = BrandTeal)
                ) {
                    Text(text = "Reset Bawaan", fontFamily = getDmSansFontFamily(), fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Navigation Items List Table / Cards
        Surface(
            color = Color.White,
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Daftar Tautan Aktif (${items.size} item)",
                    fontFamily = getMontserratFontFamily(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = InkNavy
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (items.isEmpty()) {
                    Text(
                        text = "Belum ada item navigasi yang tersimpan. Klik '+ Tambah Item Navigasi' atau 'Reset Bawaan'.",
                        fontFamily = getDmSansFontFamily(),
                        color = Color(0xFF94A3B8),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                } else {
                    val sortedList = items.sortedBy { it.order }
                    sortedList.forEachIndexed { index, item ->
                        Surface(
                            color = if (item.isVisible) Color(0xFFF8FAFC) else Color(0xFFFFF1F2),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, if (item.isVisible) Color(0xFFE2E8F0) else Color(0xFFFECDD3)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Surface(
                                        color = BrandTeal.copy(alpha = 0.15f),
                                        shape = CircleShape,
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = "${item.order}",
                                                fontFamily = getDmSansFontFamily(),
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                color = BrandTeal
                                            )
                                        }
                                    }

                                    Column {
                                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            Text(
                                                text = item.label,
                                                fontFamily = getMontserratFontFamily(),
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 15.sp,
                                                color = InkNavy
                                            )
                                            if (!item.isVisible) {
                                                Surface(
                                                    color = Color(0xFFF43F5E),
                                                    shape = RoundedCornerShape(4.dp),
                                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                ) {
                                                    Text(
                                                        text = "HIDDEN",
                                                        fontFamily = getDmSansFontFamily(),
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color.White
                                                    )
                                                }
                                            }
                                        }
                                        Text(
                                            text = "Route: ${item.route}",
                                            fontFamily = getDmSansFontFamily(),
                                            fontSize = 12.sp,
                                            color = Color(0xFF64748B)
                                        )
                                    }
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    // Move Up / Down
                                    IconButton(
                                        onClick = {
                                            if (index > 0) {
                                                val prevItem = sortedList[index - 1]
                                                val updatedList = sortedList.toMutableList()
                                                val currentOrder = item.order
                                                updatedList[index] = item.copy(order = prevItem.order)
                                                updatedList[index - 1] = prevItem.copy(order = currentOrder)
                                                val executeMove: () -> Unit = {
                                                    scope.launch {
                                                        val res = saveNavigationItemsUseCase(updatedList)
                                                        if (res.isSuccess) {
                                                            items = res.getOrThrow()
                                                            syncService.triggerLocalUpdate()
                                                        }
                                                    }
                                                }
                                                if (onActionWithProductionGuard != null) {
                                                    onActionWithProductionGuard("Pindahkan Item Navigasi Ke Atas", item.label, false, executeMove)
                                                } else {
                                                    executeMove()
                                                }
                                            }
                                        },
                                        enabled = index > 0,
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        com.sekota.components.SekotaArrowUpVectorIcon(
                                            size = 18.dp,
                                            tint = if (index > 0) InkNavy else Color.LightGray
                                        )
                                    }

                                    IconButton(
                                        onClick = {
                                            if (index < sortedList.size - 1) {
                                                val nextItem = sortedList[index + 1]
                                                val updatedList = sortedList.toMutableList()
                                                val currentOrder = item.order
                                                updatedList[index] = item.copy(order = nextItem.order)
                                                updatedList[index + 1] = nextItem.copy(order = currentOrder)
                                                val executeMove: () -> Unit = {
                                                    scope.launch {
                                                        val res = saveNavigationItemsUseCase(updatedList)
                                                        if (res.isSuccess) {
                                                            items = res.getOrThrow()
                                                            syncService.triggerLocalUpdate()
                                                        }
                                                    }
                                                }
                                                if (onActionWithProductionGuard != null) {
                                                    onActionWithProductionGuard("Pindahkan Item Navigasi Ke Bawah", item.label, false, executeMove)
                                                } else {
                                                    executeMove()
                                                }
                                            }
                                        },
                                        enabled = index < sortedList.size - 1,
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        com.sekota.components.SekotaArrowDownVectorIcon(
                                            size = 18.dp,
                                            tint = if (index < sortedList.size - 1) InkNavy else Color.LightGray
                                        )
                                    }

                                    // Edit
                                    OutlinedButton(
                                        onClick = {
                                            editingItem = item
                                            showDialog = true
                                        },
                                        shape = RoundedCornerShape(6.dp),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                        modifier = Modifier.height(32.dp)
                                    ) {
                                        Text(text = "Edit", fontSize = 12.sp, fontFamily = getDmSansFontFamily())
                                    }

                                    // Delete
                                    IconButton(
                                        onClick = {
                                            val executeDelete: () -> Unit = {
                                                scope.launch {
                                                    val updated = items.filterNot { it.id == item.id }
                                                    val res = saveNavigationItemsUseCase(updated)
                                                    if (res.isSuccess) {
                                                        items = res.getOrThrow()
                                                        statusFeedback = "✅ Item '${item.label}' berhasil dihapus."
                                                        syncService.triggerLocalUpdate()
                                                    } else {
                                                        statusFeedback = "❌ Gagal hapus: ${res.exceptionOrNull()?.message}"
                                                    }
                                                }
                                            }
                                            if (onActionWithProductionGuard != null) {
                                                onActionWithProductionGuard("Hapus Item Navigasi", item.label, true, executeDelete)
                                            } else {
                                                executeDelete()
                                            }
                                        },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        com.sekota.components.SekotaTrashVectorIcon(size = 18.dp, tint = Color(0xFFEF4444))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Dialog for adding / editing navigation item
    if (showDialog && editingItem != null) {
        val currentItem = editingItem!!
        var labelInput by remember { mutableStateOf(currentItem.label) }
        var routeInput by remember { mutableStateOf(currentItem.route) }
        var orderInput by remember { mutableStateOf(currentItem.order.toString()) }
        var isVisibleInput by remember { mutableStateOf(currentItem.isVisible) }
        var dialogError by remember { mutableStateOf<String?>(null) }

        AlertDialog(
            onDismissRequest = {
                showDialog = false
                editingItem = null
            },
            title = {
                Text(
                    text = if (items.any { it.id == currentItem.id }) "Edit Item Navigasi" else "Tambah Item Navigasi",
                    fontFamily = getMontserratFontFamily(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = InkNavy
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    dialogError?.let {
                        Text(
                            text = it,
                            fontFamily = getDmSansFontFamily(),
                            color = Color(0xFFEF4444),
                            fontSize = 13.sp
                        )
                    }

                    OutlinedTextField(
                        value = labelInput,
                        onValueChange = { labelInput = it },
                        label = { Text("Label Navigasi") },
                        placeholder = { Text("misal: Solusi, E-Book, Insights") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = routeInput,
                        onValueChange = { routeInput = it },
                        label = { Text("Route / Hash Target") },
                        placeholder = { Text("misal: #solusi, /catalog, #kontak") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = orderInput,
                        onValueChange = { orderInput = it },
                        label = { Text("Urutan Tampilan (Order)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.clickable { isVisibleInput = !isVisibleInput }
                    ) {
                        Checkbox(
                            checked = isVisibleInput,
                            onCheckedChange = { isVisibleInput = it }
                        )
                        Text(
                            text = "Tampilkan di AppBar (Visible)",
                            fontFamily = getDmSansFontFamily(),
                            fontSize = 14.sp,
                            color = InkNavy
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (labelInput.isBlank()) {
                            dialogError = "Label tidak boleh kosong"
                            return@Button
                        }
                        if (routeInput.isBlank()) {
                            dialogError = "Route tidak boleh kosong"
                            return@Button
                        }
                        val parsedOrder = orderInput.toIntOrNull() ?: currentItem.order

                        val updatedItem = currentItem.copy(
                            label = labelInput.trim(),
                            route = routeInput.trim(),
                            order = parsedOrder,
                            isVisible = isVisibleInput
                        )

                        val executeSave: () -> Unit = {
                            scope.launch {
                                val currentList = items.filterNot { it.id == updatedItem.id }.toMutableList()
                                currentList.add(updatedItem)
                                val res = saveNavigationItemsUseCase(currentList)
                                if (res.isSuccess) {
                                    items = res.getOrThrow()
                                    statusFeedback = "✅ Item '${updatedItem.label}' berhasil disimpan!"
                                    showDialog = false
                                    editingItem = null
                                    syncService.triggerLocalUpdate()
                                } else {
                                    dialogError = res.exceptionOrNull()?.message
                                }
                            }
                        }

                        if (onActionWithProductionGuard != null) {
                            onActionWithProductionGuard("Simpan Item Navigasi", updatedItem.label, false, executeSave)
                        } else {
                            executeSave()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandTeal)
                ) {
                    Text(text = "Simpan", fontFamily = getDmSansFontFamily(), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        showDialog = false
                        editingItem = null
                    }
                ) {
                    Text(text = "Batal", fontFamily = getDmSansFontFamily())
                }
            }
        )
    }
}

@Preview(device = DESKTOP)
@Composable
fun AppBarManagementTabPreview() {
    MaterialTheme {
        val repo = remember { com.sekota.features.admin.data.repository.AdminRepositoryImpl() }
        AppBarManagementTab(
            getNavigationItemsUseCase = remember { GetAdminNavigationItemsUseCase(repo) },
            saveNavigationItemsUseCase = remember { SaveAdminNavigationItemsUseCase(repo) },
            windowWidth = WindowWidth.Expanded
        )
    }
}



