package com.aowen.predcompanion.core.ui.model.mapper

import com.aowen.predcompanion.core.model.data.HeroBuild
import com.aowen.predcompanion.core.model.data.HeroDetails
import com.aowen.predcompanion.core.model.data.HeroRole
import com.aowen.predcompanion.core.model.data.ItemDetails
import com.aowen.predcompanion.core.testing.fakes.repository.FakeHeroRepository
import com.aowen.predcompanion.core.testing.fakes.repository.FakeItemRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class BuildListItemUiMapperTest {

    private val fakeItemRepository = FakeItemRepository()
    private val fakeHeroRepository = FakeHeroRepository()
    private lateinit var mapper: BuildListItemUiMapper

    private val testItems = listOf(
        ItemDetails(id = "5", displayName = "Test Crest", imageSrc = "crest.png"),
        ItemDetails(id = "1", displayName = "Sword", imageSrc = "sword.png"),
        ItemDetails(id = "2", displayName = "Shield", imageSrc = "shield.png"),
    )

    @Before
    fun setUp() {
        fakeItemRepository.emitItems(testItems)
        fakeHeroRepository.emitHeroes(
            listOf(HeroDetails(id = "42", name = "TestHero", imageUrl = "hero.png"))
        )
        mapper = BuildListItemUiMapper(fakeItemRepository, fakeHeroRepository)
    }

    @Test
    fun `buildFrom HeroBuild maps basic fields`() {
        val build = HeroBuild(
            id = "b1",
            userId = "u1",
            title = "My Build",
            author = "Author",
            role = "carry",
            description = "A build",
            heroId = 42L,
            crestId = 5,
            buildItemIds = listOf(1, 2),
            fiveStarScore = "4.5",
            createdAt = "2024-01-01",
            version = "1.0",
        )

        val result = mapper.buildFrom(build)

        assertEquals("b1", result.buildId)
        assertEquals("u1", result.userId)
        assertEquals("My Build", result.title)
        assertEquals("Author", result.author)
        assertEquals(HeroRole.Carry, result.role)
        assertEquals("A build", result.description)
        assertEquals(42L, result.heroId)
        assertEquals("hero.png", result.heroImageUrl)
        assertEquals("4.5", result.fiveStarScore)
        assertEquals("2024-01-01", result.createdAt)
        assertEquals("1.0", result.version)
    }

    @Test
    fun `buildFrom maps crest from item repository`() {
        val build = HeroBuild(id = "b1", crestId = 5)

        val result = mapper.buildFrom(build)

        assertEquals("Test Crest", result.crest.displayName)
    }

    @Test
    fun `buildFrom maps build items from item repository`() {
        val build = HeroBuild(id = "b1", buildItemIds = listOf(1, 2))

        val result = mapper.buildFrom(build)

        assertEquals(2, result.buildItems.size)
        assertEquals("Sword", result.buildItems[0].displayName)
        assertEquals("Shield", result.buildItems[1].displayName)
    }

    @Test
    fun `buildFrom skips unknown item ids`() {
        val build = HeroBuild(id = "b1", buildItemIds = listOf(1, 999))

        val result = mapper.buildFrom(build)

        assertEquals(1, result.buildItems.size)
        assertEquals("Sword", result.buildItems[0].displayName)
    }

    @Test
    fun `buildFrom with unknown role returns null`() {
        val build = HeroBuild(id = "b1", role = "nonexistent")

        val result = mapper.buildFrom(build)

        assertNull(result.role)
    }

    @Test
    fun `buildFrom maps modules with items`() {
        val build = HeroBuild(
            id = "b1",
            modules = listOf(
                HeroBuild.ItemModule(
                    id = "m1",
                    title = "Module 1",
                    itemIds = listOf(1, 2),
                )
            )
        )

        val result = mapper.buildFrom(build)

        assertEquals(1, result.modules.size)
        assertEquals("Module 1", result.modules[0].title)
        assertEquals(2, result.modules[0].items.size)
    }

    @Test
    fun `buildFrom with unknown crest returns empty ItemDetails`() {
        val build = HeroBuild(id = "b1", crestId = 999)

        val result = mapper.buildFrom(build)

        assertEquals(ItemDetails(), result.crest)
    }
}
