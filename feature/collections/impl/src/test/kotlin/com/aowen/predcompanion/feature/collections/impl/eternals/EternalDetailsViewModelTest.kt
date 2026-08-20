package com.aowen.predcompanion.feature.collections.impl.eternals

import app.cash.turbine.test
import com.aowen.predcompanion.core.model.data.BlessingDetails
import com.aowen.predcompanion.core.model.data.EternalCategory
import com.aowen.predcompanion.core.model.data.EternalDetails
import com.aowen.predcompanion.core.testing.fakes.repository.FakeEternalRepository
import com.aowen.predcompanion.core.testing.util.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class EternalDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val fakeEternalRepository = FakeEternalRepository()

    private fun createViewModel(eternalId: String) = EternalDetailsViewModel(
        eternalId = eternalId,
        eternalRepository = fakeEternalRepository,
    )

    private val testCategory = EternalCategory(
        id = "cat1",
        colorString = "#ce66a0",
        categoryDescription = "Masters of chaos",
        categoryType = EternalCategory.CategoryType.ANOMALIES,
        displayName = "Anomalies",
    )

    private val testEternal = EternalDetails(
        id = "eternal1",
        displayName = "Zephyrus",
        description = "Wind eternal",
        icon = "zephyrus.png",
        eternalCategory = testCategory,
        firstMinorBlessing = listOf(
            BlessingDetails(
                id = "b1",
                displayName = "Swift Wind",
                description = "Increases speed",
                icon = "swift.png",
            )
        ),
        secondMinorBlessing = emptyList(),
    )

    @Test
    fun `when eternal exists, state is loaded`() = runTest {
        fakeEternalRepository.emitEternals(listOf(testEternal))

        val viewModel = createViewModel("eternal1")

        viewModel.uiState.test {
            val loaded = awaitItem() as EternalDetailsUiState.Loaded
            assertEquals("Zephyrus", loaded.content.displayName)
            assertEquals("Anomalies", loaded.content.eternalCategory)
            assertEquals("Wind eternal", loaded.content.eternalAbilityDescription)
            assertEquals("Masters of chaos", loaded.content.eternalDescription)
            assertEquals(1, loaded.content.firstMinorBlessing.size)
            assertEquals("Swift Wind", loaded.content.firstMinorBlessing.first().displayName)
        }
    }

    @Test
    fun `when eternal does not exist, state is error`() = runTest {
        val viewModel = createViewModel("nonexistent")

        viewModel.uiState.test {
            assertTrue(awaitItem() is EternalDetailsUiState.Error)
        }
    }

    @Test
    fun `reloadDetails refreshes state`() = runTest {
        val viewModel = createViewModel("eternal1")

        viewModel.uiState.test {
            assertTrue(awaitItem() is EternalDetailsUiState.Error)

            fakeEternalRepository.emitEternals(listOf(testEternal))
            viewModel.reloadDetails()

            val loaded = awaitItem() as EternalDetailsUiState.Loaded
            assertEquals("Zephyrus", loaded.content.displayName)
        }
    }
}
