package com.sekota

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import sekota.composeapp.generated.resources.Res
import sekota.composeapp.generated.resources.*

@Composable
fun getMontserratFontFamily() = FontFamily(
    Font(Res.font.Montserrat_ExtraBold, FontWeight.ExtraBold)
)

@Composable
fun getDmSansFontFamily() = FontFamily(
    Font(Res.font.DMSans_Light, FontWeight.Light),
    Font(Res.font.DMSans_Bold, FontWeight.Bold),
    Font(Res.font.DMSans_Regular, FontWeight.Normal)
)
