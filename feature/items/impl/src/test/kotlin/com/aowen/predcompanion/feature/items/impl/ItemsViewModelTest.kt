package com.aowen.predcompanion.feature.items.impl

import com.aowen.predcompanion.core.model.data.HeroClass
import com.aowen.predcompanion.core.model.data.ItemDetails
import com.aowen.predcompanion.core.model.data.Rarity
import com.aowen.predcompanion.core.testing.fakes.repository.FakeItemRepository
import com.aowen.predcompanion.core.testing.util.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ItemsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val fakeItemRepository = FakeItemRepository()

    private fun createItem(
        id: String,
        displayName: String,
        rarity: Rarity = Rarity.COMMON,
        heroClass: HeroClass = HeroClass.UNKNOWN,
        stats: List<ItemDetails.StatDetails> = emptyList(),
    ) = ItemDetails(
        id = id,
        name = displayName,
        displayName = displayName,
        rarity = rarity,
        heroClass = heroClass,
        stats = stats,
    )

    @Test
    fun `init with empty items shows error`() = runTest {
        val viewModel = ItemsViewModel(fakeItemRepository)

        val state = viewModel.uiState.value
        assertEquals(false, state.isLoading)
        assertEquals("Failed to fetch items", state.itemsError)
        assertTrue(state.allItems.isEmpty())
        assertTrue(state.filteredItems.isEmpty())
    }

    @Test
    fun `init with items loads and filters to Tier III`() = runTest {
        val commonItem = createItem(id = "1", displayName = "Common Item", rarity = Rarity.COMMON)
        val rareItem = createItem(id = "2", displayName = "Rare Item", rarity = Rarity.RARE)
        val epicItem = createItem(id = "3", displayName = "Epic Item", rarity = Rarity.EPIC)
        val legendaryItem =
            createItem(id = "4", displayName = "Legendary Item", rarity = Rarity.LEGENDARY)

        fakeItemRepository.emitItems(listOf(commonItem, rareItem, epicItem, legendaryItem))

        val viewModel = ItemsViewModel(fakeItemRepository)

        val state = viewModel.uiState.value
        assertEquals(false, state.isLoading)
        assertNull(state.itemsError)
        assertEquals(4, state.allItems.size)
        assertEquals(2, state.filteredItems.size)
        assertTrue(state.filteredItems.all { it.rarity.value == "Tier III" })
        assertTrue(state.filteredItems.containsAll(listOf(epicItem, legendaryItem)))
    }

    @Test
    fun `onSelectTier updates selectedTierFilter`() = runTest {
        val viewModel = ItemsViewModel(fakeItemRepository)

        viewModel.onSelectTier("Tier II")

        assertEquals("Tier II", viewModel.uiState.value.selectedTierFilter)
    }

    @Test
    fun `onSelectHeroClass updates selectedHeroClass`() = runTest {
        val viewModel = ItemsViewModel(fakeItemRepository)

        viewModel.onSelectHeroClass(HeroClass.MAGE)

        assertEquals(HeroClass.MAGE, viewModel.uiState.value.selectedHeroClass)
    }

    @Test
    fun `onSelectStat toggles stat filter`() = runTest {
        val viewModel = ItemsViewModel(fakeItemRepository)

        viewModel.onSelectStat("Power")
        assertEquals(listOf("Power"), viewModel.uiState.value.selectedStatFilters)

        viewModel.onSelectStat("Power")
        assertTrue(viewModel.uiState.value.selectedStatFilters.isEmpty())
    }

    @Test
    fun `getFilteredItems filters by hero class and tier`() = runTest {
        val matchingItem = createItem(
            id = "1",
            displayName = "Matching Item",
            rarity = Rarity.EPIC,
            heroClass = HeroClass.MAGE,
        )
        val wrongHeroClassItem = createItem(
            id = "2",
            displayName = "Wrong Hero Class Item",
            rarity = Rarity.EPIC,
            heroClass = HeroClass.TANK,
        )
        val wrongTierItem = createItem(
            id = "3",
            displayName = "Wrong Tier Item",
            rarity = Rarity.RARE,
            heroClass = HeroClass.MAGE,
        )

        fakeItemRepository.emitItems(listOf(matchingItem, wrongHeroClassItem, wrongTierItem))

        val viewModel = ItemsViewModel(fakeItemRepository)
        viewModel.onSelectHeroClass(HeroClass.MAGE)
        viewModel.onSelectTier("Tier III")

        viewModel.getFilteredItems()

        val filteredItems = viewModel.uiState.value.filteredItems
        assertEquals(listOf(matchingItem), filteredItems)
    }
}
