package com.aowen.predcompanion.core.data.model

import com.aowen.predcompanion.core.model.data.HeroBuild
import org.junit.Assert.assertEquals
import org.junit.Test

class HeroBuildMappersTest {

    private val testBuild = HeroBuild(
        id = "build-1",
        userId = "user-1",
        title = "Carry Build",
        author = "TestAuthor",
        role = "carry",
        description = "A great carry build",
        heroId = 42L,
        crestId = 5,
        buildItemIds = listOf(1, 2, 3),
        skillOrder = listOf(1, 2, 3, 1),
        fiveStarScore = "4.5",
        createdAt = "2024-01-01",
        updatedAt = "2024-06-01",
        version = "1.2.0",
    )

    @Test
    fun `asFavoriteBuildListItem maps all fields correctly`() {
        val result = testBuild.asFavoriteBuildListItem()

        assertEquals("build-1", result.buildId)
        assertEquals(42L, result.heroId)
        assertEquals("carry", result.role)
        assertEquals("Carry Build", result.title)
        assertEquals("A great carry build", result.description)
        assertEquals("TestAuthor", result.author)
        assertEquals(5, result.crestId)
        assertEquals(listOf(1, 2, 3), result.itemIds)
        assertEquals(0, result.upvotesCount)
        assertEquals(0, result.downvotesCount)
        assertEquals("2024-01-01", result.createdAt)
        assertEquals("1.2.0", result.gameVersion)
    }

    @Test
    fun `asFavoriteBuildListItem with null version sets empty gameVersion`() {
        val build = testBuild.copy(version = null)

        val result = build.asFavoriteBuildListItem()

        assertEquals("", result.gameVersion)
    }

    @Test
    fun `asFavoriteBuildListEntity maps all fields correctly`() {
        val result = testBuild.asFavoriteBuildListEntity()

        assertEquals("build-1", result.buildId)
        assertEquals(42L, result.heroId)
        assertEquals("carry", result.role)
        assertEquals("Carry Build", result.title)
        assertEquals("A great carry build", result.description)
        assertEquals("TestAuthor", result.author)
        assertEquals(5, result.crestId)
        assertEquals(listOf(1, 2, 3), result.itemIds)
        assertEquals(0, result.upvotesCount)
        assertEquals(0, result.downvotesCount)
        assertEquals("2024-01-01", result.createdAt)
        assertEquals("1.2.0", result.gameVersion)
    }

    @Test
    fun `asFavoriteBuildListEntity round trips through asFavoriteBuildListItem`() {
        val entity = testBuild.asFavoriteBuildListEntity()
        val item = entity.asFavoriteBuildListItem()

        assertEquals(entity.buildId, item.buildId)
        assertEquals(entity.heroId, item.heroId)
        assertEquals(entity.role, item.role)
        assertEquals(entity.title, item.title)
        assertEquals(entity.description, item.description)
        assertEquals(entity.author, item.author)
        assertEquals(entity.crestId, item.crestId)
        assertEquals(entity.itemIds, item.itemIds)
    }
}
