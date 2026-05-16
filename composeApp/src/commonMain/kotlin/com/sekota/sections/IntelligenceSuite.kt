package com.sekota.sections

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import sekota.composeapp.generated.resources.Res
import sekota.composeapp.generated.resources.*

@Composable
fun IntelligenceSuite() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 100.dp, horizontal = 48.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
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
                    fontSize = 40.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = getMontserratFontFamily()
                )
            }
            Text(
                text = "Dirancang untuk saling terhubung dalam satu ekosistem data yang kohesif.",
                fontSize = 16.sp,
                color = Color.Gray,
                fontFamily = getDmSansFontFamily(),
                modifier = Modifier.width(350.dp).padding(bottom = 8.dp),
                textAlign = TextAlign.End
            )
        }
        Spacer(modifier = Modifier.height(64.dp))

        Column(verticalArrangement = Arrangement.spacedBy(32.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                SuiteCard(
                    category = "BI DASHBOARD",
                    title = "VERIDIA",
                    description = "Platform Intelligence Dashboard (BI) tingkat lanjut yang mengagregasi data dari berbagai sumber untuk memberikan visualisasi strategis bagi pengambil keputusan.",
                    features = listOf(
                        "Custom Strategic Dashboards",
                        "Predictive Trend Analytics",
                        "Cross-Department Data Integration"
                    ),
                    logo = Res.drawable.icon_veridia,
                    accentColor = Color(0xFF00BFA5),
                    modifier = Modifier.weight(1f)
                )
                SuiteCard(
                    category = "COMMUNITY",
                    title = "ASCENDIO",
                    description = "Platform manajemen komunitas dan pengembangan kapasitas yang fokus pada penilaian kompetensi dan pemantauan aktivitas lapangan secara digital.",
                    features = listOf(
                        "Capacity & Competency Assessment",
                        "Community Engagement Tracking",
                        "Field Activity Reporting"
                    ),
                    logo = Res.drawable.icon_acscendio,
                    accentColor = Color(0xFF1976D2),
                    modifier = Modifier.weight(1f)
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                SuiteCard(
                    category = "SOCIAL",
                    title = "SOCIARA",
                    description = "Platform manajemen dampak sosial untuk mengelola program CSR dan memantau status 'Social License to Operate' (SLO) perusahaan.",
                    features = listOf(
                        "Social License to Operate (SLO) Monitoring",
                        "CSR Impact Measurement & Attribution",
                        "Stakeholder Perception Mapping"
                    ),
                    logo = Res.drawable.icon_sociara,
                    accentColor = Color(0xFF8BC34A),
                    modifier = Modifier.weight(1f)
                )
                SuiteCard(
                    category = "SUSTAINABILITY",
                    title = "ECOFLOW",
                    description = "Solusi Intelligence untuk ESG (Environmental, Social, and Governance) yang mengotomatisasi pengukuran jejak karbon dan kesiapan audit kepatuhan.",
                    features = listOf(
                        "Automated ESG Audit Readiness",
                        "Carbon Footprint Calculator (MRV)",
                        "Compliance Status Monitoring"
                    ),
                    logo = Res.drawable.icon_ecoflow,
                    accentColor = Color(0xFF4CAF50),
                    modifier = Modifier.weight(1f)
                )
            }
        }
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
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(480.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
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
                        fontWeight = FontWeight.Bold,
                        fontFamily = getMontserratFontFamily()
                    )
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
