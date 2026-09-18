package com.sekota

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sekota.ui.contentHorizontalPadding
import com.sekota.ui.WindowWidth
import com.sekota.ui.sectionHorizontalPadding
import com.sekota.ui.windowWidthOf
import org.jetbrains.compose.resources.painterResource
import sekota.composeapp.generated.resources.Res
import sekota.composeapp.generated.resources.container
import sekota.composeapp.generated.resources.logo_sekota

import androidx.compose.foundation.clickable

@Composable
fun Footer(
    onNavigate: (Screen) -> Unit = {},
    onProductClick: (String) -> Unit = {},
    onKontakClick: () -> Unit = {}
) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        FooterContent(
            windowWidth = windowWidthOf(maxWidth),
            horizontalPadding = contentHorizontalPadding(maxWidth),
            onNavigate = onNavigate,
            onProductClick = onProductClick,
            onKontakClick = onKontakClick
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FooterContent(
    windowWidth: WindowWidth,
    horizontalPadding: Dp,
    onNavigate: (Screen) -> Unit,
    onProductClick: (String) -> Unit,
    onKontakClick: () -> Unit
) {
    val isCompact = windowWidth.isCompact

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(
                horizontal = horizontalPadding,
                vertical = if (isCompact) 48.dp else 64.dp
            )
    ) {
        val branding = @Composable { modifier: Modifier ->
            Column(modifier = modifier) {
                Box(modifier = Modifier.clickable { onNavigate(Screen.Landing) }) {
                    FooterLogo()
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Trusted Intelligence for Sustainable Impact. Memberdayakan keputusan strategis melalui data.",
                    fontSize = 14.sp,
                    fontFamily = getDmSansFontFamily(),
                    color = Color.Gray,
                    lineHeight = 22.sp,
                    modifier = Modifier.widthIn(max = 300.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(Res.drawable.container),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "eBdesk Group Affiliation",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF5A6972),
                            fontFamily = getDmSansFontFamily()
                        )
                        Text(
                            text = "NATIONWIDE PARTNER",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.LightGray,
                            fontFamily = getDmSansFontFamily()
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("LINKEDIN", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.LightGray)
                    Text("TWITTER", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.LightGray)
                }
            }
        }

        val linkColumns = @Composable {
            // Column 2: Produk
            FooterColumn(
                title = "PRODUK",
                items = listOf("VERIDIA", "ASCENDIO", "SOCIARA", "ECOFLOW"),
                onItemClick = { item ->
                    val code = when (item) {
                        "VERIDIA" -> "VRD"
                        "ASCENDIO" -> "ASC"
                        "SOCIARA" -> "SOC"
                        "ECOFLOW" -> "ECO"
                        else -> "VRD"
                    }
                    onProductClick(code)
                }
            )

            // Column 3: Perusahaan
            FooterColumn(
                title = "PERUSAHAAN",
                items = listOf("Tentang Kami", "E-Book", "Merchandise", "Insights", "Kontak"),
                onItemClick = { item ->
                    when (item) {
                        "E-Book" -> onNavigate(Screen.Catalog)
                        "Merchandise" -> onNavigate(Screen.Merchandise)
                        "Kontak" -> onKontakClick()
                        else -> onNavigate(Screen.Landing)
                    }
                }
            )

            // Column 4: Legal
            FooterColumn(
                title = "LEGAL",
                items = listOf("Privasi", "Syarat & Ketentuan"),
                onItemClick = {}
            )
        }

        // Branding block plus three 150dp link columns needs ~750dp. Below Expanded
        // the branding takes a full row and the link columns wrap underneath it.
        if (windowWidth.isAtMostMedium) {
            branding(Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(40.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                linkColumns()
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                branding(Modifier.weight(1.8f).padding(end = 48.dp))
                linkColumns()
            }
        }

        Spacer(modifier = Modifier.height(if (isCompact) 40.dp else 64.dp))
        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
        Spacer(modifier = Modifier.height(if (isCompact) 24.dp else 32.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "© 2025 PT. SEKOTA SINERGI INDONESIA. BAGIAN DARI EBDESK GROUP.",
                fontSize = 12.sp,
                color = Color.Gray,
                fontFamily = getDmSansFontFamily()
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("ENGLISH", fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.width(24.dp))
                Text("BAHASA INDONESIA", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            }
        }
    }
}

@Composable
fun FooterColumn(
    title: String,
    items: List<String>,
    onItemClick: (String) -> Unit = {}
) {
    Column(modifier = Modifier.widthIn(min = 140.dp)) {
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        items.forEach { item ->
            Text(
                text = item,
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .clickable { onItemClick(item) },
                fontFamily = getDmSansFontFamily()
            )
        }
    }
}

@Composable
fun FooterLogo() {
    Image(
        painter = painterResource(Res.drawable.logo_sekota),
        contentDescription = "Sekota Logo",
        modifier = Modifier.height(32.dp)
    )
}

@Preview(device = DESKTOP)
@Composable
fun FooterPreview() {
    Footer()
}
