package com.aowen.predcompanion.core.ui.model.mapper

import com.aowen.predcompanion.core.model.data.PlayerInfo
import org.junit.Assert.assertEquals
import org.junit.Test

class PlayerProfileUiMapperTest {

    private val mapper = PlayerProfileUiMapper()

    private val testDetails = PlayerInfo.PlayerDetails(
        playerId = "p1",
        playerName = "TestPlayer",
        vpCurrent = 1500,
    )

    private val testStats = PlayerInfo.PlayerStats(
        winRate = "55.0%",
        matchesPlayed = "100",
        favoriteHero = PlayerInfo.PlayerStats.FavoriteHero(
            name = "Dekker",
            imageUrl = "dekker.png"
        ),
        favoriteRole = "Support",
        averageKda = listOf("5.2", "3.1", "8.4"),
        averagePerformanceScore = "72.5",
    )

    @Test
    fun `buildFrom maps player details`() {
        val result = mapper.buildFrom(testDetails, testStats)

        assertEquals("TestPlayer", result.playerDetailsUi.playerName)
        assertEquals("1500", result.playerDetailsUi.currentVp)
    }

    @Test
    fun `buildFrom maps stats lines correctly`() {
        val result = mapper.buildFrom(testDetails, testStats)

        assertEquals(6, result.statsUi.size)

        val winRate = result.statsUi[0] as StatLine.SingleStatLine
        assertEquals("Win Rate", winRate.label)
        assertEquals("55.0%", winRate.value)

        val matchesPlayed = result.statsUi[1] as StatLine.SingleStatLine
        assertEquals("Matches Played", matchesPlayed.label)
        assertEquals("100", matchesPlayed.value)

        val favHero = result.statsUi[2] as StatLine.SingleStatLine
        assertEquals("Favorite Hero", favHero.label)
        assertEquals("Dekker", favHero.value)

        val favRole = result.statsUi[3] as StatLine.SingleStatLine
        assertEquals("Favorite Role", favRole.label)
        assertEquals("Support", favRole.value)

        val avgKda = result.statsUi[4] as StatLine.MultiStatLine
        assertEquals("Avg. KDA", avgKda.label)
        assertEquals(listOf("5.2", "3.1", "8.4"), avgKda.values)

        val perfScore = result.statsUi[5] as StatLine.SingleStatLine
        assertEquals("Avg. Performance Score", perfScore.label)
        assertEquals("72.5", perfScore.value)
    }

    @Test
    fun `buildFrom with null favorite hero shows empty string`() {
        val stats = testStats.copy(favoriteHero = null)

        val result = mapper.buildFrom(testDetails, stats)

        val favHero = result.statsUi[2] as StatLine.SingleStatLine
        assertEquals("", favHero.value)
    }
}
