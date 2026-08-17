package com.aowen.predcompanion.feature.collections.impl

import app.cash.turbine.test
import com.aowen.predcompanion.core.model.data.EternalCategory
import com.aowen.predcompanion.core.model.data.EternalDetails
import com.aowen.predcompanion.core.model.data.HeroClass
import com.aowen.predcompanion.core.model.data.HeroDetails
import com.aowen.predcompanion.core.model.data.HeroRole
import com.aowen.predcompanion.core.model.data.ItemDetails
import com.aowen.predcompanion.core.model.data.Rarity
import com.aowen.predcompanion.core.testing.fakes.repository.FakeEternalRepository
import com.aowen.predcompanion.core.testing.fakes.repository.FakeHeroRepository
import com.aowen.predcompanion.core.testing.fakes.repository.FakeItemRepository
import com.aowen.predcompanion.core.testing.util.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class CollectionsScreenViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val fakeHeroRepository = FakeHeroRepository()
    private val fakeEternalRepository = FakeEternalRepository()
    private val fakeItemRepository = FakeItemRepository()

    private fun createViewModel() = CollectionsScreenViewModel(
        omedaCityHeroRepository = fakeHeroRepository,
        eternalRepository = fakeEternalRepository,
        itemRepository = fakeItemRepository,
    )

    private fun heroDetails(
        id: String,
        displayName: String,
        roles: List<HeroRole> = emptyList(),
        classes: List<HeroClass> = emptyList(),
    ) = HeroDetails(
        id = id,
        name = id,
        displayName = displayName,
        roles = roles,
        classes = classes,
    )

    private fun itemDetails(
        id: String,
        displayName: String,
        heroClass: HeroClass = HeroClass.UNKNOWN,
        rarity: Rarity = Rarity.EPIC,
        stats: List<ItemDetails.StatDetails> = emptyList(),
    ) = ItemDetails(
        id = id,
        name = id,
        displayName = displayName,
        heroClass = heroClass,
        rarity = rarity,
        stats = stats,
    )

    private val testEternalCategory = EternalCategory(
        id = "cat1",
        colorString = "#FF0000",
        categoryDescription = "Test",
        categoryType = EternalCategory.CategoryType.ANOMALIES,
        displayName = "Anomalies",
    )

    private fun eternalDetails(
        id: String,
        displayName: String,
        category: EternalCategory = testEternalCategory,
    ) = EternalDetails(
        id = id,
        displayName = displayName,
        description = "Test eternal",
        icon = "icon.png",
        eternalCategory = category,
    )

    // region Initial State

    @Test
    fun `when all repositories emit empty data, all collections show error state`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertTrue(state.heroesState is HeroCollectionState.Error)
            assertTrue(state.eternalsState is EternalCollectionState.Error)
            assertTrue(state.itemsState is ItemCollectionState.Error)
        }
    }

    // endregion

    // region Heroes

    @Test
    fun `heroes are sorted by display name`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem()

            fakeHeroRepository.emitHeroes(
                listOf(
                    heroDetails(id = "3", displayName = "Zephyr"),
                    heroDetails(id = "1", displayName = "Aurora"),
                    heroDetails(id = "2", displayName = "Muriel"),
                )
            )

            val state = awaitItem()
            val heroesState = state.heroesState as HeroCollectionState.Success
            val heroNames = heroesState.content.map { it.displayName }
            assertEquals(listOf("Aurora", "Muriel", "Zephyr"), heroNames)
            assertFalse(state.isLoading)
        }
    }

    @Test
    fun `empty heroes list produces error state`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            assertTrue(awaitItem().heroesState is HeroCollectionState.Error)
        }
    }

    @Test
    fun `selecting a role filter shows only heroes with that role`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem()

            fakeHeroRepository.emitHeroes(
                listOf(
                    heroDetails(id = "1", displayName = "Aurora", roles = listOf(HeroRole.Support)),
                    heroDetails(id = "2", displayName = "Murdock", roles = listOf(HeroRole.Carry)),
                    heroDetails(
                        id = "3",
                        displayName = "Phase",
                        roles = listOf(HeroRole.Support, HeroRole.Midlane)
                    ),
                )
            )
            awaitItem()

            viewModel.updateRoleOption(HeroRole.Support, true)

            val heroesState = awaitItem().heroesState as HeroCollectionState.Success
            val heroNames = heroesState.content.map { it.displayName }
            assertEquals(listOf("Aurora", "Phase"), heroNames)
        }
    }

    @Test
    fun `selecting multiple role filters shows heroes matching any selected role`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem()

            fakeHeroRepository.emitHeroes(
                listOf(
                    heroDetails(id = "1", displayName = "Aurora", roles = listOf(HeroRole.Support)),
                    heroDetails(id = "2", displayName = "Murdock", roles = listOf(HeroRole.Carry)),
                    heroDetails(id = "3", displayName = "Khaimera", roles = listOf(HeroRole.Jungle)),
                )
            )
            awaitItem()

            viewModel.updateRoleOption(HeroRole.Support, true)
            awaitItem()

            viewModel.updateRoleOption(HeroRole.Carry, true)

            val heroesState = awaitItem().heroesState as HeroCollectionState.Success
            val heroNames = heroesState.content.map { it.displayName }
            assertEquals(listOf("Aurora", "Murdock"), heroNames)
        }
    }

    @Test
    fun `deselecting a role filter restores all heroes`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem()

            fakeHeroRepository.emitHeroes(
                listOf(
                    heroDetails(id = "1", displayName = "Aurora", roles = listOf(HeroRole.Support)),
                    heroDetails(id = "2", displayName = "Murdock", roles = listOf(HeroRole.Carry)),
                )
            )
            awaitItem()

            viewModel.updateRoleOption(HeroRole.Support, true)
            awaitItem()

            viewModel.updateRoleOption(HeroRole.Support, false)

            val state = awaitItem()
            val heroesState = state.heroesState as HeroCollectionState.Success
            val heroNames = heroesState.content.map { it.displayName }
            assertEquals(listOf("Aurora", "Murdock"), heroNames)
            assertTrue(state.selectedRoleFilters.isEmpty())
        }
    }

    // endregion

    // region Eternals

    @Test
    fun `eternals are sorted by display name`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem()

            fakeEternalRepository.emitEternals(
                listOf(
                    eternalDetails(id = "2", displayName = "Zariel"),
                    eternalDetails(id = "1", displayName = "Arden"),
                )
            )

            val eternalsState = awaitItem().eternalsState as EternalCollectionState.Success
            val names = eternalsState.content.map { it.name }
            assertEquals(listOf("Arden", "Zariel"), names)
        }
    }

    @Test
    fun `empty eternals list produces error state`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            assertTrue(awaitItem().eternalsState is EternalCollectionState.Error)
        }
    }

    // endregion

    // region Items

    @Test
    fun `items are sorted by display name`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem()

            fakeItemRepository.emitItems(
                listOf(
                    itemDetails(id = "2", displayName = "Zenith"),
                    itemDetails(id = "1", displayName = "Ashbringer"),
                )
            )

            val itemsState = awaitItem().itemsState as ItemCollectionState.Success
            val names = itemsState.content.map { it.displayName }
            assertEquals(listOf("Ashbringer", "Zenith"), names)
        }
    }

    @Test
    fun `empty items list produces error state`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            assertTrue(awaitItem().itemsState is ItemCollectionState.Error)
        }
    }

    @Test
    fun `selecting hero class filters items by class`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem()

            fakeItemRepository.emitItems(
                listOf(
                    itemDetails(id = "1", displayName = "Mage Rod", heroClass = HeroClass.MAGE),
                    itemDetails(id = "2", displayName = "Tank Shield", heroClass = HeroClass.TANK),
                )
            )
            awaitItem()

            viewModel.onSelectHeroClass(HeroClass.MAGE)

            val itemsState = awaitItem().itemsState as ItemCollectionState.Success
            val names = itemsState.content.map { it.displayName }
            assertEquals(listOf("Mage Rod"), names)
        }
    }

    @Test
    fun `clearing hero class shows all items`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem()

            fakeItemRepository.emitItems(
                listOf(
                    itemDetails(id = "1", displayName = "Mage Rod", heroClass = HeroClass.MAGE),
                    itemDetails(id = "2", displayName = "Tank Shield", heroClass = HeroClass.TANK),
                )
            )
            awaitItem()

            viewModel.onSelectHeroClass(HeroClass.MAGE)
            awaitItem()

            viewModel.onClearHeroClass()

            val state = awaitItem()
            val itemsState = state.itemsState as ItemCollectionState.Success
            assertEquals(2, itemsState.content.size)
            assertNull(state.selectedHeroClass)
        }
    }

    @Test
    fun `selecting tier filters items by rarity`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem()

            fakeItemRepository.emitItems(
                listOf(
                    itemDetails(id = "1", displayName = "Common Sword", rarity = Rarity.COMMON),
                    itemDetails(id = "2", displayName = "Epic Staff", rarity = Rarity.EPIC),
                    itemDetails(id = "3", displayName = "Legendary Axe", rarity = Rarity.LEGENDARY),
                )
            )
            awaitItem()

            viewModel.onSelectTier("Tier I")

            val itemsState = awaitItem().itemsState as ItemCollectionState.Success
            val names = itemsState.content.map { it.displayName }
            assertEquals(listOf("Common Sword"), names)
        }
    }

    @Test
    fun `selecting stat filters items that have all selected stats`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem()

            fakeItemRepository.emitItems(
                listOf(
                    itemDetails(
                        id = "1",
                        displayName = "Power Blade",
                        stats = listOf(
                            ItemDetails.StatDetails(iconId = 0, name = "Physical Power", value = "10"),
                            ItemDetails.StatDetails(iconId = 0, name = "Attack Speed", value = "5"),
                        ),
                    ),
                    itemDetails(
                        id = "2",
                        displayName = "Magic Wand",
                        stats = listOf(
                            ItemDetails.StatDetails(iconId = 0, name = "Magical Power", value = "15"),
                        ),
                    ),
                )
            )
            awaitItem()

            viewModel.onSelectStat("Physical Power")

            val itemsState = awaitItem().itemsState as ItemCollectionState.Success
            val names = itemsState.content.map { it.displayName }
            assertEquals(listOf("Power Blade"), names)
        }
    }

    @Test
    fun `toggling same stat twice removes the filter`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem()

            fakeItemRepository.emitItems(
                listOf(
                    itemDetails(
                        id = "1",
                        displayName = "Power Blade",
                        stats = listOf(
                            ItemDetails.StatDetails(iconId = 0, name = "Physical Power", value = "10"),
                        ),
                    ),
                    itemDetails(id = "2", displayName = "Magic Wand"),
                )
            )
            awaitItem()

            viewModel.onSelectStat("Physical Power")
            awaitItem()

            viewModel.onSelectStat("Physical Power")

            val state = awaitItem()
            assertTrue(state.selectedStatsFilters.isEmpty())
            val itemsState = state.itemsState as ItemCollectionState.Success
            assertEquals(2, itemsState.content.size)
        }
    }

    @Test
    fun `item filters combine - class, tier, and stats`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem()

            fakeItemRepository.emitItems(
                listOf(
                    itemDetails(
                        id = "1",
                        displayName = "Mage Epic Staff",
                        heroClass = HeroClass.MAGE,
                        rarity = Rarity.EPIC,
                        stats = listOf(
                            ItemDetails.StatDetails(iconId = 0, name = "Magical Power", value = "20"),
                        ),
                    ),
                    itemDetails(
                        id = "2",
                        displayName = "Mage Common Wand",
                        heroClass = HeroClass.MAGE,
                        rarity = Rarity.COMMON,
                        stats = listOf(
                            ItemDetails.StatDetails(iconId = 0, name = "Magical Power", value = "5"),
                        ),
                    ),
                    itemDetails(
                        id = "3",
                        displayName = "Tank Epic Shield",
                        heroClass = HeroClass.TANK,
                        rarity = Rarity.EPIC,
                    ),
                )
            )
            awaitItem()

            viewModel.onSelectHeroClass(HeroClass.MAGE)
            awaitItem()

            viewModel.onSelectTier("Tier III")

            viewModel.onSelectStat("Magical Power")

            val itemsState = awaitItem().itemsState as ItemCollectionState.Success
            val names = itemsState.content.map { it.displayName }
            assertEquals(listOf("Mage Epic Staff"), names)
        }
    }

    // endregion

    // region Tab Selection

    @Test
    fun `onSelectTab updates selected tab index`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem()

            viewModel.onSelectTab(2)

            assertEquals(2, awaitItem().selectedTabIndex)
        }
    }

    @Test
    fun `default selected tab index is 0`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            assertEquals(0, awaitItem().selectedTabIndex)
        }
    }

    // endregion

    // region Default Tier Filter

    @Test
    fun `default tier filter is Tier III`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            assertEquals("Tier III", awaitItem().selectedTierFilter)
        }
    }

    // endregion
}
