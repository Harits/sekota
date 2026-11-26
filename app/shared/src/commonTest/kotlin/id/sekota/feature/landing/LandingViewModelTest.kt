package id.sekota.feature.landing

import id.sekota.core.model.Language
import id.sekota.core.model.ltext
import id.sekota.feature.landing.data.LandingCatalog
import id.sekota.feature.landing.data.StaticLandingRepository
import id.sekota.feature.landing.domain.GetLandingContent
import id.sekota.feature.landing.domain.LandingRepository
import id.sekota.feature.landing.presentation.LandingViewModel
import kotlin.test.Test
import kotlin.test.assertEquals

class LandingViewModelTest {

    private fun viewModel() = LandingViewModel(GetLandingContent(StaticLandingRepository()))

    @Test
    fun highlightCyclesThroughPlatforms() {
        val vm = viewModel()
        assertEquals(0, vm.state.value.highlightedRow)
        vm.highlightNextRow()
        assertEquals(1, vm.state.value.highlightedRow)
        repeat(3) { vm.highlightNextRow() }
        assertEquals(0, vm.state.value.highlightedRow)
    }

    @Test
    fun highlightIndexOutsideRangeIsIgnored() {
        val vm = viewModel()
        vm.highlightRow(99)
        assertEquals(0, vm.state.value.highlightedRow)
        vm.highlightRow(2)
        assertEquals(2, vm.state.value.highlightedRow)
    }

    @Test
    fun faqOpensAndCloses() {
        val vm = viewModel()
        assertEquals(0, vm.state.value.openFaqId)
        vm.toggleFaq(2)
        assertEquals(2, vm.state.value.openFaqId)
        vm.toggleFaq(2)
        assertEquals(-1, vm.state.value.openFaqId)
    }

    @Test
    fun faqIsSingleOpen() {
        val vm = viewModel()
        vm.toggleFaq(2)
        vm.toggleFaq(4)
        assertEquals(4, vm.state.value.openFaqId)
    }

    @Test
    fun contentExposesDesignedCounts() {
        val vm = viewModel()
        assertEquals(4, vm.content.services.items.size)
        assertEquals(6, vm.content.books.items.size)
    }

    @Test
    fun viewModelConsumesTheInjectedRepository() {
        val fake = LandingCatalog.content.copy(
            hero = LandingCatalog.content.hero.copy(titleLine1 = ltext("Judul uji", "Test title")),
        )
        val repository = object : LandingRepository {
            override fun load() = fake
        }
        val vm = LandingViewModel(GetLandingContent(repository))
        assertEquals("Judul uji", vm.content.hero.titleLine1[Language.ID])
        assertEquals("Test title", vm.content.hero.titleLine1[Language.EN])
    }
}
