package com.sekota.features.companyprofile.presentation.sections

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sekota.features.admin.data.seed.CompanyProfileSeed
import com.sekota.features.admin.domain.model.AdminBook
import com.sekota.features.admin.domain.model.AdminProduct
import com.sekota.features.admin.domain.model.FooterConfig
import com.sekota.features.admin.domain.model.StrategicConversationConfig
import com.sekota.features.admin.domain.model.ValueLoopConfig
import com.sekota.features.companyprofile.domain.model.CompanyProfileContent
import com.sekota.ui.brand.BrandAssets
import com.sekota.ui.brand.BrandBrushes
import com.sekota.ui.brand.BrandColors
import com.sekota.ui.brand.BrandLayout
import com.sekota.utils.decodeImageToBitmap
import org.jetbrains.compose.resources.painterResource
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

/** A full-width page band with the design's vertical rhythm (.band). */
@Composable
internal fun Band(
    modifier: Modifier = Modifier,
    background: Brush? = null,
    color: Color = BrandColors.Background,
    topPadding: Boolean = true,
    content: @Composable BoxWithConstraintsScope.() -> Unit
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .then(if (background != null) Modifier.background(background) else Modifier.background(color))
    ) {
        val pad = BrandLayout.bandPadding(maxWidth)
        Box(Modifier.fillMaxWidth().padding(top = if (topPadding) pad else 0.dp, bottom = pad)) {
            BoxWithConstraints(Modifier.fillMaxWidth()) { content() }
        }
    }
}

/** Platform logo for light or dark surfaces; falls back to the name when no asset matches. */
@Composable
internal fun ProductLogo(product: AdminProduct, onDark: Boolean, modifier: Modifier, textStyle: TextStyle) {
    val resource = BrandAssets.drawable(if (onDark) product.logoDarkAsset else product.logoLightAsset)
    if (resource != null) {
        Image(painterResource(resource), contentDescription = product.name, modifier = modifier)
    } else {
        Text(product.name, style = textStyle, color = if (onDark) Color.White else BrandColors.Ink)
    }
}

@OptIn(ExperimentalEncodingApi::class)
private fun decodeDataUri(raw: String?): ImageBitmap? {
    if (raw == null || !raw.startsWith("data:image")) return null
    return runCatching { decodeImageToBitmap(Base64.decode(raw.substringAfter("base64,"))) }.getOrNull()
}

/**
 * Book cover: CMS upload (data URI) first, then the bundled asset, then a
 * gradient placeholder with the title so the shelf never shows a broken image.
 */
@Composable
internal fun BookCover(book: AdminBook, contentDescription: String, modifier: Modifier, titleStyle: TextStyle) {
    val uploaded = remember(book.coverImage) { decodeDataUri(book.coverImage) }
    val asset = BrandAssets.drawable(book.coverAsset)
    when {
        uploaded != null -> Image(uploaded, contentDescription, modifier, contentScale = ContentScale.Crop)
        asset != null -> Image(painterResource(asset), contentDescription, modifier, contentScale = ContentScale.Crop)
        else -> Box(modifier.background(BrandBrushes.DarkBand), contentAlignment = Alignment.Center) {
            Text(book.title, style = titleStyle, color = Color.White, textAlign = TextAlign.Center, modifier = Modifier.padding(12.dp))
        }
    }
}

/** Seed-backed sample content for previews and screenshot tests. */
object CompanyProfilePreviewData {
    val content = CompanyProfileContent(
        about = CompanyProfileSeed.about,
        services = CompanyProfileSeed.services,
        process = ValueLoopConfig(),
        products = CompanyProfileSeed.products,
        books = CompanyProfileSeed.books,
        partnerCta = CompanyProfileSeed.partnerCta,
        faqs = CompanyProfileSeed.faqs,
        footer = FooterConfig(),
        contactEmail = StrategicConversationConfig().email
    )
}

@Composable
internal fun FillBox(modifier: Modifier = Modifier) = Box(modifier.fillMaxSize())
