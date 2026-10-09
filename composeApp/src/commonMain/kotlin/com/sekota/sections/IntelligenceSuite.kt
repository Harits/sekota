package com.sekota.sections

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.sekota.*
import com.sekota.features.admin.data.repository.AdminRepositoryImpl
import com.sekota.features.admin.domain.model.AdminProduct
import com.sekota.features.admin.domain.usecase.GetAdminProductsUseCase
import com.sekota.ui.contentHorizontalPadding
import com.sekota.ui.WindowWidth
import com.sekota.ui.gridSpacing
import com.sekota.ui.sectionHorizontalPadding
import com.sekota.ui.sectionVerticalPadding
import com.sekota.ui.windowWidthOf
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import sekota.composeapp.generated.resources.Res
import sekota.composeapp.generated.resources.*

@Composable
fun IntelligenceSuite(
    onProductClick: (AdminProduct) -> Unit = {}
) {
    val repository = remember { AdminRepositoryImpl() }
    val getProductsUseCase = remember { GetAdminProductsUseCase(repository) }
    var products by remember { mutableStateOf<List<AdminProduct>>(emptyList()) }

    LaunchedEffect(Unit) {
        products = getProductsUseCase()
        syncService.syncEventFlow.collect {
            products = getProductsUseCase()
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val windowWidth = windowWidthOf(maxWidth)
        val columns = when (windowWidth) {
            WindowWidth.Compact -> 1
            WindowWidth.Medium, WindowWidth.Expanded -> 2
            WindowWidth.Large, WindowWidth.ExtraLarge -> 4
        }
        val gridSpacing = windowWidth.gridSpacing

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = windowWidth.sectionVerticalPadding,
                horizontal = contentHorizontalPadding(maxWidth)
            )
    ) {
        // The eyebrow/title and the supporting line sit side by side only when
        // there is room; otherwise the line stacks under the title, left aligned.
        if (windowWidth.isAtMostMedium) {
            Column(modifier = Modifier.fillMaxWidth()) {
                SuiteHeaderTitle(windowWidth)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Dirancang untuk saling terhubung dalam satu ekosistem data yang kohesif.",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    fontFamily = getDmSansFontFamily(),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Box(modifier = Modifier.weight(1f)) { SuiteHeaderTitle(windowWidth) }
                Text(
                    text = "Dirancang untuk saling terhubung dalam satu ekosistem data yang kohesif.",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    fontFamily = getDmSansFontFamily(),
                    modifier = Modifier.widthIn(max = 350.dp).padding(bottom = 8.dp, start = 24.dp),
                    textAlign = TextAlign.End
                )
            }
        }
        Spacer(modifier = Modifier.height(if (windowWidth.isCompact) 40.dp else 64.dp))

        Column(verticalArrangement = Arrangement.spacedBy(gridSpacing)) {
            products.chunked(columns).forEach { rowProducts ->
                Row(
                    modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max),
                    horizontalArrangement = Arrangement.spacedBy(gridSpacing)
                ) {
                    rowProducts.forEach { product ->
                        val logo = when (product.code.uppercase()) {
                            "VRD" -> Res.drawable.icon_veridia
                            "ASC" -> Res.drawable.icon_acscendio
                            "SOC" -> Res.drawable.icon_sociara
                            "ECO" -> Res.drawable.icon_ecoflow
                            else -> Res.drawable.icon_veridia
                        }
                        val defaultAccentColor = when (product.code.uppercase()) {
                            "VRD" -> Color(0xFF00BFA5)
                            "ASC" -> Color(0xFF1976D2)
                            "SOC" -> Color(0xFF8BC34A)
                            "ECO" -> Color(0xFF4CAF50)
                            else -> Color(0xFF00B5C8)
                        }
                        val customHex = product.accentColorHex
                        val accentColor = if (!customHex.isNullOrBlank()) {
                            try {
                                val cleanHex = customHex.removePrefix("#")
                                Color(cleanHex.toLong(16) or 0x00000000FF000000)
                            } catch (_: Exception) {
                                defaultAccentColor
                            }
                        } else defaultAccentColor

                        SuiteCard(
                            category = product.categoryEyebrow.uppercase(),
                            title = product.name.uppercase(),
                            description = product.description,
                            features = product.features,
                            logo = logo,
                            accentColor = accentColor,
                            customIconUrlOrBase64 = product.iconUrlOrBase64,
                            compactPadding = windowWidth.isCompact,
                            minHeight = if (columns == 1) 0.dp else 480.dp,
                            modifier = Modifier.weight(1f).fillMaxHeight(),
                            onClick = { onProductClick(product) }
                        )
                    }
                    repeat(columns - rowProducts.size) {
                        Box(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
    }
}

@Composable
private fun SuiteHeaderTitle(windowWidth: WindowWidth) {
    Column {
        Text(
            text = "EKOSISTEM PRODUK",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF60BD65),
            fontFamily = getDmSansFontFamily()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Intelligence Suite.",
            fontSize = if (windowWidth.isCompact) 30.sp else 40.sp,
            lineHeight = if (windowWidth.isCompact) 38.sp else 48.sp,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = getMontserratFontFamily()
        )
    }
}

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun SuiteCard(
    category: String,
    title: String,
    description: String,
    features: List<String>,
    logo: DrawableResource,
    accentColor: Color,
    customIconUrlOrBase64: String? = null,
    modifier: Modifier = Modifier,
    compactPadding: Boolean = false,
    minHeight: androidx.compose.ui.unit.Dp = 480.dp,
    onClick: () -> Unit = {}
) {
    val customBitmap = remember(customIconUrlOrBase64) {
        if (!customIconUrlOrBase64.isNullOrBlank()) {
            com.sekota.utils.decodeBase64ToBitmap(customIconUrlOrBase64)
        } else null
    }

    Card(
        modifier = modifier
            .heightIn(min = minHeight)
            .pointerHoverIcon(PointerIcon.Hand)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            hoveredElevation = 8.dp,
            pressedElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = minHeight)
                .padding(if (compactPadding) 24.dp else 32.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                // Header with M3 pill tag & logo
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(),
                        tooltip = {
                            PlainTooltip(
                                shape = RoundedCornerShape(8.dp),
                                containerColor = Color(0xFF0F172A),
                                contentColor = Color.White
                            ) {
                                Text(
                                    text = category,
                                    fontSize = 12.sp,
                                    fontFamily = getDmSansFontFamily(),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        },
                        state = androidx.compose.material3.rememberTooltipState(),
                        modifier = Modifier.weight(1f, fill = false).padding(end = 12.dp)
                    ) {
                        Surface(
                            color = accentColor.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = category,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = accentColor,
                                fontFamily = getDmSansFontFamily(),
                                letterSpacing = 1.sp,
                                maxLines = 2,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }

                    if (customBitmap != null) {
                        Image(
                            bitmap = customBitmap,
                            contentDescription = "$title Custom Logo",
                            modifier = Modifier.size(44.dp)
                        )
                    } else {
                        Image(
                            painter = painterResource(logo),
                            contentDescription = "$title Logo",
                            modifier = Modifier.size(44.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = title,
                    fontSize = 28.sp,
                    lineHeight = 34.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = getMontserratFontFamily(),
                    color = Color(0xFF0F172A)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = Color(0xFF475569),
                    fontFamily = getDmSansFontFamily(),
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(20.dp))
                
                features.forEach { feature ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 5.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(RoundedCornerShape(50))
                                .background(accentColor)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = feature,
                            fontSize = 13.sp,
                            color = Color(0xFF334155),
                            fontFamily = getDmSansFontFamily(),
                            lineHeight = 18.sp
                        )
                    }
                }
            }
            
            // Interactive M3 Text Action with authentic Material Icon vector
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .pointerHoverIcon(PointerIcon.Hand)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onClick
                    )
            ) {
                Text(
                    text = "Pelajari Selengkapnya",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00B5C8),
                    fontFamily = getDmSansFontFamily()
                )
                Spacer(modifier = Modifier.width(8.dp))
                Canvas(modifier = Modifier.size(14.dp)) {
                    val strokeWidth = 2.dp.toPx()
                    val color = Color(0xFF00B5C8)
                    // Horizontal arrow shaft
                    drawLine(
                        color = color,
                        start = Offset(0f, size.height / 2f),
                        end = Offset(size.width, size.height / 2f),
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round
                    )
                    // Upper arrowhead wing
                    drawLine(
                        color = color,
                        start = Offset(size.width - 5.dp.toPx(), (size.height / 2f) - 5.dp.toPx()),
                        end = Offset(size.width, size.height / 2f),
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round
                    )
                    // Lower arrowhead wing
                    drawLine(
                        color = color,
                        start = Offset(size.width - 5.dp.toPx(), (size.height / 2f) + 5.dp.toPx()),
                        end = Offset(size.width, size.height / 2f),
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round
                    )
                }
            }
        }
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun IntelligenceSuitePreview() {
    androidx.compose.material3.MaterialTheme {
        IntelligenceSuite()
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun IntelligenceSuiteDefaultPreview() {
    androidx.compose.material3.MaterialTheme {
        IntelligenceSuite()
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun IntelligenceSuiteCardSelectedPreview() {
    androidx.compose.material3.MaterialTheme {
        Column(
            modifier = Modifier.padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                "SuiteCard Variants (Different Enterprise Tags & Accents)",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = getMontserratFontFamily()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                SuiteCard(
                    category = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse molestie dictum suscipit. Sed venenatis quam vitae justo pulvinar venenatis. Phasellus consequat lacus eu diam scelerisque, id maximus nunc efficitur. Sed condimentum rhoncus lectus. Quisque vitae erat eu massa volutpat porta. Suspendisse et orci ut tellus imperdiet laoreet. Duis lobortis mi sem, nec dictum turpis fringilla eget. Mauris varius faucibus lorem, eu lacinia est accumsan sed. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. In condimentum dui metus, id imperdiet orci mollis in. Sed tempor volutpat pretium. In hac habitasse platea dictumst. Fusce sagittis nisl orci, ut malesuada felis tincidunt et. Ut at suscipit ante. Nunc egestas massa sed faucibus aliquam.",
                    title = "VERIDIA",
                    description = "Platform simulasi skenario kebijakan dan prediksi dampak sosial multi-sektor.",
                    features = listOf(
                        "Dynamic Scenario Modeling",
                        "Multi-variable Risk Assessment",
                        "Cross-sector Impact Forecasting"
                    ),
                    logo = Res.drawable.icon_veridia,
                    accentColor = Color(0xFF00BFA5),
                    modifier = Modifier.weight(1f)
                )

                SuiteCard(
                    category = "CORPORATE GOVERNANCE",
                    title = "ASCENDIO",
                    description = "Sistem pengukuran reputasi dan sentimen pemangku kepentingan real-time.",
                    features = listOf(
                        "Real-time Sentiment Engine",
                        "Stakeholder Mapping Matrix",
                        "Reputation Risk Radar"
                    ),
                    logo = Res.drawable.icon_acscendio,
                    accentColor = Color(0xFF1976D2),
                    modifier = Modifier.weight(1f)
                )

                SuiteCard(
                    category = "SUSTAINABILITY & ESG",
                    title = "ECOFLOW",
                    description = "Automasi pelaporan ESG dan pemantauan jejak karbon berbasis standar global.",
                    features = listOf(
                        "GRI & ISSB Standard Alignment",
                        "Carbon Footprint Tracker",
                        "Automated Assurance Reports"
                    ),
                    logo = Res.drawable.icon_ecoflow,
                    accentColor = Color(0xFF4CAF50),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun IntelligenceSuiteCustomIconCardPreview() {
    androidx.compose.material3.MaterialTheme {
        Box(modifier = Modifier.padding(32.dp).width(360.dp)) {
            SuiteCard(
                category = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                title = "CYBER PULSE",
                description = "Pemantauan anomali lalu lintas perkotaan dan respons otomatis tanggap darurat.",
                features = listOf(
                    "Real-time Traffic Telemetry",
                    "Predictive Incident Management",
                    "Automated Dispatch Gateway"
                ),
                logo = Res.drawable.icon_veridia,
                accentColor = Color(0xFF00B5C8),
                // Prominent 48x48 cyan shield icon Base64 for previewing custom icon branch
                customIconUrlOrBase64 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAAAAXNSR0IArs4c6QAAAERlWElmTU0AKgAAAAgAAYdpAAQAAAABAAAAGgAAAAAAA6ABAAMAAAABAAEAAKACAAQAAAABAAAAMKADAAQAAAABAAAAMAAAAABUo+ClAAACjklEQVRoBe2YvWsUQRTGZ3fX3CRGoyEQbAwRsfcPxB+I2AkWqRRUUBG7FFQsoqDYW4iFpBAhhYUg2NgIaGvjBwiKqIka73bmXfa4vXv39mb3bm4P5jZvu/Pmff/ed2fmbm9nZ8a8q17Vq/7Fq37f/0080z3nE6/B18U+6fUf9qS+9T/t7X5p3zD0v+P40y+T/2eA+4V/Uo+yq78HwB0cAC60AAYD64HBwDpgcDYf4B7gD8K/4yD6Gge4V9i/D584v0z6Z7pP34u6nfsd0r9vWf/bE532R/yXG+/A/i7A7wH+0t2r7hU1D/B6YV4sFgv5fF5bW1va3Ny0/N3d3eq2tbX1f8eZ/p18ZwfAG4D/gL+K/S/3r0oB3N3dVXt7e3pwkMlkdPny5c8fL6A0G61qAGj8u/uH2H/r/qEUAMzW19d1f39fBwcHNT0ePnzoe4iG/fWPAQBg94O7R4UAgB8eHqrmF0Zra2t9r77e/w4AvB/cPTs/AMB2dnZi35m2tLRUfR8A3M/unhsfANzc3Gz6ztS5ubkBAHb3vLs3xweA6enp2Hd8bW1tzf8A3j539870+QDA6Oio7e/v2+jra3Fx8U+N/Z2x7p4bHwCwt7f32wGwtbU18L7x97y796fHB4DJycnYd3zNzMx8X21n/u+7+23qcwBwOzs7v913ZqempqrfA8DeC+5enj4fAEj08/q8e1QJgEQ/L3r7e3oA8OH+/gA93gJ48eJF4zU9NTVVffc8AHA7Ojq6f1QJgEQ/b27u71cCYPzly5e/7Ttzc3PVEwFw+/j4+O5RJQAS/bxvPj4BvHjxYsB94+/5+Pj44/kAAOPf3V1RAkCiP56u/oMv3d0/Z2Bf7r8FwD+xR/3r+v17AP4CSU0E+e8Qk2oAAAAASUVORK5CYRNOTEAAA==",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun IntelligenceSuiteCompactMobilePreview() {
    androidx.compose.material3.MaterialTheme {
        Box(modifier = Modifier.width(375.dp)) {
            IntelligenceSuite()
        }
    }
}

