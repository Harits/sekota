package id.sekota.app.chrome

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import id.sekota.app.AppState
import id.sekota.app.navigation.Route
import id.sekota.core.content.SiteContent
import id.sekota.core.designsystem.ContentWrap
import id.sekota.core.designsystem.SekotaColors
import id.sekota.core.designsystem.SekotaSpacing
import id.sekota.core.designsystem.SekotaTheme
import id.sekota.core.designsystem.VSpace
import id.sekota.core.model.ProductSlug
import id.sekota.core.ui.LocalWindowSize
import id.sekota.core.ui.SekotaLogo
import id.sekota.core.ui.WindowSize

@Composable
fun SiteFooter(appState: AppState) {
    val window = LocalWindowSize.current
    val wide = window == WindowSize.Expanded || window == WindowSize.Wide
    val language = appState.language

    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 0.dp),
    ) {
        Box(Modifier.fillMaxWidth().height(0.dp))
        Column(
            Modifier
                .fillMaxWidth()
                .background(SekotaColors.Dark)
                .padding(top = 80.dp, bottom = 40.dp),
        ) {
            ContentWrap {
                Column {
                if (wide) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Column(Modifier.width(360.dp)) {
                            SekotaLogo(dark = true, height = 36.dp)
                            VSpace(20.dp)
                            Text(
                                text = SiteContent.footerDescription[language],
                                style = SekotaTheme.type.small,
                                color = SekotaColors.OnDarkMuted,
                            )
                        }
                        FooterColumn(
                            title = SiteContent.footerNavigation[language],
                        ) {
                            SiteContent.navItems.forEach { item ->
                                FooterLink(item.label[language]) {
                                    appState.navigator.navigateHome(item.anchor)
                                }
                            }
                        }
                        FooterColumn(
                            title = SiteContent.footerPlatform[language],
                        ) {
                            ProductSlug.entries.forEach { slug ->
                                FooterLink(SiteContent.platformNames.getValue(slug)[language]) {
                                    appState.navigator.navigate(Route.Product(slug))
                                }
                            }
                        }
                        FooterColumn(
                            title = SiteContent.footerContact[language],
                        ) {
                            FooterLink(SiteContent.contactUs[language]) {
                                appState.navigator.navigate(Route.Contact())
                            }
                            FooterLink(SiteContent.email) {}
                        }
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(32.dp)) {
                        SekotaLogo(dark = true, height = 36.dp)
                        Text(
                            text = SiteContent.footerDescription[language],
                            style = SekotaTheme.type.small,
                            color = SekotaColors.OnDarkMuted,
                        )
                        FooterColumn(title = SiteContent.footerNavigation[language]) {
                            SiteContent.navItems.forEach { item ->
                                FooterLink(item.label[language]) {
                                    appState.navigator.navigateHome(item.anchor)
                                }
                            }
                        }
                        FooterColumn(title = SiteContent.footerPlatform[language]) {
                            ProductSlug.entries.forEach { slug ->
                                FooterLink(SiteContent.platformNames.getValue(slug)[language]) {
                                    appState.navigator.navigate(Route.Product(slug))
                                }
                            }
                        }
                        FooterColumn(title = SiteContent.footerContact[language]) {
                            FooterLink(SiteContent.contactUs[language]) {
                                appState.navigator.navigate(Route.Contact())
                            }
                            FooterLink(SiteContent.email) {}
                        }
                    }
                }
                Spacer(Modifier.height(48.dp))
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(SekotaColors.OnDarkMuted.copy(alpha = 0.18f)),
                )
                Spacer(Modifier.height(40.dp))
                if (wide) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = SiteContent.copyright[language],
                            style = SekotaTheme.type.monoSmall,
                            color = SekotaColors.OnDarkMuted,
                        )
                        LegalLinks(appState)
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = SiteContent.copyright[language],
                            style = SekotaTheme.type.monoSmall,
                            color = SekotaColors.OnDarkMuted,
                        )
                        LegalLinks(appState)
                    }
                }
                }
            }
        }
    }
}

@Composable
private fun LegalLinks(appState: AppState) {
    val language = appState.language
    Row(verticalAlignment = Alignment.CenterVertically) {
        FooterLegalLink(SiteContent.privacy[language]) {
            appState.navigator.navigate(Route.Privacy)
        }
        Text(
            text = " · ",
            style = SekotaTheme.type.monoSmall,
            color = SekotaColors.OnDarkMuted,
        )
        FooterLegalLink(SiteContent.terms[language]) {
            appState.navigator.navigate(Route.Terms)
        }
    }
}

@Composable
private fun FooterLegalLink(text: String, onClick: () -> Unit) {
    val source = remember { MutableInteractionSource() }
    val hovered by source.collectIsHoveredAsState()
    Text(
        text = text,
        style = SekotaTheme.type.monoSmall,
        color = if (hovered) Color.White else SekotaColors.OnDarkMuted,
        modifier = Modifier
            .heightIn(min = 28.dp)
            .hoverable(source)
            .clickable(interactionSource = source, indication = null, onClick = onClick),
    )
}

@Composable
private fun FooterColumn(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier) {
        Text(
            text = title.uppercase(),
            style = SekotaTheme.type.monoOverline,
            color = androidx.compose.ui.graphics.Color.White,
        )
        Spacer(Modifier.height(14.dp))
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            content()
        }
    }
}

@Composable
private fun FooterLink(text: String, onClick: () -> Unit) {
    val uriHandler = LocalUriHandler.current
    val click = if (text == SiteContent.email) {
        { uriHandler.openUri("mailto:${SiteContent.email}") }
    } else {
        onClick
    }
    Text(
        text = text,
        style = SekotaTheme.type.small,
        color = SekotaColors.OnDarkMuted,
        modifier = Modifier.heightIn(min = 28.dp).clickable(onClick = click),
    )
}
