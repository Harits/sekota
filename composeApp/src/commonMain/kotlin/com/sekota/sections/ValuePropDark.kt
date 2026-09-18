package com.sekota.sections

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.sekota.*
import com.sekota.ui.contentHorizontalPadding
import com.sekota.ui.WindowWidth
import com.sekota.ui.cardPadding
import com.sekota.ui.sectionHorizontalPadding
import com.sekota.ui.sectionVerticalPadding
import com.sekota.ui.windowWidthOf
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import sekota.composeapp.generated.resources.*

@Composable
fun ValuePropDark(
    onConsultationClick: () -> Unit = {}
) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        ValuePropDarkContent(windowWidthOf(maxWidth), contentHorizontalPadding(maxWidth), onConsultationClick)
    }
}

@Composable
private fun ValuePropDarkContent(windowWidth: WindowWidth, horizontalPadding: Dp, onConsultationClick: () -> Unit) {
    // The copy column and the two proof cards only fit side by side from Expanded;
    // narrower than that the pair stacks beneath the headline.
    val stacked = windowWidth.isAtMostMedium

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF0B141B))
            .padding(
                vertical = windowWidth.sectionVerticalPadding,
                horizontal = horizontalPadding
            )
    ) {
        val copy = @Composable { modifier: Modifier ->
            Column(modifier = modifier) {
                Text(
                    text = "MENGAPA SEKOTA",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00A99D),
                    fontFamily = getDmSansFontFamily(),
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = if (stacked) "Sekota Bukan Sekadar Vendor,\nBukan Sekadar Konsultan."
                           else "Sekota Bukan\nSekadar Vendor,\nBukan Sekadar\nKonsultan.",
                    fontSize = if (windowWidth.isCompact) 34.sp else 56.sp,
                    lineHeight = if (windowWidth.isCompact) 42.sp else 64.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    fontFamily = getMontserratFontFamily()
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Kami berada di persimpangan unik antara teknologi, konteks lokal, dan strategic foresight.",
                    fontSize = 18.sp,
                    lineHeight = 28.sp,
                    color = Color.Gray,
                    fontFamily = getDmSansFontFamily(),
                    modifier = Modifier.widthIn(max = 500.dp)
                )
                Spacer(modifier = Modifier.height(48.dp))
                Button(
                    onClick = onConsultationClick,
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(),
                    modifier = if (windowWidth.isCompact) Modifier.fillMaxWidth() else Modifier
                ) {
                    Box(
                        modifier = Modifier
                            .then(if (windowWidth.isCompact) Modifier.fillMaxWidth() else Modifier)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(Color(0xFF02B6CF), Color(0xFF60BD65))
                                ),
                                shape = RoundedCornerShape(50)
                            )
                            .padding(horizontal = 24.dp, vertical = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "MULAI KONSULTASI STRATEGIS",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontFamily = getDmSansFontFamily(),
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        val cards = @Composable { modifier: Modifier ->
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                ValuePropCard(
                    title = "Lebih Strategis dari Vendor IT",
                    description = "Memberikan insight yang dapat langsung ditindaklanjuti untuk kebijakan.",
                    icon = Res.drawable.icon_3,
                    contentPadding = windowWidth.cardPadding
                )
                ValuePropCard(
                    title = "Lebih Teknis dari Konsultan",
                    description = "Implementasi data real-time, bukan sekadar slide deck rekomendasi.",
                    icon = Res.drawable.icon_4,
                    contentPadding = windowWidth.cardPadding
                )
            }
        }

        if (stacked) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(48.dp)
            ) {
                copy(Modifier.fillMaxWidth())
                cards(Modifier.fillMaxWidth())
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(if (windowWidth == WindowWidth.Large) 80.dp else 48.dp)
            ) {
                copy(Modifier.weight(1.2f))
                cards(Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun ValuePropCard(
    title: String,
    description: String,
    icon: DrawableResource,
    contentPadding: androidx.compose.ui.unit.Dp = 40.dp
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF111E26), shape = RoundedCornerShape(24.dp))
            .padding(contentPadding)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.Top
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp).padding(top = 4.dp)
            )
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = getDmSansFontFamily()
                )
                Text(
                    text = description,
                    fontSize = 15.sp,
                    lineHeight = 24.sp,
                    color = Color.Gray,
                    fontFamily = getDmSansFontFamily()
                )
            }
        }
    }
}

@Preview(device=DESKTOP)
@Composable
fun ValuePropDarkPreview() {
    ValuePropDark()
}
