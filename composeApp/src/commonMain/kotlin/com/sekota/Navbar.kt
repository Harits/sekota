package com.sekota

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sekota.features.navigation.domain.model.LandingAnchor
import com.sekota.features.navigation.domain.model.NavigationItem
import com.sekota.localization.AppLocale
import com.sekota.ui.brand.*
import org.jetbrains.compose.resources.painterResource

/** Navbar height: 80dp on wide layouts, 64dp once links collapse into the menu. */
fun navbarHeightFor(width: Dp): Dp = if (BrandLayout.isStacked(width)) 64.dp else BrandLayout.NavHeight

/**
 * Company profile navbar (WEB-07-03-02 / #22669).
 *
 * Transparent over the hero; once the page scrolls (or on any other page) it turns
 * into a translucent white bar with a hairline. Links are the CMS navigation items;
 * the ID/EN toggle switches language on the same page. Below 1100dp the links move
 * into a drop-down menu.
 */
@Composable
fun Navbar(
    items: List<NavigationItem>,
    locale: AppLocale,
    activeAnchor: LandingAnchor?,
    solid: Boolean,
    onRoute: (String) -> Unit,
    onLocaleChange: (AppLocale) -> Unit,
    onContactClick: () -> Unit,
    modifier: Modifier = Modifier,
    initiallyOpen: Boolean = false
) {
    val visibleItems = remember(items) { items.filter { it.isVisible }.sortedBy { it.order } }
    var menuOpen by remember { mutableStateOf(initiallyOpen) }
    val showSolid = solid || menuOpen
    val background by animateColorAsState(if (showSolid) Color.White.copy(alpha = if (menuOpen) 1f else 0.9f) else Color.Transparent, tween(400), label = "navBg")
    val hairline by animateColorAsState(if (showSolid) BrandColors.Line2 else Color.Transparent, tween(400), label = "navLine")
    val type = brandType()

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val wide = !BrandLayout.isStacked(maxWidth)
        val height = navbarHeightFor(maxWidth)
        Column(
            Modifier
                .fillMaxWidth()
                .background(background)
                .drawBehind { drawLine(hairline, Offset(0f, size.height - 1f), Offset(size.width, size.height - 1f), 1f) }
        ) {
            BrandContainer {
                Row(
                    modifier = Modifier.fillMaxWidth().height(height),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Image(
                        painter = painterResource(BrandAssets.SekotaLogoLight),
                        contentDescription = "Sekota Sinergi Indonesia",
                        modifier = Modifier
                            .height(if (wide) 36.dp else 30.dp)
                            .clickable(role = Role.Button) { menuOpen = false; onRoute("/") }
                            .pointerHoverIcon(PointerIcon.Hand)
                    )
                    Spacer(Modifier.weight(1f))
                    if (wide) {
                        Row(horizontalArrangement = Arrangement.spacedBy(36.dp), verticalAlignment = Alignment.CenterVertically) {
                            visibleItems.forEach { item ->
                                NavLink(
                                    label = item.labelFor(locale),
                                    active = LandingAnchor.fromId(item.route) == activeAnchor && activeAnchor != null,
                                    onClick = { onRoute(item.route) }
                                )
                            }
                        }
                        Spacer(Modifier.width(12.dp))
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        LanguageToggle(selected = locale, onSelect = onLocaleChange)
                        if (wide) {
                            BrandButton(
                                text = if (locale == AppLocale.EN) "Contact Us" else "Hubungi Kami",
                                onClick = onContactClick,
                                small = true
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(BrandShapes.Toggle)
                                    .clickable(role = Role.Button) { menuOpen = !menuOpen }
                                    .pointerHoverIcon(PointerIcon.Hand)
                                    .semantics { contentDescription = if (menuOpen) "Tutup menu" else "Buka menu" },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(if (menuOpen) BrandIcons.Close else BrandIcons.Menu, contentDescription = null, tint = BrandColors.Ink, modifier = Modifier.size(22.dp))
                            }
                        }
                    }
                }
            }
            AnimatedVisibility(
                visible = !wide && menuOpen,
                enter = expandVertically(tween(300)) + fadeIn(tween(200)),
                exit = shrinkVertically(tween(250)) + fadeOut(tween(150))
            ) {
                BrandContainer(Modifier.shadow(12.dp).background(Color.White).padding(top = 8.dp, bottom = 24.dp)) {
                    visibleItems.forEach { item ->
                        val active = LandingAnchor.fromId(item.route) == activeAnchor && activeAnchor != null
                        Text(
                            text = item.labelFor(locale),
                            style = type.body.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Medium),
                            color = if (active) BrandColors.Ink else BrandColors.Ink2,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(role = Role.Button) { menuOpen = false; onRoute(item.route) }
                                .semantics { selected = active }
                                .padding(vertical = 12.dp)
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    BrandButton(
                        text = if (locale == AppLocale.EN) "Contact Us" else "Hubungi Kami",
                        onClick = { menuOpen = false; onContactClick() },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

/** .links a: muted label with a gradient underline when active or hovered. */
@Composable
private fun NavLink(label: String, active: Boolean, onClick: () -> Unit) {
    val source = remember { MutableInteractionSource() }
    val hovered by source.collectIsHoveredAsState()
    val underline by animateFloatAsState(if (active || hovered) 1f else 0f, tween(350, easing = BrandEasing), label = "navUnderline")
    val color by animateColorAsState(if (active || hovered) BrandColors.Ink else BrandColors.Ink2, tween(250), label = "navColor")
    Column(
        modifier = Modifier
            .width(IntrinsicSize.Max)
            .hoverable(source)
            .clickable(interactionSource = source, indication = null, role = Role.Button, onClick = onClick)
            .pointerHoverIcon(PointerIcon.Hand)
            .semantics { selected = active }
            .padding(vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(label, style = brandType().nav, color = color)
        Spacer(Modifier.height(4.dp))
        Box(
            Modifier
                .height(2.dp)
                .fillMaxWidth()
                .graphicsLayer { scaleX = underline }
                .clip(BrandShapes.Pill)
                .background(BrandBrushes.PrimaryHorizontal)
        )
    }
}

// =========================================================================
// DESKTOP PREVIEWS (WEB-07-03-04 / #22671)
// =========================================================================

@Composable
private fun NavbarPreviewFrame(width: Dp, background: Color = BrandColors.Subtle, content: @Composable () -> Unit) {
    BrandTheme(reduceMotion = true) {
        Box(Modifier.width(width).background(background).padding(bottom = 16.dp)) { content() }
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun NavbarTransparentOverHeroPreview() {
    NavbarPreviewFrame(1280.dp) {
        Navbar(NavigationItem.CANONICAL_ITEMS, AppLocale.ID, activeAnchor = null, solid = false, onRoute = {}, onLocaleChange = {}, onContactClick = {})
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun NavbarScrolledActiveSectionPreview() {
    NavbarPreviewFrame(1280.dp) {
        Navbar(NavigationItem.CANONICAL_ITEMS, AppLocale.ID, activeAnchor = LandingAnchor.LAYANAN, solid = true, onRoute = {}, onLocaleChange = {}, onContactClick = {})
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun NavbarEnglishPreview() {
    NavbarPreviewFrame(1280.dp) {
        Navbar(NavigationItem.CANONICAL_ITEMS, AppLocale.EN, activeAnchor = LandingAnchor.WAWASAN, solid = true, onRoute = {}, onLocaleChange = {}, onContactClick = {})
    }
}

@Preview(device = DESKTOP, showBackground = true, widthDp = 390, heightDp = 640)
@Composable
fun NavbarCompactMenuOpenPreview() {
    NavbarPreviewFrame(390.dp, background = Color.White) {
        Navbar(NavigationItem.CANONICAL_ITEMS, AppLocale.ID, activeAnchor = LandingAnchor.FAQ, solid = true, onRoute = {}, onLocaleChange = {}, onContactClick = {}, initiallyOpen = true)
    }
}

@Preview(device = DESKTOP, showBackground = true, widthDp = 390)
@Composable
fun NavbarCompactClosedPreview() {
    NavbarPreviewFrame(390.dp) {
        Navbar(NavigationItem.CANONICAL_ITEMS, AppLocale.ID, activeAnchor = null, solid = false, onRoute = {}, onLocaleChange = {}, onContactClick = {})
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun NavbarEmptyItemsPreview() {
    NavbarPreviewFrame(1280.dp) {
        Navbar(emptyList(), AppLocale.ID, activeAnchor = null, solid = true, onRoute = {}, onLocaleChange = {}, onContactClick = {})
    }
}
