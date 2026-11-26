package id.sekota.core.designsystem

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import sekota.app.shared.generated.resources.Res
import sekota.app.shared.generated.resources.ibm_plex_mono_400
import sekota.app.shared.generated.resources.ibm_plex_mono_500
import sekota.app.shared.generated.resources.plus_jakarta_sans_400
import sekota.app.shared.generated.resources.plus_jakarta_sans_500
import sekota.app.shared.generated.resources.plus_jakarta_sans_600
import sekota.app.shared.generated.resources.plus_jakarta_sans_700

/** Type scale from the design: Plus Jakarta Sans for UI, IBM Plex Mono for labels. */
@Immutable
data class SekotaType(
    val hero: TextStyle,
    val headline: TextStyle,
    val h3: TextStyle,
    val h4: TextStyle,
    val body: TextStyle,
    val lead: TextStyle,
    val small: TextStyle,
    val caption: TextStyle,
    val nav: TextStyle,
    val button: TextStyle,
    val monoOverline: TextStyle,
    val monoSmall: TextStyle,
    val number: TextStyle,
    val bigNumber: TextStyle,
)

val LocalSekotaType = staticCompositionLocalOf<SekotaType> {
    error("SekotaType not provided: wrap content in SekotaTheme")
}

val LocalSekotaPalette = staticCompositionLocalOf { SekotaPalette() }

/** True when the platform asks for reduced motion; animations should be skipped. */
val LocalReducedMotion = staticCompositionLocalOf { false }

object SekotaTheme {
    val type: SekotaType
        @Composable get() = LocalSekotaType.current

    val colors: SekotaPalette
        @Composable get() = LocalSekotaPalette.current
}

@Composable
fun sekotaFontFamilies(): Pair<FontFamily, FontFamily> {
    val sans = FontFamily(
        Font(Res.font.plus_jakarta_sans_400, FontWeight.Normal, FontStyle.Normal),
        Font(Res.font.plus_jakarta_sans_500, FontWeight.Medium, FontStyle.Normal),
        Font(Res.font.plus_jakarta_sans_600, FontWeight.SemiBold, FontStyle.Normal),
        Font(Res.font.plus_jakarta_sans_700, FontWeight.Bold, FontStyle.Normal),
    )
    val mono = FontFamily(
        Font(Res.font.ibm_plex_mono_400, FontWeight.Normal, FontStyle.Normal),
        Font(Res.font.ibm_plex_mono_500, FontWeight.Medium, FontStyle.Normal),
    )
    return sans to mono
}

@Composable
fun sekotaType(): SekotaType {
    val (sans, mono) = sekotaFontFamilies()
    return remember(sans, mono) {
        SekotaType(
            hero = TextStyle(
                fontFamily = sans,
                fontWeight = FontWeight.SemiBold,
                fontSize = 56.sp,
                lineHeight = 64.sp,
                letterSpacing = (-1.96).sp,
            ),
            headline = TextStyle(
                fontFamily = sans,
                fontWeight = FontWeight.SemiBold,
                fontSize = 40.sp,
                lineHeight = 48.sp,
                letterSpacing = (-0.8).sp,
            ),
            h3 = TextStyle(
                fontFamily = sans,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                lineHeight = 28.sp,
                letterSpacing = (-0.4).sp,
            ),
            h4 = TextStyle(
                fontFamily = sans,
                fontWeight = FontWeight.SemiBold,
                fontSize = 17.sp,
                lineHeight = 24.sp,
                letterSpacing = (-0.34).sp,
            ),
            body = TextStyle(
                fontFamily = sans,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 25.6.sp,
            ),
            lead = TextStyle(
                fontFamily = sans,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                lineHeight = 30.sp,
            ),
            small = TextStyle(
                fontFamily = sans,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 22.sp,
            ),
            caption = TextStyle(
                fontFamily = sans,
                fontWeight = FontWeight.Normal,
                fontSize = 13.sp,
                lineHeight = 18.sp,
            ),
            nav = TextStyle(
                fontFamily = sans,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 20.sp,
            ),
            button = TextStyle(
                fontFamily = sans,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                lineHeight = 20.sp,
            ),
            monoOverline = TextStyle(
                fontFamily = mono,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                letterSpacing = 1.56.sp,
            ),
            monoSmall = TextStyle(
                fontFamily = mono,
                fontWeight = FontWeight.Normal,
                fontSize = 13.sp,
                lineHeight = 18.sp,
            ),
            number = TextStyle(
                fontFamily = mono,
                fontWeight = FontWeight.Normal,
                fontSize = 13.sp,
                lineHeight = 18.sp,
            ),
            bigNumber = TextStyle(
                fontFamily = sans,
                fontWeight = FontWeight.SemiBold,
                fontSize = 40.sp,
                lineHeight = 48.sp,
                letterSpacing = (-1.2).sp,
            ),
        )
    }
}

@Composable
fun SekotaTheme(content: @Composable () -> Unit) {
    val type = sekotaType()
    val palette = SekotaPalette()
    val materialTypography = remember(type) {
        Typography(
            displayLarge = type.hero,
            displayMedium = type.headline,
            headlineLarge = type.headline,
            headlineMedium = type.headline,
            headlineSmall = type.h3,
            titleLarge = type.h3,
            titleMedium = type.h4,
            titleSmall = type.h4,
            bodyLarge = type.body,
            bodyMedium = type.small,
            bodySmall = type.caption,
            labelLarge = type.button,
            labelMedium = type.monoSmall,
            labelSmall = type.monoOverline,
        )
    }
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = SekotaColors.Cyan,
            onPrimary = SekotaColors.Bg,
            secondary = SekotaColors.Green,
            background = SekotaColors.Bg,
            onBackground = SekotaColors.Ink,
            surface = SekotaColors.Bg,
            onSurface = SekotaColors.Ink,
            outline = SekotaColors.Line,
            error = SekotaColors.Required,
        ),
        typography = materialTypography,
        shapes = Shapes(
            extraSmall = RoundedCornerShape(8.dp),
            small = RoundedCornerShape(10.dp),
            medium = RoundedCornerShape(12.dp),
            large = RoundedCornerShape(16.dp),
            extraLarge = RoundedCornerShape(24.dp),
        ),
    ) {
        CompositionLocalProvider(
            LocalSekotaType provides type,
            LocalSekotaPalette provides palette,
            content = content,
        )
    }
}

object SekotaSpacing {
    val xxs = 4.dp
    val xs = 8.dp
    val sm = 12.dp
    val md = 16.dp
    val lg = 20.dp
    val xl = 24.dp
    val xxl = 32.dp
    val xxxl = 40.dp
    val huge = 56.dp
    val section = 112.dp
    val sectionCompact = 72.dp
    val navHeight = 80.dp
    val maxContentWidth = 1200.dp
    val gutter = 24.dp
    val gutterCompact = 20.dp
}

object SekotaShape {
    val xs = RoundedCornerShape(7.dp)
    val sm = RoundedCornerShape(10.dp)
    val md = RoundedCornerShape(12.dp)
    val lg = RoundedCornerShape(16.dp)
    val xl = RoundedCornerShape(20.dp)
    val xxl = RoundedCornerShape(24.dp)
    val pill = RoundedCornerShape(percent = 50)
}
