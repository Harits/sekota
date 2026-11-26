package id.sekota.feature.landing.domain

import id.sekota.core.content.SiteNavItem
import id.sekota.core.model.BookSlug
import id.sekota.core.model.IconKey
import id.sekota.core.model.LText
import id.sekota.core.model.ProductSlug

data class LandingContent(
    val navItems: List<SiteNavItem>,
    val hero: HeroContent,
    val sectorsTitle: LText,
    val sectors: List<SectorItem>,
    val about: AboutContent,
    val services: ServicesContent,
    val process: ProcessContent,
    val platforms: PlatformsContent,
    val books: BooksSectionContent,
    val partner: PartnerContent,
    val faq: FaqContent,
)

data class HeroContent(
    val chip: LText,
    val titleLine1: LText,
    val titleLine2: LText,
    val lead: LText,
    val primaryCta: LText,
    val secondaryCta: LText,
    val proofPlatforms: LText,
    val proofBooks: LText,
    val suiteTitle: LText,
    val suiteSubtitle: LText,
    val suiteCount: LText,
    val suiteRows: List<SuiteRow>,
    val illustrationLabel: LText,
)

data class SuiteRow(
    val slug: ProductSlug,
    val serviceName: LText,
    val description: LText,
    val tag: LText,
    val tagStyle: TagStyle,
)

enum class TagStyle { Neutral, Green, Cyan }

data class SectorItem(val icon: IconKey, val label: LText)

data class AboutContent(
    val overline: LText,
    val title: LText,
    val lead: LText,
    val reasons: List<ReasonItem>,
)

data class ReasonItem(val icon: IconKey, val title: LText, val text: LText)

data class ServicesContent(
    val overline: LText,
    val title: LText,
    val items: List<ServiceItem>,
    val alsoAvailable: LText,
    val extras: List<LText>,
)

data class ServiceItem(
    val number: String,
    val title: LText,
    val tagline: LText,
    val points: List<LText>,
    val icon: IconKey,
    val platform: ProductSlug,
)

data class ProcessContent(
    val overline: LText,
    val title: LText,
    val subtitle: LText,
    val steps: List<ProcessStep>,
    val collaborationTitle: LText,
    val collaborationModels: List<LText>,
)

data class ProcessStep(val number: String, val title: LText, val description: LText, val icon: IconKey)

data class PlatformsContent(
    val overline: LText,
    val title: LText,
    val subtitle: LText,
    val techBadge: LText,
    val items: List<PlatformCard>,
)

data class PlatformCard(
    val slug: ProductSlug,
    val description: LText,
    val learnMore: LText,
)

data class BooksSectionContent(
    val overline: LText,
    val title: LText,
    val subtitle: LText,
    val items: List<BookCard>,
    val detailLabel: LText,
)

data class BookCard(
    val slug: BookSlug,
    val title: String,
    val tagline: LText,
    val blurb: LText,
)

data class PartnerContent(
    val title: LText,
    val text: LText,
    val cta: LText,
)

data class FaqContent(
    val overline: LText,
    val title: LText,
    val mailboxLabel: LText,
    val email: String,
    val items: List<FaqItem>,
)

data class FaqItem(val id: Int, val question: LText, val answer: LText)
