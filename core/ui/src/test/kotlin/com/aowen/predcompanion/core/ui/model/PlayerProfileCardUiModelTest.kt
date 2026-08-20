package com.aowen.predcompanion.core.ui.model

import com.aowen.predcompanion.core.model.data.HeroDetails
import com.aowen.predcompanion.core.model.data.Player
import com.aowen.predcompanion.core.model.data.PlayerHeroStatistics
import org.junit.Assert.assertEquals
import org.junit.Test

class PlayerProfileCardUiModelTest {

    @Test
    fun `toPlayerProfileCardUiModel maps all fields`() {
        val player = Player(
            id = "p1",
            name = "TestPlayer",
            winRate = "55.0%",
            rankIconUrl = "https://example.com/rank.png",
            currentRankTitle = "Gold I",
            currentRankPoints = "1500",
            favoriteRole = "carry",
            heroStatistics = emptyList(),
            favoriteHero = HeroDetails(
                id = "1",
                displayName = "Dekker",
                imageUrl = "https://example.com/dekker.png"
            ),
        )

        val result = player.toPlayerProfileCardUiModel()

        assertEquals("TestPlayer", result.playerName)
        assertEquals("https://example.com/rank.png", result.rankIconUrl)
        assertEquals("1500", result.rankPoints)
        assertEquals("Gold I", result.rankTitle)
        assertEquals("55.0%", result.winPercentage)
        assertEquals("NA", result.region)
        assertEquals("https://example.com/dekker.png", result.favoriteHeroIconUrl)
    }

    @Test
    fun `toPlayerProfileCardUiModel with null favoriteHero uses empty string`() {
        val player = Player(
            id = "p1",
            name = "TestPlayer",
            winRate = "50%",
            rankIconUrl = "",
            currentRankTitle = "Silver",
            currentRankPoints = "500",
            favoriteRole = "support",
            heroStatistics = emptyList(),
            favoriteHero = null,
        )

        val result = player.toPlayerProfileCardUiModel()
        assertEquals("", result.favoriteHeroIconUrl)
    }

    @Test
    fun `toHeroStatisticsUiModel maps fields correctly`() {
        val stat = PlayerHeroStatistics(
            heroId = "hero-1",
            heroName = "Grux",
            matchCount = 20,
            matchesWon = 12,
            totalKills = 100,
            totalDeaths = 40,
            totalAssists = 60,
        )

        val result = stat.toHeroStatisticsUiModel()

        assertEquals("hero-1", result.id)
        assertEquals("Grux", result.name)
        assertEquals("20", result.totalMatches)
        assertEquals("60.0%", result.winRate)
        assertEquals("5.0", result.averageKills)
        assertEquals("2.0", result.averageDeaths)
        assertEquals("3.0", result.averageAssists)
    }

    @Test
    fun `toHeroStatisticsUiModel kda with zero deaths uses 1`() {
        val stat = PlayerHeroStatistics(
            heroId = "hero-1",
            heroName = "Murdock",
            matchCount = 10,
            matchesWon = 8,
            totalKills = 50,
            totalDeaths = 0,
            totalAssists = 30,
        )

        val result = stat.toHeroStatisticsUiModel()
        assertEquals("80", result.kdaText)
    }
}
