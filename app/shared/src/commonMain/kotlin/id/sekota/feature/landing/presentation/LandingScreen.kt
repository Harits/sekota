package id.sekota.feature.landing.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import id.sekota.app.AppState
import id.sekota.app.chrome.SiteFooter
import id.sekota.core.designsystem.LocalReducedMotion
import id.sekota.core.model.BookSlug
import id.sekota.core.model.ProductSlug
import id.sekota.feature.landing.domain.GetLandingContent
import id.sekota.feature.landing.presentation.section.AboutSection
import id.sekota.feature.landing.presentation.section.BooksSection
import id.sekota.feature.landing.presentation.section.FaqSection
import id.sekota.feature.landing.presentation.section.HeroSection
import id.sekota.feature.landing.presentation.section.PartnerSection
import id.sekota.feature.landing.presentation.section.PlatformsSection
import id.sekota.feature.landing.presentation.section.ProcessSection
import id.sekota.feature.landing.presentation.section.ServicesSection
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import org.koin.compose.koinInject

private val sectionKeys = listOf(
    "hero", "tentang", "layanan", "cara-kerja", "platform", "wawasan", "mitra", "faq", "footer",
)

@Composable
fun LandingScreen(
    appState: AppState,
    onProduct: (ProductSlug) -> Unit,
    onBook: (BookSlug) -> Unit,
    onContact: (String?) -> Unit,
) {
    val getContent = koinInject<GetLandingContent>()
    val viewModel = remember { LandingViewModel(getContent) }
    val uiState by viewModel.state.collectAsState()
    val content = viewModel.content
    val language = appState.language
    val listState = rememberLazyListState()
    DisposableEffect(listState) {
        appState.activeListState = listState
        onDispose { if (appState.activeListState === listState) appState.activeListState = null }
    }
    val headerOffset = with(LocalDensity.current) { 80.dp.roundToPx() }
    var processProgress by remember { mutableStateOf(0f) }

    val reduceMotion = LocalReducedMotion.current
    LaunchedEffect(reduceMotion) {
        if (!reduceMotion) {
            while (true) {
                delay(2600)
                viewModel.highlightNextRow()
            }
        }
    }

    // Collect anchor requests instead of keying the effect on the consumed value:
    // consuming sets the state to null and would cancel the scroll coroutine.
    LaunchedEffect(Unit) {
        snapshotFlow { appState.navigator.pendingHomeAnchor }
            .filterNotNull()
            .collectLatest { anchor ->
                val index = sectionKeys.indexOf(anchor)
                if (index >= 0) {
                    listState.animateScrollToItem(
                        index = index,
                        scrollOffset = if (index == 0) 0 else -headerOffset,
                    )
                }
                appState.navigator.consumeAnchor()
            }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo }.collectLatest { info ->
            val firstVisible = info.visibleItemsInfo.firstOrNull()
            appState.chromeSolid = (firstVisible?.index ?: 0) > 0 || (firstVisible?.offset ?: 0) < -12
            // Design scrollspy: a section becomes current once its top passes 40% of the viewport.
            val threshold = info.viewportEndOffset * 0.4f
            val current = info.visibleItemsInfo.lastOrNull {
                it.index < sectionKeys.lastIndex && it.offset < threshold
            }
            appState.activeAnchor = current?.let { sectionKeys.getOrNull(it.index) } ?: "hero"
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo }.collectLatest { info ->
            val item = info.visibleItemsInfo.firstOrNull { it.index == 3 }
            processProgress = when {
                item != null -> {
                    val viewport = info.viewportSize.height.toFloat()
                    ((viewport * 0.85f - item.offset) / (viewport * 0.6f)).coerceIn(0f, 1f)
                }
                listState.firstVisibleItemIndex > 3 -> 1f
                else -> 0f
            }
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxWidth(),
    ) {
        item(key = "hero") {
            HeroSection(
                content = content.hero,
                sectorsTitle = content.sectorsTitle[language],
                sectors = content.sectors,
                language = language,
                highlightedRow = uiState.highlightedRow,
                onViewServices = { appState.navigator.navigateHome("layanan") },
                onContact = { onContact(null) },
                onProduct = onProduct,
                onRowHover = viewModel::highlightRow,
            )
        }
        item(key = "tentang") { AboutSection(content.about, language) }
        item(key = "layanan") {
            ServicesSection(content.services, language, onProduct)
        }
        item(key = "cara-kerja") { ProcessSection(content.process, language, processProgress) }
        item(key = "platform") {
            PlatformsSection(content.platforms, language, onProduct)
        }
        item(key = "wawasan") {
            BooksSection(content.books, language, onBook)
        }
        item(key = "mitra") {
            PartnerSection(content.partner, language) { onContact("mitra-awal") }
        }
        item(key = "faq") {
            FaqSection(
                content = content.faq,
                language = language,
                openFaqId = uiState.openFaqId,
                onToggle = viewModel::toggleFaq,
            )
        }
        item(key = "footer") { SiteFooter(appState) }
    }
}
