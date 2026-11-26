package id.sekota.feature.product.domain

import id.sekota.core.model.IconKey
import id.sekota.core.model.LText
import id.sekota.core.model.ProductSlug

data class ProductDetail(
    val slug: ProductSlug,
    val name: String,
    val serviceName: LText,
    val breadcrumb: LText,
    val headline: LText,
    val lead: LText,
    val demoCta: LText,
    val featuresCta: LText,
    val problemsOverline: LText,
    val problemsTitle: LText,
    val problems: List<ProductProblem>,
    val featuresOverline: LText,
    val featuresTitle: LText,
    val features: List<ProductFeature>,
    val screensOverline: LText,
    val screensTitle: LText,
    val screensNote: LText,
    val screens: List<ProductScreen>,
    val flowOverline: LText,
    val flowTitle: LText,
    val flow: List<ProductFlowStep>,
    val audienceOverline: LText,
    val audienceTitle: LText,
    val audience: List<LText>,
    val relatedOverline: LText,
    val relatedTitle: LText,
    val relatedText: LText,
    val otherPlatformsOverline: LText,
    val otherPlatformsTitle: LText,
    val ctaTitle: LText,
    val ctaText: LText,
    val ctaButton: LText,
)

data class ProductProblem(val number: String, val title: LText, val text: LText)

data class ProductFeature(val icon: IconKey, val title: LText, val text: LText)

data class ProductScreen(val title: LText, val description: LText)

data class ProductFlowStep(val number: String, val title: LText, val text: LText, val icon: IconKey)

interface ProductRepository {
    fun load(slug: ProductSlug): ProductDetail?
}

class GetProductDetail(private val repository: ProductRepository) {
    operator fun invoke(slug: ProductSlug): ProductDetail? = repository.load(slug)
}
