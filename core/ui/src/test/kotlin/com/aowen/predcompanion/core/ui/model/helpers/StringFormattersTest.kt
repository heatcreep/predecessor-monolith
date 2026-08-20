package com.aowen.predcompanion.core.ui.model.helpers

import org.junit.Assert.assertEquals
import org.junit.Test

class StringFormattersTest {

    @Test
    fun `toPercentageString with default decimals`() {
        assertEquals("55%", 0.55f.toPercentageString())
    }

    @Test
    fun `toPercentageString with one decimal`() {
        assertEquals("55.5%", 0.555f.toPercentageString(1))
    }

    @Test
    fun `toPercentageString with two decimals`() {
        assertEquals("55.56%", 0.5556f.toPercentageString(2))
    }

    @Test
    fun `toPercentageString zero`() {
        assertEquals("0%", 0.0f.toPercentageString())
    }

    @Test
    fun `toPercentageString full 100 percent`() {
        assertEquals("100%", 1.0f.toPercentageString())
    }
}
