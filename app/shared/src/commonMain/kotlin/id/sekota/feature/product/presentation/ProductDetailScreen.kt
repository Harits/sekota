package id.sekota.feature.product.presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.sekota.app.AppState
import id.sekota.app.chrome.SiteFooter
import id.sekota.core.content.SiteContent
import id.sekota.core.designsystem.BrowserFrame
import id.sekota.core.designsystem.ContentWrap
import id.sekota.core.designsystem.CtaBanner
import id.sekota.core.designsystem.IconTile
import id.sekota.core.designsystem.LocalReducedMotion
import id.sekota.core.designsystem.Overline
import id.sekota.core.designsystem.Reveal
import id.sekota.core.designsystem.SekotaButton
import id.sekota.core.designsystem.SekotaCard
import id.sekota.core.designsystem.SekotaChip
import id.sekota.core.designsystem.SekotaColors
import id.sekota.core.designsystem.SekotaIcons
import id.sekota.core.designsystem.SekotaShape
import id.sekota.core.designsystem.SekotaTheme
import id.sekota.core.designsystem.SectionBand
import id.sekota.core.designsystem.SectionHeader
import id.sekota.core.designsystem.VSpace
import id.sekota.core.designsystem.rememberCardHover
import id.sekota.core.model.Language
import id.sekota.core.model.ProductSlug
import id.sekota.core.ui.LocalWindowSize
import id.sekota.core.ui.ProductLogo
import id.sekota.core.ui.WindowSize
import id.sekota.core.ui.screens
import id.sekota.core.ui.vector
import id.sekota.feature.product.domain.GetProductDetail
import id.sekota.feature.product.domain.ProductFlowStep
import id.sekota.feature.product.domain.ProductProblem
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import kotlinx.coroutines.launch

private val productSectionKeys = listOf(
    "hero", "masalah", "fitur", "tampilan", "alur", "pengguna", "platform-lain", "cta", "footer",
)

@Composable
fun ProductDetailScreen(
    appState: AppState,
    slug: ProductSlug,
    onProduct: (ProductSlug) -> Unit,
    onContact: (String?) -> Unit,
    onHomeSection: (String) -> Unit,
) {
    val getProduct = koinInject<GetProductDetail>()
    val detail = remember(slug) { getProduct(slug) } ?: return
    val viewModel = remember(detail) { ProductViewModel(detail) }
    val reducedMotion = LocalReducedMotion.current
    LaunchedEffect(viewModel, reducedMotion) {
        if (reducedMotion || detail.screens.size <= 1) return@LaunchedEffect
        while (viewModel.state.value.autoAdvance) {
            kotlinx.coroutines.delay(6000)
            viewModel.advanceAuto()
        }
    }
    val uiState by viewModel.state.collectAsState()
    val language = appState.language
    val window = LocalWindowSize.current
    val listState = rememberLazyListState()
    DisposableEffect(listState) {
        appState.activeListState = listState
        onDispose { if (appState.activeListState === listState) appState.activeListState = null }
    }
    val headerOffset = with(LocalDensity.current) { 80.dp.roundToPx() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(slug) { listState.scrollToItem(0) }

    LaunchedEffect(listState) {
        androidx.compose.runtime.snapshotFlow { listState.firstVisibleItemIndex }
            .collect { appState.chromeSolid = it > 0 }
    }

    fun scrollTo(key: String) {
        val index = productSectionKeys.indexOf(key)
        if (index >= 0) scope.launch { listState.animateScrollToItem(index, -headerOffset) }
    }

    val shots = detail.slug.screens()

    LazyColumn(state = listState, modifier = Modifier.fillMaxWidth()) {
        item(key = "hero") {
            ProductHero(
                detail = detail,
                language = language,
                window = window,
                painter = painterResource(shots.first()),
                onBack = { onHomeSection("platform") },
                onDemo = { onContact("demo-${detail.slug.path}") },
                onFeatures = { scrollTo("fitur") },
            )
        }
        item(key = "masalah") {
            SectionBand(subtle = true) {
                SectionHeader(
                    overline = detail.problemsOverline[language],
                    title = detail.problemsTitle[language],
                )
                VSpace(if (window == WindowSize.Compact) 32.dp else 56.dp)
                Grid(columns = when (window) {
                    WindowSize.Expanded -> 3
                    WindowSize.Wide, WindowSize.Medium -> 2
                    WindowSize.Compact -> 1
                }) { perRow ->
                    detail.problems.chunked(perRow).forEach { row ->
                        Row(horizontalArrangement = Arrangement.spacedBy(24.dp), modifier = Modifier.fillMaxWidth()) {
                            row.forEach { problem ->
                                Box(Modifier.weight(1f)) { ProblemCard(problem, language) }
                            }
                            repeat(perRow - row.size) { Spacer(Modifier.weight(1f)) }
                        }
                    }
                }
            }
        }
        item(key = "fitur") {
            SectionBand {
                SectionHeader(
                    overline = detail.featuresOverline[language],
                    title = detail.featuresTitle[language],
                )
                VSpace(if (window == WindowSize.Compact) 32.dp else 56.dp)
                Grid(columns = when (window) {
                    WindowSize.Expanded -> 3
                    WindowSize.Wide, WindowSize.Medium -> 2
                    WindowSize.Compact -> 1
                }) { perRow ->
                    detail.features.chunked(perRow).forEach { row ->
                        Row(horizontalArrangement = Arrangement.spacedBy(24.dp), modifier = Modifier.fillMaxWidth()) {
                            row.forEach { feature ->
                                Box(Modifier.weight(1f)) {
                                    FeatureCard(feature.icon.vector(), feature.title[language], feature.text[language])
                                }
                            }
                            repeat(perRow - row.size) { Spacer(Modifier.weight(1f)) }
                        }
                    }
                }
            }
        }
        item(key = "tampilan") {
            SectionBand(subtle = true) {
                SectionHeader(
                    overline = detail.screensOverline[language],
                    title = detail.screensTitle[language],
                )
                VSpace(if (window == WindowSize.Compact) 32.dp else 56.dp)
                Gallery(detail, language, window, uiState.selectedScreen, shots, viewModel::selectScreen)
                VSpace(20.dp)
                Text(
                    text = detail.screensNote[language],
                    style = SekotaTheme.type.caption,
                    color = SekotaColors.Muted,
                )
            }
        }
        item(key = "alur") {
            SectionBand {
                SectionHeader(
                    overline = detail.flowOverline[language],
                    title = detail.flowTitle[language],
                )
                VSpace(if (window == WindowSize.Compact) 32.dp else 56.dp)
                Grid(columns = when (window) {
                    WindowSize.Expanded -> 4
                    WindowSize.Wide, WindowSize.Medium -> 2
                    WindowSize.Compact -> 1
                }) { perRow ->
                    detail.flow.chunked(perRow).forEach { row ->
                        val connected = perRow == 4
                        Row(
                            horizontalArrangement = if (connected) Arrangement.Start else Arrangement.spacedBy(24.dp),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            row.forEachIndexed { index, step ->
                                Box(Modifier.weight(1f)) { FlowCard(step, language) }
                                if (connected && index != row.lastIndex) {
                                    Box(
                                        Modifier
                                            .offset(y = 47.dp)
                                            .width(24.dp)
                                            .height(2.dp)
                                            .background(SekotaColors.BrandGradient),
                                    )
                                }
                            }
                            repeat(perRow - row.size) { Spacer(Modifier.weight(1f)) }
                        }
                    }
                }
            }
        }
        item(key = "pengguna") {
            SectionBand(subtle = true) {
                val stacked = window == WindowSize.Compact || window == WindowSize.Medium
                if (stacked) {
                    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                        AudienceBox(detail, language)
                        RelatedBox(detail, language) { onHomeSection("layanan") }
                    }
                } else {
                    Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                        Box(Modifier.weight(1f)) { AudienceBox(detail, language) }
                        Box(Modifier.weight(1f)) { RelatedBox(detail, language) { onHomeSection("layanan") } }
                    }
                }
            }
        }
        item(key = "platform-lain") {
            SectionBand(dark = true) {
                Overline(detail.otherPlatformsOverline[language], dark = true)
                VSpace(16.dp)
                Text(
                    text = detail.otherPlatformsTitle[language],
                    style = SekotaTheme.type.headline,
                    color = Color.White,
                )
                VSpace(40.dp)
                val others = ProductSlug.entries.filter { it != detail.slug }
                val stacked = window == WindowSize.Compact
                if (stacked) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        others.forEach { other -> OtherPlatformCard(other, language) { onProduct(other) } }
                    }
                } else {
                    Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                        others.forEach { other ->
                            Box(Modifier.weight(1f)) { OtherPlatformCard(other, language) { onProduct(other) } }
                        }
                    }
                }
            }
        }
        item(key = "cta") {
            SectionBand {
                CtaBanner(
                    title = detail.ctaTitle[language],
                    text = detail.ctaText[language],
                    buttonLabel = detail.ctaButton[language],
                    onClick = { onContact("demo-${detail.slug.path}") },
                )
            }
        }
        item(key = "footer") { SiteFooter(appState) }
    }
}

@Composable
private fun Grid(columns: Int, content: @Composable (Int) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) { content(columns) }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ProductHero(
    detail: id.sekota.feature.product.domain.ProductDetail,
    language: Language,
    window: WindowSize,
    painter: androidx.compose.ui.graphics.painter.Painter,
    onBack: () -> Unit,
    onDemo: () -> Unit,
    onFeatures: () -> Unit,
) {
    val compact = window == WindowSize.Compact
    val stacked = window != WindowSize.Expanded
    Box(
        Modifier
            .fillMaxWidth()
            .background(SekotaColors.Bg),
    ) {
        ContentWrap {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(top = if (compact) 112.dp else 150.dp, bottom = if (compact) 56.dp else 96.dp),
            ) {
                val textColumn: @Composable () -> Unit = {
                    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = SekotaIcons.ChevronLeft,
                                contentDescription = null,
                                tint = SekotaColors.Ink2,
                                modifier = Modifier.size(16.dp),
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = detail.breadcrumb[language],
                                style = SekotaTheme.type.small,
                                color = SekotaColors.Ink2,
                                modifier = Modifier.clickable(onClick = onBack),
                            )
                            Text(
                                text = " / ${detail.name}",
                                style = SekotaTheme.type.small,
                                color = SekotaColors.Ink2,
                            )
                        }
                        ProductLogo(slug = detail.slug, height = 40.dp)
                        Text(
                            text = "${if (language == Language.ID) "Layanan" else "Service"}: ${detail.serviceName[language]}",
                            style = SekotaTheme.type.caption.copy(fontWeight = FontWeight.SemiBold),
                            color = SekotaColors.BrandInk,
                            modifier = Modifier
                                .clip(SekotaShape.pill)
                                .background(SekotaColors.BrandGradientSoft)
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                        )
                        Text(
                            text = detail.headline[language],
                            style = when (window) {
                                WindowSize.Compact -> SekotaTheme.type.headline.copy(fontSize = 32.sp, lineHeight = 38.sp)
                                WindowSize.Medium -> SekotaTheme.type.headline.copy(fontSize = 32.sp, lineHeight = 38.sp)
                                WindowSize.Wide -> SekotaTheme.type.headline.copy(fontSize = 34.sp, lineHeight = 40.sp)
                                WindowSize.Expanded -> SekotaTheme.type.headline.copy(fontSize = 46.sp, lineHeight = 52.sp)
                            },
                            color = SekotaColors.Ink,
                            modifier = Modifier.semantics { heading() },
                        )
                        Text(
                            text = detail.lead[language],
                            style = SekotaTheme.type.lead,
                            color = SekotaColors.Ink2,
                            modifier = Modifier.widthIn(max = 560.dp),
                        )
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            SekotaButton(text = detail.demoCta[language], onClick = onDemo)
                            SekotaButton(
                                text = detail.featuresCta[language],
                                onClick = onFeatures,
                                variant = id.sekota.core.designsystem.ButtonVariant.Secondary,
                                trailingIcon = null,
                            )
                        }
                    }
                }
                if (stacked) {
                    textColumn()
                    VSpace(40.dp)
                    BrowserFrame(
                        url = "${detail.slug.path}.sekota.id",
                        painter = painter,
                        contentDescription = detail.name,
                        modifier = Modifier.fillMaxWidth(),
                    )
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(56.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(Modifier.weight(1f)) { textColumn() }
                        Box(Modifier.weight(1.15f)) {
                            BrowserFrame(
                                url = "${detail.slug.path}.sekota.id",
                                painter = painter,
                                contentDescription = detail.name,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProblemCard(problem: ProductProblem, language: Language) {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(SekotaShape.xl)
            .background(Color.White)
            .border(1.dp, SekotaColors.Line, SekotaShape.xl)
            .padding(28.dp),
    ) {
        Text(problem.number, style = SekotaTheme.type.number, color = SekotaColors.BrandInk)
        VSpace(16.dp)
        Text(problem.title[language], style = SekotaTheme.type.h4, color = SekotaColors.Ink)
        VSpace(8.dp)
        Text(problem.text[language], style = SekotaTheme.type.small, color = SekotaColors.Ink2)
    }
}

@Composable
private fun FeatureCard(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, text: String) {
    val (interaction, hovered) = rememberCardHover()
    val reducedMotion = LocalReducedMotion.current
    val lift by androidx.compose.animation.core.animateDpAsState(
        targetValue = if (hovered && !reducedMotion) 4.dp else 0.dp,
        animationSpec = androidx.compose.animation.core.tween(400),
        label = "featureCardLift",
    )
    val border by animateColorAsState(
        targetValue = if (hovered) SekotaColors.Cyan.copy(alpha = 0.45f) else SekotaColors.Line,
        animationSpec = androidx.compose.animation.core.tween(400),
        label = "featureCardBorder",
    )
    val tile by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (hovered && !reducedMotion) 1f else 0f,
        animationSpec = androidx.compose.animation.core.tween(500),
        label = "featureCardTile",
    )
    Box(
        Modifier
            .fillMaxWidth()
            .graphicsLayer { translationY = -lift.toPx() }
            .clip(SekotaShape.xl)
            .background(Color.White)
            .border(
                width = 1.dp,
                color = border,
                shape = SekotaShape.xl,
            )
            .hoverable(interaction),
    ) {
        Column(Modifier.padding(28.dp)) {
            Box(
                Modifier
                    .size(44.dp)
                    .graphicsLayer {
                        rotationZ = -8f * tile
                        scaleX = 1f + 0.06f * tile
                        scaleY = 1f + 0.06f * tile
                    },
            ) {
                IconTile(icon, size = 44.dp, iconSize = 22.dp)
            }
            VSpace(16.dp)
            Text(title, style = SekotaTheme.type.h3.copy(fontSize = 18.sp), color = SekotaColors.Ink)
            VSpace(8.dp)
            Text(text, style = SekotaTheme.type.small, color = SekotaColors.Ink2)
        }
    }
}

@Composable
private fun Gallery(
    detail: id.sekota.feature.product.domain.ProductDetail,
    language: Language,
    window: WindowSize,
    selected: Int,
    shots: List<org.jetbrains.compose.resources.DrawableResource>,
    onSelect: (Int) -> Unit,
) {
    val stacked = window != WindowSize.Expanded
    val tabs: @Composable () -> Unit = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            detail.screens.forEachIndexed { index, screen ->
                val active = index == selected
                val (interaction, hovered) = rememberCardHover()
                Column(
                    Modifier
                        .fillMaxWidth()
                        .then(
                            if (active) {
                                Modifier.shadow(
                                    elevation = 8.dp,
                                    shape = SekotaShape.lg,
                                    ambientColor = SekotaColors.Cyan.copy(alpha = 0.25f),
                                    spotColor = SekotaColors.Cyan.copy(alpha = 0.25f),
                                )
                            } else {
                                Modifier
                            },
                        )
                        .clip(SekotaShape.lg)
                        .background(if (active || hovered) Color.White else Color.Transparent)
                        .border(
                            1.dp,
                            if (active) SekotaColors.Cyan.copy(alpha = 0.45f) else Color.Transparent,
                            SekotaShape.lg,
                        )
                        .hoverable(interaction)
                        .clickable(
                            interactionSource = interaction,
                            indication = null,
                            onClick = { onSelect(index) },
                        )
                        .padding(horizontal = 20.dp, vertical = 18.dp),
                ) {
                    Text(
                        text = screen.title[language],
                        style = SekotaTheme.type.body.copy(
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            fontWeight = FontWeight.SemiBold,
                        ),
                        color = SekotaColors.Ink,
                    )
                    VSpace(4.dp)
                    Text(
                        text = screen.description[language],
                        style = SekotaTheme.type.small.copy(fontSize = 14.sp, lineHeight = 22.sp),
                        color = SekotaColors.Ink2,
                    )
                }
            }
        }
    }
    if (stacked) {
        Column {
            tabs()
            VSpace(24.dp)
            BrowserFrame(
                url = detail.screens[selected].title[language],
                painter = painterResource(shots[selected]),
                contentDescription = detail.screens[selected].title[language],
                modifier = Modifier.fillMaxWidth(),
                aspectRatio = null,
                contentScale = ContentScale.Fit,
            )
        }
    } else {
        Row(horizontalArrangement = Arrangement.spacedBy(40.dp)) {
            Box(Modifier.widthIn(max = 320.dp)) { tabs() }
            Box(Modifier.weight(1f)) {
                BrowserFrame(
                    url = detail.screens[selected].title[language],
                    painter = painterResource(shots[selected]),
                    contentDescription = detail.screens[selected].title[language],
                    modifier = Modifier.fillMaxWidth(),
                    aspectRatio = null,
                    contentScale = ContentScale.Fit,
                )
            }
        }
    }
}

@Composable
private fun FlowCard(step: ProductFlowStep, language: Language) {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(SekotaShape.xl)
            .background(Color.White)
            .border(1.dp, SekotaColors.Line, SekotaShape.xl)
            .padding(24.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconTile(step.icon.vector(), size = 44.dp, iconSize = 22.dp)
            Text(step.number, style = SekotaTheme.type.number, color = SekotaColors.BrandInk)
        }
        VSpace(14.dp)
        Text(step.title[language], style = SekotaTheme.type.h3.copy(fontSize = 18.sp), color = SekotaColors.Ink)
        VSpace(8.dp)
        Text(step.text[language], style = SekotaTheme.type.small, color = SekotaColors.Ink2)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AudienceBox(detail: id.sekota.feature.product.domain.ProductDetail, language: Language) {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(SekotaShape.xl)
            .background(Color.White)
            .border(1.dp, SekotaColors.Line, SekotaShape.xl)
            .padding(32.dp),
    ) {
        Overline(detail.audienceOverline[language])
        VSpace(12.dp)
        Text(
            text = detail.audienceTitle[language],
            style = SekotaTheme.type.h3,
            color = SekotaColors.Ink,
        )
        VSpace(20.dp)
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            detail.audience.forEach { chip -> SekotaChip(chip[language]) }
        }
    }
}

@Composable
private fun RelatedBox(
    detail: id.sekota.feature.product.domain.ProductDetail,
    language: Language,
    onAllServices: () -> Unit,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(SekotaShape.xl)
            .background(Color.White)
            .border(1.dp, SekotaColors.Line, SekotaShape.xl)
            .padding(32.dp),
    ) {
        Overline(detail.relatedOverline[language])
        VSpace(12.dp)
        Text(detail.relatedTitle[language], style = SekotaTheme.type.h3, color = SekotaColors.Ink)
        VSpace(12.dp)
        Text(
            text = detail.relatedText[language],
            style = SekotaTheme.type.body.copy(fontSize = 15.sp, lineHeight = 24.sp),
            color = SekotaColors.Ink2,
        )
        VSpace(20.dp)
        val (interaction, hovered) = rememberCardHover()
        val reducedMotion = LocalReducedMotion.current
        val arrowOffset by androidx.compose.animation.core.animateDpAsState(
            targetValue = if (hovered && !reducedMotion) 3.dp else 0.dp,
            animationSpec = androidx.compose.animation.core.tween(300),
            label = "arrowOffset",
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.hoverable(interaction).clickable(
                interactionSource = interaction,
                indication = null,
                onClick = onAllServices,
            ),
        ) {
            Text(
                text = if (language == Language.ID) "Lihat semua layanan" else "View all services",
                style = SekotaTheme.type.caption.copy(fontWeight = FontWeight.SemiBold),
                color = SekotaColors.BrandInk,
            )
            Spacer(Modifier.width(6.dp))
            Icon(
                imageVector = SekotaIcons.ArrowRight,
                contentDescription = null,
                tint = SekotaColors.BrandInk,
                modifier = Modifier.size(16.dp).graphicsLayer {
                    translationX = arrowOffset.toPx()
                },
            )
        }
    }
}

@Composable
private fun OtherPlatformCard(slug: ProductSlug, language: Language, onClick: () -> Unit) {
    val (interaction, hovered) = rememberCardHover()
    val reducedMotion = LocalReducedMotion.current
    val lift by androidx.compose.animation.core.animateDpAsState(
        targetValue = if (hovered && !reducedMotion) 4.dp else 0.dp,
        animationSpec = androidx.compose.animation.core.tween(400),
        label = "otherPlatformCardLift",
    )
    val border by animateColorAsState(
        targetValue = if (hovered && !reducedMotion) SekotaColors.Cyan.copy(alpha = 0.5f) else Color.White.copy(alpha = 0.1f),
        animationSpec = androidx.compose.animation.core.tween(400),
        label = "otherPlatformCardBorder",
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                translationY = -lift.toPx()
            }
            .clip(SekotaShape.xl)
            .background(Color.White.copy(alpha = 0.05f))
            .border(1.dp, border, SekotaShape.xl)
            .hoverable(interaction)
            .clickable(
                interactionSource = interaction,
                indication = null,
                onClick = onClick,
            )
            .padding(24.dp),
    ) {
        ProductLogo(slug = slug, dark = true, height = 28.dp)
        VSpace(14.dp)
        Text(
            text = SiteContent.platformServices.getValue(slug)[language],
            style = SekotaTheme.type.small,
            color = SekotaColors.OnDarkMuted,
        )
        VSpace(12.dp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = if (language == Language.ID) "Pelajari" else "Learn more",
                style = SekotaTheme.type.small.copy(fontWeight = FontWeight.Medium),
                color = Color.White,
            )
            Spacer(Modifier.width(6.dp))
            Icon(
                imageVector = SekotaIcons.ArrowUpRight,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp),
            )
        }
    }
}
