package id.sekota.feature.product

import id.sekota.core.model.ProductSlug
import id.sekota.feature.product.data.StaticProductRepository
import id.sekota.feature.product.domain.GetProductDetail
import id.sekota.core.ui.screens
import id.sekota.feature.product.presentation.ProductViewModel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ProductViewModelTest {

    private val getProduct = GetProductDetail(StaticProductRepository())

    @Test
    fun everyPlatformHasDetail() {
        ProductSlug.entries.forEach { slug ->
            assertNotNull(getProduct(slug), "missing detail for $slug")
        }
    }

    @Test
    fun selectScreenIsBounded() {
        val detail = getProduct(ProductSlug.Clarivio)!!
        val vm = ProductViewModel(detail)
        assertEquals(0, vm.state.value.selectedScreen)
        vm.selectScreen(1)
        assertEquals(1, vm.state.value.selectedScreen)
        vm.selectScreen(10)
        assertEquals(1, vm.state.value.selectedScreen)
        vm.selectScreen(-1)
        assertEquals(1, vm.state.value.selectedScreen)
    }

    @Test
    fun galleryAutoAdvancesUntilUserSelects() {
        val detail = getProduct(ProductSlug.Clarivio)!!
        val vm = ProductViewModel(detail)
        assertEquals(0, vm.state.value.selectedScreen)
        assertEquals(true, vm.state.value.autoAdvance)

        vm.advanceAuto()
        assertEquals(1, vm.state.value.selectedScreen)
        vm.advanceAuto()
        assertEquals(0, vm.state.value.selectedScreen)

        vm.selectScreen(1)
        assertEquals(false, vm.state.value.autoAdvance)
        vm.advanceAuto()
        assertEquals(1, vm.state.value.selectedScreen)
    }

    @Test
    fun everyScreenTabHasMatchingScreenshot() {
        ProductSlug.entries.forEach { slug ->
            val detail = getProduct(slug)!!
            assertEquals(
                detail.screens.size,
                slug.screens().size,
                "screen tab/asset mismatch for $slug",
            )
        }
    }

    @Test
    fun unknownSlugReturnsNull() {
        // The repository is keyed by the enum, so an unknown key can only be exercised
        // through a custom repository; ensure the contract is nullable.
        val empty = GetProductDetail(object : id.sekota.feature.product.domain.ProductRepository {
            override fun load(slug: ProductSlug) = null
        })
        assertNull(empty(ProductSlug.Clarivio))
    }
}
