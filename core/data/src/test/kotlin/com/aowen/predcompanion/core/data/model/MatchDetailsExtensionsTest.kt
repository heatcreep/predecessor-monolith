package com.aowen.predcompanion.core.data.model

import com.aowen.predcompanion.core.model.data.MatchDetails
import org.junit.Assert.assertEquals
import org.junit.Test

class MatchDetailsExtensionsTest {

    private fun player(kills: Int, deaths: Int, assists: Int) =
        MatchDetails.MatchPlayerDetails(
            kills = kills,
            deaths = deaths,
            assists = assists,
            augment = null,
            eternal = null,
            minorBlessing1 = null,
            minorBlessing2 = null,
        )

    @Test
    fun `getKda with normal values`() {
        val result = player(kills = 10, deaths = 5, assists = 10).getKda()
        assertEquals("4", result)
    }

    @Test
    fun `getKda with zero deaths uses 1 as divisor`() {
        val result = player(kills = 8, deaths = 0, assists = 4).getKda()
        assertEquals("12", result)
    }

    @Test
    fun `getKda with all zeros`() {
        val result = player(kills = 0, deaths = 0, assists = 0).getKda()
        assertEquals("0", result)
    }

    @Test
    fun `getKda with fractional result`() {
        val result = player(kills = 7, deaths = 3, assists = 2).getKda()
        assertEquals("3", result)
    }

    @Test
    fun `toDecimal formats float with default pattern`() {
        val result = 3.456f.toDecimal()
        assertEquals("3.5", result)
    }

    @Test
    fun `toDecimal formats with custom pattern`() {
        val result = 3.456f.toDecimal("#.##")
        assertEquals("3.46", result)
    }

    @Test
    fun `toDecimal null float returns zero`() {
        val result = (null as Float?).toDecimal()
        assertEquals("0", result)
    }

    @Test
    fun `toDecimal whole number omits decimal`() {
        val result = 5.0f.toDecimal("#.#")
        assertEquals("5", result)
    }
}
