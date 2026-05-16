package com.sekota

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import sekota.composeapp.generated.resources.Res
import sekota.composeapp.generated.resources.container
import sekota.composeapp.generated.resources.logo_sekota

@Composable
fun Footer() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 48.dp, vertical = 64.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Column 1: Branding
            Column(modifier = Modifier.weight(1.8f).padding(end = 48.dp)) {
                FooterLogo()
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Trusted Intelligence for Sustainable Impact. Memberdayakan keputusan strategis melalui data.",
                    fontSize = 14.sp,
                    fontFamily = getDmSansFontFamily(),
                    color = Color.Gray,
                    lineHeight = 22.sp,
                    modifier = Modifier.width(300.dp)
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

            // Column 2: Produk
            FooterColumn(
                title = "PRODUK",
                items = listOf("VERIDIA", "ASCENDIO", "SOCIARA", "ECOFLOW")
            )

            // Column 3: Perusahaan
            FooterColumn(
                title = "PERUSAHAAN",
                items = listOf("Tentang Kami", "E-Book", "Merchandise", "Insights", "Kontak")
            )

            // Column 4: Legal
            FooterColumn(
                title = "LEGAL",
                items = listOf("Privasi", "Syarat & Ketentuan")
            )
        }

        Spacer(modifier = Modifier.height(64.dp))
        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
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
fun FooterColumn(title: String, items: List<String>) {
    Column(modifier = Modifier.width(150.dp)) {
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
                modifier = Modifier.padding(bottom = 20.dp),
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
