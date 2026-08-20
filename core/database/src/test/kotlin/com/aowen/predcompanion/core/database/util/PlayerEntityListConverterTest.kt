package com.aowen.predcompanion.core.database.util

import com.aowen.predcompanion.core.database.model.PlayerEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class PlayerEntityListConverterTest {

    private val converter = PlayerEntityListConverter()

    private val testPlayer = PlayerEntity(
        playerId = "p1",
        playerName = "TestPlayer",
        favoriteRole = "carry",
        winRate = "55%",
        rankIconUrl = "rank.png",
        currentRankTitle = "Gold I",
        currentRankPoints = "1500",
        favoriteHero = null,
    )

    @Test
    fun `round trip single player`() {
        val original = listOf(testPlayer)
        val json = converter.fromPlayerEntityList(original)
        val result = converter.toPlayerEntityList(json)
        assertEquals(original, result)
    }

    @Test
    fun `round trip multiple players`() {
        val second = testPlayer.copy(playerId = "p2", playerName = "Player2")
        val original = listOf(testPlayer, second)
        val json = converter.fromPlayerEntityList(original)
        val result = converter.toPlayerEntityList(json)
        assertEquals(2, result.size)
        assertEquals("p1", result[0].playerId)
        assertEquals("p2", result[1].playerId)
    }

    @Test
    fun `round trip empty list`() {
        val original = emptyList<PlayerEntity>()
        val json = converter.fromPlayerEntityList(original)
        val result = converter.toPlayerEntityList(json)
        assertEquals(emptyList<PlayerEntity>(), result)
    }

    @Test
    fun `fromPlayerEntityList produces valid json`() {
        val json = converter.fromPlayerEntityList(listOf(testPlayer))
        assert(json.startsWith("["))
        assert(json.contains("\"playerId\":\"p1\""))
    }
}
