package id.sekota.feature.contact.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.LinkInteractionListener
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.sekota.app.AppState
import id.sekota.app.chrome.SiteFooter
import id.sekota.app.navigation.Route
import id.sekota.core.designsystem.ContentWrap
import id.sekota.core.designsystem.HeroBackdrop
import id.sekota.core.designsystem.Overline
import id.sekota.core.designsystem.SekotaButton
import id.sekota.core.designsystem.SekotaColors
import id.sekota.core.designsystem.SekotaIcons
import id.sekota.core.designsystem.SekotaShape
import id.sekota.core.designsystem.SekotaTheme
import id.sekota.core.designsystem.VSpace
import id.sekota.core.model.Language
import id.sekota.core.model.LText
import id.sekota.core.ui.LocalViewportHeight
import id.sekota.core.ui.LocalWindowSize
import id.sekota.core.ui.WindowSize
import id.sekota.feature.contact.domain.ContactContent
import id.sekota.feature.contact.domain.ContactFieldError
import id.sekota.feature.contact.domain.ContactTopicKey
import id.sekota.feature.contact.domain.GetContactContent
import id.sekota.feature.contact.domain.SendContactMessage
import id.sekota.feature.contact.domain.topicLabel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun ContactScreen(
    appState: AppState,
    initialPreset: String?,
    page: String = "",
) {
    val send = koinInject<SendContactMessage>()
    val getContact = koinInject<GetContactContent>()
    val content = remember { getContact() }
    val viewModel = remember(initialPreset) { ContactViewModel(send) }
    val scope = rememberCoroutineScope()
    val state by viewModel.state.collectAsState()
    val language = appState.language
    val window = LocalWindowSize.current
    val listState = rememberLazyListState()
    DisposableEffect(listState) {
        appState.activeListState = listState
        onDispose { if (appState.activeListState === listState) appState.activeListState = null }
    }

    LaunchedEffect(initialPreset, language) {
        val prefill = getContact.prefill(initialPreset) ?: return@LaunchedEffect
        viewModel.prefill(prefill.topic, prefill.message[language])
    }
    LaunchedEffect(initialPreset) {
        listState.scrollToItem(0)
    }
    LaunchedEffect(listState) {
        androidx.compose.runtime.snapshotFlow { listState.firstVisibleItemIndex }
            .collect { appState.chromeSolid = it > 0 }
    }

    val stacked = window != WindowSize.Expanded
    val compact = window == WindowSize.Compact

    LazyColumn(state = listState, modifier = Modifier.fillMaxWidth()) {
        item(key = "hero") {
            Box(
                Modifier
                    .fillMaxWidth()
                    .heightIn(min = LocalViewportHeight.current)
                    .background(SekotaColors.Bg),
            ) {
                HeroBackdrop(Modifier.matchParentSize())
                ContentWrap {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = if (compact) 120.dp else 152.dp, bottom = if (compact) 56.dp else 96.dp),
                    ) {
                        if (stacked) {
                            Column(verticalArrangement = Arrangement.spacedBy(40.dp)) {
                                ContactIntro(content, language, compact)
                                FormCard(
                                    content = content,
                                    language = language,
                                    state = state,
                                    viewModel = viewModel,
                                    scope = scope,
                                    page = page,
                                    stacked = true,
                                    onPrivacy = { appState.navigator.navigate(Route.Privacy) },
                                )
                            }
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(72.dp),
                                verticalAlignment = Alignment.Top,
                            ) {
                                Box(Modifier.weight(1f)) {
                                    ContactIntro(content, language, compact)
                                }
                                Box(Modifier.width(560.dp)) {
                                    FormCard(
                                        content = content,
                                        language = language,
                                        state = state,
                                        viewModel = viewModel,
                                        scope = scope,
                                        page = page,
                                        stacked = false,
                                        onPrivacy = { appState.navigator.navigate(Route.Privacy) },
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        item(key = "footer") { SiteFooter(appState) }
    }
}

@Composable
private fun ContactIntro(
    content: ContactContent,
    language: Language,
    compact: Boolean,
) {
    val type = SekotaTheme.type
    val titleStyle = when (LocalWindowSize.current) {
        WindowSize.Compact -> type.headline.copy(fontSize = 32.sp, lineHeight = 38.sp)
        WindowSize.Medium -> type.headline.copy(fontSize = 36.sp, lineHeight = 42.sp)
        WindowSize.Wide -> type.headline.copy(fontSize = 42.sp, lineHeight = 48.sp)
        WindowSize.Expanded -> type.headline.copy(fontSize = 48.sp, lineHeight = 55.sp, letterSpacing = (-1.44).sp)
    }
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        Overline(content.overline[language])
        Text(
            text = buildAnnotatedString {
                append(content.titlePrefix[language])
                withStyle(SpanStyle(brush = SekotaColors.BrandGradient)) {
                    append(content.titleGradient[language])
                }
            },
            style = titleStyle,
            color = SekotaColors.Ink,
            modifier = Modifier.semantics { heading() },
        )
        Text(
            text = content.lead[language],
            style = type.lead,
            color = SekotaColors.Ink2,
            modifier = Modifier.widthIn(max = 560.dp),
        )
        VSpace(8.dp)
        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
            content.steps.forEach { step ->
                Row(verticalAlignment = Alignment.Top) {
                    Box(
                        Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(SekotaColors.BrandGradient),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = step.number,
                            style = type.monoSmall.copy(fontSize = 13.sp),
                            color = Color.White,
                        )
                    }
                    Spacer(Modifier.width(14.dp))
                    Column {
                        Text(
                            text = step.title[language],
                            style = type.h4.copy(fontSize = 16.sp),
                            color = SekotaColors.Ink,
                        )
                        Text(
                            text = step.text[language],
                            style = type.small,
                            color = SekotaColors.Ink2,
                        )
                    }
                }
            }
        }
        VSpace(8.dp)
        DirectEmail(content, language)
    }
}

@Composable
private fun DirectEmail(content: ContactContent, language: Language) {
    val uriHandler = LocalUriHandler.current
    val cardWidth = if (LocalWindowSize.current == WindowSize.Compact) {
        Modifier.fillMaxWidth()
    } else {
        Modifier.width(420.dp)
    }
    val source = remember { MutableInteractionSource() }
    val hovered by source.collectIsHoveredAsState()
    val clipboard = LocalClipboardManager.current
    val scope = rememberCoroutineScope()
    var copied by remember { mutableStateOf(false) }
    Row(
        modifier = cardWidth
            .clip(SekotaShape.md)
            .background(if (hovered) SekotaColors.MutedBg else SekotaColors.Subtle)
            .border(1.dp, if (hovered) SekotaColors.Cyan.copy(alpha = 0.45f) else SekotaColors.Line, SekotaShape.md)
            .hoverable(source)
            .clickable(
                interactionSource = source,
                indication = null,
            ) { uriHandler.openUri("mailto:${content.email}") }
            .semantics { role = Role.Button }
            .testTag("direct-email")
            .padding(start = 16.dp, end = 10.dp, top = 14.dp, bottom = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = SekotaIcons.Mail,
            contentDescription = null,
            tint = SekotaColors.Teal,
            modifier = Modifier.size(20.dp),
        )
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(
                text = content.directEmailLabel[language],
                style = SekotaTheme.type.caption,
                color = SekotaColors.Ink2,
            )
            Text(
                text = content.email,
                style = SekotaTheme.type.h4,
                color = if (hovered) SekotaColors.BrandInk else SekotaColors.Ink,
                textDecoration = if (hovered) TextDecoration.Underline else null,
            )
        }
        Box(
            Modifier
                .size(32.dp)
                .clip(CircleShape)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ) {
                    runCatching {
                        clipboard.setText(AnnotatedString(content.email))
                        copied = true
                        scope.launch {
                            delay(2000)
                            copied = false
                        }
                    }
                }
                .semantics { role = Role.Button }
                .testTag("copy-email"),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = if (copied) SekotaIcons.Check else SekotaIcons.Copy,
                contentDescription = if (copied) {
                    content.copiedEmailLabel[language]
                } else {
                    content.copyEmailLabel[language]
                },
                tint = if (copied) SekotaColors.SuccessInk else SekotaColors.Teal,
                modifier = Modifier.size(17.dp),
            )
        }
    }
}

@Composable
private fun FormCard(
    content: ContactContent,
    language: Language,
    state: ContactUiState,
    viewModel: ContactViewModel,
    scope: kotlinx.coroutines.CoroutineScope,
    page: String,
    stacked: Boolean,
    onPrivacy: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 24.dp,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
                ambientColor = SekotaColors.Ink.copy(alpha = 0.12f),
                spotColor = SekotaColors.Ink.copy(alpha = 0.12f),
            )
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(24.dp))
            .background(Color.White)
            .border(
                1.dp,
                SekotaColors.Line,
                androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
            )
            .padding(if (stacked) 24.dp else 36.dp),
    ) {
        if (state.sent) {
            SuccessPanel(
                content = content,
                language = language,
                name = state.sentName,
                email = state.sentEmail,
                onReset = viewModel::reset,
            )
        } else {
            Text(
                content.formTitle[language],
                style = SekotaTheme.type.h3.copy(fontSize = 24.sp, lineHeight = 32.sp),
                color = SekotaColors.Ink,
            )
            VSpace(4.dp)
            Text(
                text = buildAnnotatedString {
                    append(content.requiredNote[language].substringBefore("*"))
                    withStyle(SpanStyle(color = SekotaColors.Required)) { append("*") }
                    append(content.requiredNote[language].substringAfter("*"))
                },
                style = SekotaTheme.type.caption,
                color = SekotaColors.Ink2,
            )
            VSpace(20.dp)
            FormField(
                label = content.nameLabel[language],
                required = true,
                value = state.form.name,
                onValueChange = viewModel::onNameChange,
                placeholder = content.namePlaceholder[language],
                error = state.validation.name.takeIf { state.showErrors }?.let { content.errors.nameRequired[language] },
                testTag = "field-name",
            )
            VSpace(16.dp)
            FormField(
                label = content.emailLabel[language],
                required = true,
                value = state.form.email,
                onValueChange = viewModel::onEmailChange,
                placeholder = content.emailPlaceholder[language],
                error = state.validation.email.takeIf { state.showErrors }?.let { content.errors.emailInvalid[language] },
                testTag = "field-email",
            )
            VSpace(16.dp)
            TopicField(content, language, state.form.topic, viewModel::onTopicChange)
            VSpace(16.dp)
            FormField(
                label = content.messageLabel[language],
                required = true,
                value = state.form.message,
                onValueChange = viewModel::onMessageChange,
                placeholder = content.messagePlaceholder[language],
                error = state.validation.message.takeIf { state.showErrors }?.let { content.errors.messageRequired[language] },
                singleLine = false,
                minHeight = 140.dp,
                testTag = "field-message",
            )
            VSpace(20.dp)
            if (state.failed) {
                Text(
                    text = content.sendError[language],
                    style = SekotaTheme.type.small.copy(fontWeight = FontWeight.Medium),
                    color = SekotaColors.Required,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .semantics { liveRegion = LiveRegionMode.Polite },
                )
            }
            SekotaButton(
                text = if (state.sending) {
                    content.sendingLabel[language]
                } else {
                    content.submitLabel[language]
                },
                onClick = {
                    scope.launch {
                        viewModel.submit(
                            topicLabel = content.topicLabel(state.form.topic, language),
                            labels = if (language == Language.ID) content.draftLabels else content.draftLabelsEn,
                            language = language,
                            page = page,
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.sending,
                trailingIcon = if (state.sending) null else SekotaIcons.ArrowRight,
            )
            VSpace(12.dp)
            Text(
                text = buildAnnotatedString {
                    append(content.hintBefore[language])
                    withLink(
                        LinkAnnotation.Clickable(
                            tag = "privacy",
                            styles = TextLinkStyles(
                                style = SpanStyle(
                                    color = SekotaColors.BrandInk,
                                    textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline,
                                ),
                            ),
                            linkInteractionListener = LinkInteractionListener { onPrivacy() },
                        ),
                    ) {
                        append(content.hintLink[language])
                    }
                    append(content.hintAfter[language])
                },
                style = SekotaTheme.type.caption.copy(fontSize = 12.sp, lineHeight = 17.sp),
                color = SekotaColors.Muted,
            )
        }
    }
}

@Composable
private fun FormField(
    label: String,
    required: Boolean,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    error: String?,
    singleLine: Boolean = true,
    minHeight: androidx.compose.ui.unit.Dp = 48.dp,
    testTag: String? = null,
) {
    Column(Modifier.fillMaxWidth()) {
        Row {
            Text(
                text = label,
                style = SekotaTheme.type.caption.copy(fontWeight = FontWeight.SemiBold),
                color = SekotaColors.Ink,
            )
            if (required) {
                Text(" *", style = SekotaTheme.type.caption, color = SekotaColors.Required)
            }
        }
        Spacer(Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = minHeight)
                .clip(SekotaShape.md)
                .background(SekotaColors.Subtle)
                .border(
                    1.dp,
                    if (error != null) SekotaColors.Required else SekotaColors.Line,
                    SekotaShape.md,
                )
                .padding(horizontal = 14.dp, vertical = if (singleLine) 13.dp else 12.dp),
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = singleLine,
                textStyle = SekotaTheme.type.body.copy(fontSize = 16.sp, lineHeight = 24.sp, color = SekotaColors.Ink),
                cursorBrush = SolidColor(SekotaColors.Cyan),
                visualTransformation = VisualTransformation.None,
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics {
                        contentDescription = label + if (required) " *" else ""
                        if (error != null) this.error(error)
                    }
                    .then(if (testTag != null) Modifier.testTag(testTag) else Modifier)
                    .then(if (singleLine) Modifier else Modifier.heightIn(min = minHeight - 24.dp)),
                decorationBox = { innerTextField ->
                    Box {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = SekotaTheme.type.body.copy(fontSize = 16.sp, lineHeight = 24.sp),
                                color = SekotaColors.Muted,
                            )
                        }
                        innerTextField()
                    }
                },
            )
        }
        if (error != null) {
            Text(
                text = error,
                style = SekotaTheme.type.caption.copy(fontSize = 12.sp),
                color = SekotaColors.Required,
                modifier = Modifier.padding(top = 6.dp),
            )
        }
    }
}

@Composable
private fun TopicField(
    content: ContactContent,
    language: Language,
    selected: ContactTopicKey,
    onSelect: (ContactTopicKey) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedLabel = content.topics.first { it.key == selected }.label[language]
    Column(Modifier.fillMaxWidth()) {
        Text(
            text = content.topicLabel[language],
            style = SekotaTheme.type.caption.copy(fontWeight = FontWeight.SemiBold),
            color = SekotaColors.Ink,
        )
        Spacer(Modifier.height(6.dp))
        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(SekotaShape.md)
                    .background(SekotaColors.Subtle)
                    .border(1.dp, SekotaColors.Line, SekotaShape.md)
                    .clickable { expanded = true }
                    .semantics {
                        role = Role.Button
                        stateDescription = if (expanded) "Terbuka" else "Tertutup"
                    }
                    .padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = selectedLabel,
                    style = SekotaTheme.type.body.copy(fontSize = 16.sp),
                    color = SekotaColors.Ink,
                    modifier = Modifier.weight(1f),
                )
                Icon(
                    imageVector = SekotaIcons.ChevronLeft,
                    contentDescription = null,
                    tint = SekotaColors.Muted,
                    modifier = Modifier.size(16.dp).graphicsLayer { rotationZ = -90f },
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Color.White),
            ) {
                content.topics.forEach { topic ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = topic.label[language],
                                style = SekotaTheme.type.small,
                                color = SekotaColors.Ink,
                            )
                        },
                        onClick = {
                            expanded = false
                            onSelect(topic.key)
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun SuccessPanel(
    content: ContactContent,
    language: Language,
    name: String,
    email: String,
    onReset: () -> Unit,
) {
    val message = content.successText[language]
        .replace("{n}", name)
        .replace("{name}", name)
        .replace("{e}", email)
        .replace("{email}", email)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .semantics { liveRegion = LiveRegionMode.Polite },
    ) {
        Box(
            Modifier
                .size(48.dp)
                .clip(SekotaShape.md)
                .background(SekotaColors.BrandGradient),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = SekotaIcons.Check,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp),
            )
        }
        VSpace(20.dp)
        Text(
            text = content.successTitle[language],
            style = SekotaTheme.type.h3.copy(fontSize = 22.sp, lineHeight = 30.sp),
            color = SekotaColors.Ink,
            modifier = Modifier.semantics { heading() },
        )
        VSpace(10.dp)
        Text(
            text = message,
            style = SekotaTheme.type.small.copy(fontSize = 15.sp, lineHeight = 24.sp),
            color = SekotaColors.Ink2,
        )
        VSpace(24.dp)
        SekotaButton(
            text = content.againLabel[language],
            onClick = onReset,
            variant = id.sekota.core.designsystem.ButtonVariant.Secondary,
            size = id.sekota.core.designsystem.ButtonSize.Small,
            trailingIcon = null,
        )
    }
}
