package com.sekota.sections

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.sekota.*
import com.sekota.ui.contentHorizontalPadding
import com.sekota.ui.cardPadding
import com.sekota.ui.sectionHorizontalPadding
import com.sekota.ui.sectionVerticalPadding
import com.sekota.ui.windowWidthOf
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import sekota.composeapp.generated.resources.*

@Composable
fun FeatureGrid() {
    BoxWithConstraints {
        val windowWidth = windowWidthOf(maxWidth)
        // Three feature cards side by side need ~840dp to keep their copy readable.
        val isCompact = !windowWidth.isAtLeastExpanded
        val paddingHorizontal = contentHorizontalPadding(maxWidth)
        val titleSize = if (windowWidth.isCompact) 32.sp else 48.sp
        val titleLineHeight = if (windowWidth.isCompact) 40.sp else 56.sp

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF8FAFB))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = windowWidth.sectionVerticalPadding, horizontal = paddingHorizontal),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "TANTANGAN 2026",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00A99D),
                    fontFamily = getDmSansFontFamily(),
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Data Melimpah,\nKeputusan Masih Kabur.",
                    fontSize = titleSize,
                    lineHeight = titleLineHeight,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = getMontserratFontFamily(),
                    textAlign = TextAlign.Start,
                    color = Color(0xFF122329)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Di tahun 2026, organisasi modern menghadapi paradoks data, volume informasi yang belum pernah sebesar ini, namun kepercayaan terhadap data justru semakin menipis.",
                    fontSize = 18.sp,
                    color = Color(0xFF122329).copy(alpha = 0.6f),
                    fontFamily = getDmSansFontFamily(),
                    textAlign = TextAlign.Start,
                    modifier = Modifier.widthIn(max = 850.dp),
                    lineHeight = 28.sp
                )
                Spacer(modifier = Modifier.height(if (windowWidth.isCompact) 40.dp else 64.dp))

                if (isCompact) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        FeatureCard(
                            title = "Data Tidak Terintegrasi",
                            description = "Silo data antar departemen dan sistem yang tidak terhubung menghasilkan insight yang fragmentaris dan tidak dapat diandalkan untuk keputusan level eksekutif.",
                            icon = Res.drawable.icon,
                            iconBgColor = Color(0xFFFEE2E2),
                            iconTintColor = Color(0xFFE24B4A),
                            contentPadding = windowWidth.cardPadding,
                            modifier = Modifier.fillMaxWidth()
                        )
                        FeatureCard(
                            title = "AI Tanpa Transparansi",
                            description = "Penggunaan AI yang tidak dapat dijelaskan (black-box) memicu krisis kepercayaan internal dan eksternal, serta risiko kepatuhan regulasi yang semakin ketat.",
                            icon = Res.drawable.icon_1,
                            iconBgColor = Color(0xFFFEF3C7),
                            iconTintColor = Color(0xFFEF9F27),
                            contentPadding = windowWidth.cardPadding,
                            modifier = Modifier.fillMaxWidth()
                        )
                        FeatureCard(
                            title = "ESG Hanya Beban Administratif",
                            description = "Pelaporan ESG yang terpisah dari operasional bisnis menjadikannya beban birokrasi, bukan kompas strategis yang mendorong nilai jangka panjang.",
                            icon = Res.drawable.icon_2,
                            iconBgColor = Color(0xFFDBEAFE),
                            iconTintColor = Color(0xFF378ADD),
                            contentPadding = windowWidth.cardPadding,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max),
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        FeatureCard(
                            title = "Data Tidak Terintegrasi",
                            description = "Silo data antar departemen dan sistem yang tidak terhubung menghasilkan insight yang fragmentaris dan tidak dapat diandalkan untuk keputusan level eksekutif.",
                            icon = Res.drawable.icon,
                            iconBgColor = Color(0xFFFEE2E2),
                            iconTintColor = Color(0xFFE24B4A),
                            modifier = Modifier.weight(1f).fillMaxHeight()
                        )
                        FeatureCard(
                            title = "AI Tanpa Transparansi",
                            description = "Penggunaan AI yang tidak dapat dijelaskan (black-box) memicu krisis kepercayaan internal dan eksternal, serta risiko kepatuhan regulasi yang semakin ketat.",
                            icon = Res.drawable.icon_1,
                            iconBgColor = Color(0xFFFEF3C7),
                            iconTintColor = Color(0xFFEF9F27),
                            modifier = Modifier.weight(1f).fillMaxHeight()
                        )
                        FeatureCard(
                            title = "ESG Hanya Beban Administratif",
                            description = "Pelaporan ESG yang terpisah dari operasional bisnis menjadikannya beban birokrasi, bukan kompas strategis yang mendorong nilai jangka panjang.",
                            icon = Res.drawable.icon_2,
                            iconBgColor = Color(0xFFDBEAFE),
                            iconTintColor = Color(0xFF378ADD),
                            modifier = Modifier.weight(1f).fillMaxHeight()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FeatureCard(
    title: String,
    description: String,
    icon: DrawableResource,
    iconBgColor: Color,
    iconTintColor: Color,
    modifier: Modifier = Modifier,
    contentPadding: androidx.compose.ui.unit.Dp = 40.dp
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(iconBgColor, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(icon),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    colorFilter = ColorFilter.tint(iconTintColor)
                )
            }
            
            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = getDmSansFontFamily(),
                color = Color(0xFF122329),
                lineHeight = 32.sp
            )
            Text(
                text = description,
                fontSize = 16.sp,
                color = Color(0xFF122329).copy(alpha = 0.6f),
                fontFamily = getDmSansFontFamily(),
                lineHeight = 26.sp
            )
        }
    }
}

@Preview(device = DESKTOP, showBackground = true, backgroundColor = 0xFFF8FAFB)
@Composable
fun FeatureGridPreview() {
    FeatureGrid()
}
