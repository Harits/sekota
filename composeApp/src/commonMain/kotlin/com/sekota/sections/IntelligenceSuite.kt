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
                        val accentColor = when (product.code.uppercase()) {
                            "VRD" -> Color(0xFF00BFA5)
                            "ASC" -> Color(0xFF1976D2)
                            "SOC" -> Color(0xFF8BC34A)
                            "ECO" -> Color(0xFF4CAF50)
                            else -> Color(0xFF00B5C8)
                        }

                        SuiteCard(
                            category = product.categoryEyebrow.uppercase(),
                            title = product.name.uppercase(),
                            description = product.description,
                            features = product.features,
                            logo = logo,
                            accentColor = accentColor,
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

@Composable
fun SuiteCard(
    category: String,
    title: String,
    description: String,
    features: List<String>,
    logo: DrawableResource,
    accentColor: Color,
    modifier: Modifier = Modifier,
    compactPadding: Boolean = false,
    minHeight: androidx.compose.ui.unit.Dp = 480.dp,
    onClick: () -> Unit = {}
) {
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
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }

                    Image(
                        painter = painterResource(logo),
                        contentDescription = "$title Logo",
                        modifier = Modifier.size(44.dp)
                    )
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
    IntelligenceSuite()
}
