package com.aowen.predcompanion.feature.collections.impl.ui.data

import com.aowen.predcompanion.core.model.data.BlessingDetails
import com.aowen.predcompanion.core.model.data.EternalCategory
import com.aowen.predcompanion.core.model.data.EternalCategory.CategoryType
import com.aowen.predcompanion.core.model.data.EternalDetails
import org.junit.Assert.assertEquals
import org.junit.Test

class EternalDetailsUiModelMapperTest {

    private fun eternalWithCategory(type: CategoryType) = EternalDetails(
        id = "e1",
        displayName = "TestEternal",
        description = "Ability desc",
        icon = "test_icon",
        eternalCategory = EternalCategory(
            id = "cat1",
            colorString = "#000000",
            categoryDescription = "Category desc",
            categoryType = type,
            displayName = "CategoryName",
        ),
    )

    @Test
    fun `toDetailsUi maps all fields correctly`() {
        val eternal = EternalDetails(
            id = "e1",
            displayName = "Zephyrus",
            description = "Wind ability",
            icon = "zephyrus_icon",
            eternalCategory = EternalCategory(
                id = "cat1",
                colorString = "#ce66a0",
                categoryDescription = "Masters of chaos",
                categoryType = CategoryType.ANOMALIES,
                displayName = "Anomalies",
            ),
            firstMinorBlessing = listOf(
                BlessingDetails(id = "b1", displayName = "Swift", description = "Fast", icon = "swift_icon"),
            ),
            secondMinorBlessing = listOf(
                BlessingDetails(id = "b2", displayName = "Guard", description = "Shield", icon = "guard_icon"),
            ),
        )

        val result = eternal.toDetailsUi()

        assertEquals("Zephyrus", result.displayName)
        assertEquals("https://pred.gg/assets/zephyrus_icon.webp", result.imageSrc)
        assertEquals("Anomalies", result.eternalCategory)
        assertEquals("Masters of chaos", result.eternalDescription)
        assertEquals("Wind ability", result.eternalAbilityDescription)
        assertEquals(1, result.firstMinorBlessing.size)
        assertEquals("Swift", result.firstMinorBlessing.first().displayName)
        assertEquals(1, result.secondMinorBlessing.size)
        assertEquals("Guard", result.secondMinorBlessing.first().displayName)
    }

    @Test
    fun `toDetailsUi maps ANOMALIES color`() {
        val result = eternalWithCategory(CategoryType.ANOMALIES).toDetailsUi()
        assertEquals("#ce66a0", result.categoryColorString)
    }

    @Test
    fun `toDetailsUi maps DIVINES color`() {
        val result = eternalWithCategory(CategoryType.DIVINES).toDetailsUi()
        assertEquals("#907fd4", result.categoryColorString)
    }

    @Test
    fun `toDetailsUi maps DREADNOUGHTS color`() {
        val result = eternalWithCategory(CategoryType.DREADNOUGHTS).toDetailsUi()
        assertEquals("#2f9e88", result.categoryColorString)
    }

    @Test
    fun `toDetailsUi maps HARBINGERS color`() {
        val result = eternalWithCategory(CategoryType.HARBINGERS).toDetailsUi()
        assertEquals("#2f9e88", result.categoryColorString)
    }

    @Test
    fun `toDetailsUi maps PRIMARCHS color`() {
        val result = eternalWithCategory(CategoryType.PRIMARCHS).toDetailsUi()
        assertEquals("#ad9643", result.categoryColorString)
    }

    @Test
    fun `toDetailsUi maps SOVEREIGNS color`() {
        val result = eternalWithCategory(CategoryType.SOVEREIGNS).toDetailsUi()
        assertEquals("#319ea7", result.categoryColorString)
    }

    @Test
    fun `toDetailsUi maps UNKNOWN color to empty string`() {
        val result = eternalWithCategory(CategoryType.UNKNOWN).toDetailsUi()
        assertEquals("", result.categoryColorString)
    }

    @Test
    fun `toDetailsUi with empty blessings produces empty lists`() {
        val eternal = eternalWithCategory(CategoryType.ANOMALIES)

        val result = eternal.toDetailsUi()

        assertEquals(emptyList<Any>(), result.firstMinorBlessing)
        assertEquals(emptyList<Any>(), result.secondMinorBlessing)
    }
}
