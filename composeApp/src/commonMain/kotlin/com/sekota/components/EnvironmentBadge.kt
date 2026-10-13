package com.sekota.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sekota.features.admin.domain.model.AppEnvironment
import com.sekota.getDmSansFontFamily
import com.sekota.getMontserratFontFamily

@Composable
fun EnvironmentBadge(
    environment: AppEnvironment,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val (bgColor, textColor, borderColor, dotColor) = when (environment) {
        AppEnvironment.PRODUCTION -> Quadruple(
            Color(0xFFFFEBEE), // Light red bg
            Color(0xFFC62828), // Deep red text
            Color(0xFFFFCDD2), // Red border
            Color(0xFFD32F2F)  // Pulse dot red
        )
        AppEnvironment.STAGING -> Quadruple(
            Color(0xFFFFF8E1), // Light amber bg
            Color(0xFFF57F17), // Deep amber text
            Color(0xFFFFE082), // Amber border
            Color(0xFFFFA000)  // Pulse dot amber
        )
        AppEnvironment.DEVELOPMENT -> Quadruple(
            Color(0xFFE0F7FA), // Light teal bg
            Color(0xFF00838F), // Deep teal text
            Color(0xFFB2EBF2), // Teal border
            Color(0xFF00ACC1)  // Pulse dot teal
        )
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(100.dp),
        border = BorderStroke(1.dp, borderColor),
        modifier = modifier.then(
            if (onClick != null) Modifier.clickable { onClick() } else Modifier
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(dotColor)
            )
            Text(
                text = environment.displayName,
                fontFamily = getDmSansFontFamily(),
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                letterSpacing = 0.5.sp,
                color = textColor
            )
            if (environment.isProduction) {
                Text(
                    text = "• LIVE GUARD",
                    fontFamily = getDmSansFontFamily(),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 10.sp,
                    color = textColor.copy(alpha = 0.85f)
                )
            }
        }
    }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun EnvironmentBadgeDevelopmentPreview() {
    MaterialTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            EnvironmentBadge(environment = AppEnvironment.DEVELOPMENT)
        }
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun EnvironmentBadgeStagingPreview() {
    MaterialTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            EnvironmentBadge(environment = AppEnvironment.STAGING)
        }
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun EnvironmentBadgeProductionPreview() {
    MaterialTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            EnvironmentBadge(environment = AppEnvironment.PRODUCTION)
        }
    }
}
