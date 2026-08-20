package com.aowen.predcompanion.core.ui.model

import com.aowen.predcompanion.ui.theme.Bronze
import com.aowen.predcompanion.ui.theme.Diamond
import com.aowen.predcompanion.ui.theme.Gold
import com.aowen.predcompanion.ui.theme.LightKhaki
import com.aowen.predcompanion.ui.theme.Paragon
import com.aowen.predcompanion.ui.theme.Platinum
import com.aowen.predcompanion.ui.theme.Silver
import org.junit.Assert.assertEquals
import org.junit.Test

class RankDetailsTest {

    // region String.toRankColor

    @Test
    fun `string toRankColor Bronze`() {
        assertEquals(Bronze, "Bronze III".toRankColor())
    }

    @Test
    fun `string toRankColor Silver`() {
        assertEquals(Silver, "Silver II".toRankColor())
    }

    @Test
    fun `string toRankColor Gold`() {
        assertEquals(Gold, "Gold I".toRankColor())
    }

    @Test
    fun `string toRankColor Platinum`() {
        assertEquals(Platinum, "Platinum III".toRankColor())
    }

    @Test
    fun `string toRankColor Diamond`() {
        assertEquals(Diamond, "Diamond II".toRankColor())
    }

    @Test
    fun `string toRankColor Paragon`() {
        assertEquals(Paragon, "Paragon".toRankColor())
    }

    @Test
    fun `string toRankColor unknown defaults to LightKhaki`() {
        assertEquals(LightKhaki, "Placement".toRankColor())
    }

    // endregion

    // region Int?.toRankColor

    @Test
    fun `int toRankColor bronze range`() {
        assertEquals(Bronze, 10.toRankColor())
        assertEquals(Bronze, 11.toRankColor())
        assertEquals(Bronze, 12.toRankColor())
    }

    @Test
    fun `int toRankColor silver range`() {
        assertEquals(Silver, 20.toRankColor())
        assertEquals(Silver, 22.toRankColor())
    }

    @Test
    fun `int toRankColor gold range`() {
        assertEquals(Gold, 30.toRankColor())
        assertEquals(Gold, 32.toRankColor())
    }

    @Test
    fun `int toRankColor platinum range`() {
        assertEquals(Platinum, 40.toRankColor())
    }

    @Test
    fun `int toRankColor diamond range`() {
        assertEquals(Diamond, 50.toRankColor())
    }

    @Test
    fun `int toRankColor paragon`() {
        assertEquals(Paragon, 70.toRankColor())
    }

    @Test
    fun `int toRankColor null defaults to LightKhaki`() {
        assertEquals(LightKhaki, (null as Int?).toRankColor())
    }

    @Test
    fun `int toRankColor unknown value defaults to LightKhaki`() {
        assertEquals(LightKhaki, 99.toRankColor())
    }

    // endregion

    // region Int?.toRankDetails

    @Test
    fun `toRankDetails maps bronze correctly`() {
        assertEquals(RankDetails.BRONZE_III, 10.toRankDetails())
        assertEquals(RankDetails.BRONZE_II, 11.toRankDetails())
        assertEquals(RankDetails.BRONZE_I, 12.toRankDetails())
    }

    @Test
    fun `toRankDetails maps silver correctly`() {
        assertEquals(RankDetails.SILVER_III, 20.toRankDetails())
        assertEquals(RankDetails.SILVER_II, 21.toRankDetails())
        assertEquals(RankDetails.SILVER_I, 22.toRankDetails())
    }

    @Test
    fun `toRankDetails maps gold correctly`() {
        assertEquals(RankDetails.GOLD_III, 30.toRankDetails())
        assertEquals(RankDetails.GOLD_II, 31.toRankDetails())
        assertEquals(RankDetails.GOLD_I, 32.toRankDetails())
    }

    @Test
    fun `toRankDetails maps paragon`() {
        assertEquals(RankDetails.PARAGON, 70.toRankDetails())
    }

    @Test
    fun `toRankDetails null maps to unranked`() {
        assertEquals(RankDetails.UNRANKED, (null as Int?).toRankDetails())
    }

    // endregion

    // region Int?.toRankTitle

    @Test
    fun `toRankTitle maps bronze correctly`() {
        assertEquals("Bronze III", 10.toRankTitle())
        assertEquals("Bronze II", 11.toRankTitle())
        assertEquals("Bronze I", 12.toRankTitle())
    }

    @Test
    fun `toRankTitle maps gold correctly`() {
        assertEquals("Gold III", 30.toRankTitle())
    }

    @Test
    fun `toRankTitle paragon`() {
        assertEquals("Paragon", 70.toRankTitle())
    }

    @Test
    fun `toRankTitle null maps to Placement`() {
        assertEquals("Placement", (null as Int?).toRankTitle())
    }

    @Test
    fun `toRankTitle unknown value maps to Placement`() {
        assertEquals("Placement", 99.toRankTitle())
    }

    // endregion
}
