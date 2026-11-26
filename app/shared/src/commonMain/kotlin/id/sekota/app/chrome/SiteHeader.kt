package id.sekota.app.chrome

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.InputMode
import androidx.compose.ui.platform.LocalInputModeManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import id.sekota.app.AppState
import id.sekota.app.navigation.Route
import id.sekota.core.content.SiteContent
import id.sekota.core.designsystem.ButtonSize
import id.sekota.core.designsystem.ContentWrap
import id.sekota.core.designsystem.SekotaButton
import id.sekota.core.designsystem.SekotaColors
import id.sekota.core.designsystem.SekotaEase
import id.sekota.core.designsystem.SekotaShape
import id.sekota.core.designsystem.SekotaTheme
import id.sekota.core.model.Language
import id.sekota.core.ui.LocalWindowSize
import id.sekota.core.ui.SekotaLogo
import id.sekota.core.ui.WindowSize

@Composable
fun SiteHeader(appState: AppState, modifier: Modifier = Modifier) {
    val window = LocalWindowSize.current
    val collapsed = window.isCollapsedNav
    val current = appState.navigator.current
    val solid = appState.chromeSolid || current !is Route.Home
    val keyboardInput = LocalInputModeManager.current.inputMode == InputMode.Keyboard
    var menuOpen by remember { mutableStateOf(false) }

    LaunchedEffect(current) { menuOpen = false }
    LaunchedEffect(window) { menuOpen = false }

    Column(
        modifier
            .fillMaxWidth()
            .zIndex(50f),
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .background(if (solid) Color.White.copy(alpha = 0.97f) else Color.Transparent)
                .drawBehind {
                    if (solid) {
                        drawLine(
                            color = SekotaColors.Line2,
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = 1.dp.toPx(),
                        )
                    }
                },
        ) {
            ContentWrap {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                            ) { menuOpen = false; appState.navigator.navigateHome("hero") }
                            .semantics { contentDescription = "Sekota Sinergi Indonesia" },
                    ) {
                        SekotaLogo(height = 36.dp)
                    }
                    if (!collapsed) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(if (window == WindowSize.Expanded) 36.dp else 22.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            SiteContent.navItems.forEach { item ->
                                NavLink(
                                    label = item.label[appState.language],
                                    active = current is Route.Home && appState.activeAnchor == item.anchor,
                                    keyboardInput = keyboardInput,
                                    onClick = {
                                        menuOpen = false
                                        appState.navigator.navigateHome(item.anchor)
                                    },
                                )
                            }
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        LanguageToggle(appState)
                        Spacer(Modifier.width(12.dp))
                        if (!collapsed) {
                            SekotaButton(
                                text = SiteContent.contactUs[appState.language],
                                onClick = { appState.navigator.navigate(Route.Contact()) },
                                size = ButtonSize.Small,
                                trailingIcon = null,
                            )
                        } else {
                            BurgerButton(open = menuOpen, onClick = { menuOpen = !menuOpen })
                        }
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = menuOpen && collapsed,
            enter = expandVertically(tween(250)) + fadeIn(tween(200)),
            exit = shrinkVertically(tween(200)) + fadeOut(tween(150)),
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 24.dp)
                    .padding(top = 8.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                SiteContent.navItems.forEach { item ->
                    val active = current is Route.Home && appState.activeAnchor == item.anchor
                    Text(
                        text = item.label[appState.language],
                        style = SekotaTheme.type.body.copy(
                            fontSize = 16.sp,
                            fontWeight = if (active) androidx.compose.ui.text.font.FontWeight.SemiBold else null,
                        ),
                        color = if (active) SekotaColors.BrandInk else SekotaColors.Ink,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 44.dp)
                            .clip(SekotaShape.sm)
                            .clickable {
                                menuOpen = false
                                appState.navigator.navigateHome(item.anchor)
                            }
                            .semantics { selected = active; role = Role.Button }
                            .padding(vertical = 12.dp),
                    )
                }
                Spacer(Modifier.height(12.dp))
                SekotaButton(
                    text = SiteContent.contactUs[appState.language],
                    onClick = {
                        menuOpen = false
                        appState.navigator.navigate(Route.Contact())
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun NavLink(
    label: String,
    active: Boolean,
    keyboardInput: Boolean,
    onClick: () -> Unit,
) {
    val source = remember { MutableInteractionSource() }
    val hovered by source.collectIsHoveredAsState()
    val focused by source.collectIsFocusedAsState()
    val highlight by animateFloatAsState(
        targetValue = if (active || hovered) 1f else 0f,
        animationSpec = tween(350, easing = SekotaEase),
        label = "navUnderline",
    )
    val color by animateColorAsState(
        targetValue = if (active || hovered) SekotaColors.Ink else SekotaColors.Ink2,
        animationSpec = tween(250),
        label = "navColor",
    )
    Text(
        text = label,
        style = SekotaTheme.type.nav,
        color = color,
        modifier = Modifier
            .heightIn(min = 44.dp)
            .hoverable(source)
            .clickable(interactionSource = source, indication = null, onClick = onClick)
            .semantics { selected = active; role = Role.Button }
            .padding(vertical = 11.dp)
            .drawWithContent {
                drawContent()
                if (highlight > 0.01f) {
                    val stroke = 2.dp.toPx()
                    val half = size.width * highlight / 2f
                    drawLine(
                        brush = SekotaColors.BrandGradient,
                        start = Offset(size.width / 2f - half, size.height - stroke / 2f),
                        end = Offset(size.width / 2f + half, size.height - stroke / 2f),
                        strokeWidth = stroke,
                        cap = androidx.compose.ui.graphics.StrokeCap.Round,
                    )
                }
                if (focused && keyboardInput) {
                    drawRoundRect(
                        color = SekotaColors.Ink,
                        cornerRadius = CornerRadius(6.dp.toPx()),
                        style = Stroke(2.dp.toPx()),
                    )
                }
            },
    )
}

@Composable
private fun LanguageToggle(appState: AppState) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .border(1.dp, SekotaColors.Line, RoundedCornerShape(10.dp))
            .background(Color.White.copy(alpha = 0.75f))
            .padding(3.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Language.entries.forEach { language ->
            val isSelected = appState.language == language
            val source = remember { MutableInteractionSource() }
            val hovered by source.collectIsHoveredAsState()
            Box(
                modifier = Modifier
                    .height(28.dp)
                    .width(30.dp)
                    .clip(RoundedCornerShape(7.dp))
                    .background(if (isSelected) SekotaColors.MutedBg else Color.Transparent)
                    .hoverable(source)
                    .clickable(interactionSource = source, indication = null) {
                        appState.changeLanguage(language)
                    }
                    .semantics { selected = isSelected; role = Role.Button },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = language.label,
                    style = SekotaTheme.type.caption.copy(
                        fontSize = 12.sp,
                        lineHeight = 12.sp,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
                    ),
                    color = if (isSelected || hovered) SekotaColors.Ink else SekotaColors.Ink2,
                )
            }
        }
    }
}

@Composable
private fun BurgerButton(open: Boolean, onClick: () -> Unit) {
    val top by animateFloatAsState(if (open) 45f else 0f, tween(300), label = "burgerTop")
    val bottom by animateFloatAsState(if (open) -45f else 0f, tween(300), label = "burgerBottom")
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(SekotaShape.md)
            .background(Color.White)
            .border(1.dp, SekotaColors.Line, SekotaShape.md)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
            .semantics {
                role = Role.Button
                contentDescription = if (open) "Tutup menu" else "Buka menu"
                stateDescription = if (open) "Terbuka" else "Tertutup"
            },
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                Modifier
                    .size(width = 18.dp, height = 2.dp)
                    .graphicsLayer { rotationZ = top; translationY = if (open) 6.dp.toPx() else 0f }
                    .background(SekotaColors.Ink, RoundedCornerShape(2.dp)),
            )
            Spacer(Modifier.height(4.dp))
            Box(
                Modifier
                    .size(width = 18.dp, height = 2.dp)
                    .graphicsLayer { alpha = if (open) 0f else 1f }
                    .background(SekotaColors.Ink, RoundedCornerShape(2.dp)),
            )
            Spacer(Modifier.height(4.dp))
            Box(
                Modifier
                    .size(width = 18.dp, height = 2.dp)
                    .graphicsLayer { rotationZ = bottom; translationY = if (open) -6.dp.toPx() else 0f }
                    .background(SekotaColors.Ink, RoundedCornerShape(2.dp)),
            )
        }
    }
}
