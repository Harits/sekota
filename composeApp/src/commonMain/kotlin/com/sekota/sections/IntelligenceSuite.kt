package com.sekota.sections

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.sekota.ui.sectionHorizontalPadding
import com.sekota.ui.sectionVerticalPadding
import com.sekota.ui.windowWidthOf
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import sekota.composeapp.generated.resources.Res
import sekota.composeapp.generated.resources.*

@Composable
fun IntelligenceSuite() {
    val repository = remember { AdminRepositoryImpl() }
    val getProductsUseCase = remember { GetAdminProductsUseCase(repository) }
    var products by remember { mutableStateOf<List<AdminProduct>>(emptyList()) }

    LaunchedEffect(Unit) {
        products = getProductsUseCase()
    }

    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val windowWidth = windowWidthOf(maxWidth)
        // Two side-by-side suite cards need ~360dp each to hold their feature list.
        val columns = if (windowWidth.isAtMostMedium) 1 else 2
        val gridSpacing = if (windowWidth.isCompact) 20.dp else 32.dp

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
                            modifier = Modifier.weight(1f).fillMaxHeight()
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
    // In a 2-up grid the pair is equalised to a 480dp floor so the cards line up.
    // A single full-width card has no partner to match, so it wraps its content
    // instead of stranding whitespace above the footer link.
    minHeight: androidx.compose.ui.unit.Dp = 480.dp
) {
    Card(
        // heightIn rather than a fixed height: a narrow card wraps its description
        // and feature list onto more lines and must be allowed to grow.
        modifier = modifier.heightIn(min = minHeight),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = minHeight)
                .padding(if (compactPadding) 24.dp else 32.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = category,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = accentColor,
                    fontFamily = getDmSansFontFamily(),
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        fontSize = 28.sp,
                        lineHeight = 34.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = getMontserratFontFamily(),
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Image(
                        painter = painterResource(logo),
                        contentDescription = "$title Logo",
                        modifier = Modifier.size(48.dp)
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontFamily = getDmSansFontFamily(),
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
                
                features.forEach { feature ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(accentColor, RoundedCornerShape(50))
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = feature,
                            fontSize = 13.sp,
                            color = Color.DarkGray,
                            fontFamily = getDmSansFontFamily()
                        )
                    }
                }
            }
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(
                    text = "Pelajari Selengkapnya",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF60BD65),
                    fontFamily = getDmSansFontFamily()
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "→",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF60BD65)
                )
            }
        }
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun IntelligenceSuitePreview() {
    IntelligenceSuite()
}
