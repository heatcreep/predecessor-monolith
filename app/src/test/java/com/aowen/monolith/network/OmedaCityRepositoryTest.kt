package com.aowen.monolith.network

import com.aowen.monolith.data.AbilityDetails
import com.aowen.monolith.data.HeroClass
import com.aowen.monolith.data.HeroDetails
import com.aowen.monolith.data.HeroRole
import com.aowen.monolith.data.PlayerInfo
import com.aowen.monolith.data.create
import com.aowen.monolith.fakes.FakeOmedaCityService
import com.aowen.monolith.fakes.data.defaultHeroBaseStats
import com.aowen.monolith.fakes.data.fakeBuildDto
import com.aowen.monolith.fakes.data.fakeHeroDto
import com.aowen.monolith.fakes.data.fakeHeroStatisticsDto
import com.aowen.monolith.fakes.data.fakeItemDto
import com.aowen.monolith.fakes.data.fakeMatchDto
import com.aowen.monolith.fakes.data.fakePlayerDto
import com.aowen.monolith.fakes.data.fakePlayerHeroStatsDto
import com.aowen.monolith.fakes.data.fakePlayerStatsDto
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class OmedaCityRepositoryTest {

    private lateinit var omedaCityRepository: OmedaCityRepository

    @Before
    fun setUp() {
        omedaCityRepository = OmedaCityRepositoryImpl(
            playerApiService = FakeOmedaCityService(200)
        )
    }

    // fetchPlayerById
    @Test
    fun `fetchPlayerById - successful response returns a PlayerDetails`() = runTest {
        val actual = omedaCityRepository.fetchPlayerInfo("123").getOrNull()
        val expected = PlayerInfo(
            playerDetails = fakePlayerDto.create(),
            playerStats = fakePlayerStatsDto.create()
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `fetchPlayerById - non-successful response returns exception with message`() = runTest {
        omedaCityRepository = OmedaCityRepositoryImpl(
            playerApiService = FakeOmedaCityService(404)
        )
        val actual = omedaCityRepository.fetchPlayerInfo("123").exceptionOrNull()
        val expected = "Failed to fetch player info"
        assertTrue(actual is Exception)
        assertEquals(expected, actual?.message)
    }

    @Test
    fun `fetchPlayerById - thrown exception returns failure with message`() = runTest {
        omedaCityRepository = OmedaCityRepositoryImpl(
            playerApiService = FakeOmedaCityService()
        )
        val actual = omedaCityRepository.fetchPlayerInfo("123").exceptionOrNull()
        val expected = "Something went wrong"
        assertTrue(actual is Exception)
        assertEquals(expected, actual?.message)
    }

    // fetchAllPlayerHeroStats

    @Test
    fun `fetchAllPlayerHeroStats - successful response returns a list of PlayerHeroStats`() =
        runTest {
            val actual = omedaCityRepository.fetchAllPlayerHeroStats("123").getOrNull()
            val expected = listOf(
                fakePlayerHeroStatsDto.create()
            )
            assertEquals(expected, actual)
        }

    @Test
    fun `fetchAllPlayerHeroStats - non-successful response returns exception with message`() =
        runTest {
            omedaCityRepository = OmedaCityRepositoryImpl(
                playerApiService = FakeOmedaCityService(404)
            )
            val actual = omedaCityRepository.fetchAllPlayerHeroStats("123").exceptionOrNull()
            val expected = "Failed to fetch player hero stats"
            assertTrue(actual is Exception)
            assertEquals(expected, actual?.message)
        }

    @Test
    fun `fetchAllPlayerHeroStats - thrown exception returns failure with message`() = runTest {
        omedaCityRepository = OmedaCityRepositoryImpl(
            playerApiService = FakeOmedaCityService()
        )
        val actual = omedaCityRepository.fetchAllPlayerHeroStats("123").exceptionOrNull()
        val expected = "Something went wrong"
        assertTrue(actual is Exception)
        assertEquals(expected, actual?.message)
    }

    // fetchMatchesById

    @Test
    fun `fetchMatchesById - successful response returns a list of MatchDetails`() = runTest {
        val actual = omedaCityRepository.fetchMatchesById("123").getOrNull()?.matches
        val expected = listOf(
            fakeMatchDto.create()
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `fetchMatchesById - non-successful response returns exception with message`() = runTest {
        omedaCityRepository = OmedaCityRepositoryImpl(
            playerApiService = FakeOmedaCityService(404)
        )
        val actual = omedaCityRepository.fetchMatchesById("123").exceptionOrNull()
        val expected = "Failed to fetch matches: Code 404"
        assertTrue(actual is Exception)
        assertEquals(expected, actual?.message)
    }

    @Test
    fun `fetchMatchesById - thrown exception returns failure with message`() = runTest {
        omedaCityRepository = OmedaCityRepositoryImpl(
            playerApiService = FakeOmedaCityService()
        )
        val actual = omedaCityRepository.fetchMatchesById("123").exceptionOrNull()
        val expected = "Something went wrong"
        assertTrue(actual is Exception)
        assertEquals(expected, actual?.message)
    }

    // fetchMatchById

    @Test
    fun `fetchMatchById - successful response returns a MatchDetails`() = runTest {
        val actual = omedaCityRepository.fetchMatchById("123").getOrNull()
        val expected = fakeMatchDto.create()
        assertEquals(expected, actual)
    }

    @Test
    fun `fetchMatchById - non-successful response returns exception with message`() = runTest {
        omedaCityRepository = OmedaCityRepositoryImpl(
            playerApiService = FakeOmedaCityService(404)
        )
        val actual = omedaCityRepository.fetchMatchById("Error").exceptionOrNull()
        val expected = "Failed to fetch match"
        assertTrue(actual is Exception)
        assertEquals(expected, actual?.message)
    }

    @Test
    fun `fetchMatchById - thrown exception returns failure with message`() = runTest {
        omedaCityRepository = OmedaCityRepositoryImpl(
            playerApiService = FakeOmedaCityService()
        )
        val actual = omedaCityRepository.fetchMatchById("123").exceptionOrNull()
        val expected = "Something went wrong"
        assertTrue(actual is Exception)
        assertEquals(expected, actual?.message)
    }

    // fetchPlayersByName

    @Test
    fun `fetchPlayersByName - successful response returns a list of PlayerDetails`() = runTest {
        val actual = omedaCityRepository.fetchPlayersByName("test").getOrNull()
        val expected = listOf(
            fakePlayerDto.create()
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `fetchPlayersByName - non-successful response returns exception with message`() = runTest {
        omedaCityRepository = OmedaCityRepositoryImpl(
            playerApiService = FakeOmedaCityService(404)
        )
        val actual = omedaCityRepository.fetchPlayersByName("test").exceptionOrNull()
        val expected = "Failed to fetch players"
        assertTrue(actual is Exception)
        assertEquals(expected, actual?.message)
    }

    @Test
    fun `fetchPlayersByName - thrown exception returns failure with message`() = runTest {
        omedaCityRepository = OmedaCityRepositoryImpl(
            playerApiService = FakeOmedaCityService()
        )
        val actual = omedaCityRepository.fetchPlayersByName("test").exceptionOrNull()
        val expected = "Something went wrong"
        assertTrue(actual is Exception)
        assertEquals(expected, actual?.message)
    }

    // fetchAllHeroes

    @Test
    fun `fetchAllHeroes - successful response returns a list of HeroDetails`() = runTest {
        val actual = omedaCityRepository.fetchAllHeroes().getOrNull()
        val expected = listOf(
            HeroDetails(
                id = 123,
                name = "test",
                displayName = "Test",
                stats = listOf(3, 4, 5, 6),
                classes = listOf(HeroClass.Assassin, HeroClass.Fighter),
                roles = listOf(HeroRole.Offlane, HeroRole.Jungle),
                abilities = listOf(
                    AbilityDetails(
                        displayName = "ability",
                        image = "https://omeda.city/test",
                        gameDescription = "gameDescription",
                        menuDescription = "menuDescription",
                        cooldown = listOf(1.2f, 3.4f, 5.6f),
                        cost = listOf(1.0f, 2.0f, 3.0f)
                    )
                ),
                baseStats = defaultHeroBaseStats
            )
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `fetchAllHeroes - non-successful response returns exception with message`() = runTest {
        omedaCityRepository = OmedaCityRepositoryImpl(
            playerApiService = FakeOmedaCityService(404)
        )
        val actual = omedaCityRepository.fetchAllHeroes().exceptionOrNull()
        val expected = "Failed to fetch heroes"
        assertTrue(actual is Exception)
        assertEquals(expected, actual?.message)
    }

    @Test
    fun `fetchAllHeroes - thrown exception returns failure with message`() = runTest {
        omedaCityRepository = OmedaCityRepositoryImpl(
            playerApiService = FakeOmedaCityService()
        )
        val actual = omedaCityRepository.fetchAllHeroes().exceptionOrNull()
        val expected = "Something went wrong"
        assertTrue(actual is Exception)
        assertEquals(expected, actual?.message)
    }

    // fetchAllItems

    @Test
    fun `fetchAllItems - successful response returns a list of ItemDetails`() = runTest {
        val actual = omedaCityRepository.fetchAllItems().getOrNull()
        val expected = listOf(
            fakeItemDto.create()
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `fetchAllItems - non-successful response returns exception with message`() = runTest {
        omedaCityRepository = OmedaCityRepositoryImpl(
            playerApiService = FakeOmedaCityService(404)
        )
        val actual = omedaCityRepository.fetchAllItems().exceptionOrNull()
        val expected = "Failed to fetch items"
        assertTrue(actual is Exception)
        assertEquals(expected, actual?.message)
    }

    @Test
    fun `fetchAllItems - thrown exception returns failure with message`() = runTest {
        omedaCityRepository = OmedaCityRepositoryImpl(
            playerApiService = FakeOmedaCityService()
        )
        val actual = omedaCityRepository.fetchAllItems().exceptionOrNull()
        val expected = "Something went wrong"
        assertTrue(actual is Exception)
        assertEquals(expected, actual?.message)
    }

    // fetchItemByName

    @Test
    fun `fetchItemByName - successful response returns an ItemDetails`() = runTest {
        val actual = omedaCityRepository.fetchItemByName("test").getOrNull()
        val expected = fakeItemDto.create()
        assertEquals(expected, actual)
    }

    @Test
    fun `fetchItemByName - non-successful response returns exception with message`() = runTest {
        omedaCityRepository = OmedaCityRepositoryImpl(
            playerApiService = FakeOmedaCityService(404)
        )
        val actual = omedaCityRepository.fetchItemByName("test").exceptionOrNull()
        val expected = "Failed to fetch item"
        assertTrue(actual is Exception)
        assertEquals(expected, actual?.message)
    }

    @Test
    fun `fetchItemByName - thrown exception returns failure with message`() = runTest {
        omedaCityRepository = OmedaCityRepositoryImpl(
            playerApiService = FakeOmedaCityService()
        )
        val actual = omedaCityRepository.fetchItemByName("test").exceptionOrNull()
        val expected = "Something went wrong"
        assertTrue(actual is Exception)
        assertEquals(expected, actual?.message)
    }

    // fetchHeroByName

    @Test
    fun `fetchHeroByName - successful response returns a HeroDetails`() = runTest {
        val actual = omedaCityRepository.fetchHeroByName("test").getOrNull()
        val expected = fakeHeroDto.create()
        assertEquals(expected, actual)
    }

    @Test
    fun `fetchHeroByName - non-successful response returns exception with message`() = runTest {
        omedaCityRepository = OmedaCityRepositoryImpl(
            playerApiService = FakeOmedaCityService(404)
        )
        val actual = omedaCityRepository.fetchHeroByName("test").exceptionOrNull()
        val expected = "Failed to fetch hero"
        assertTrue(actual is Exception)
        assertEquals(expected, actual?.message)
    }

    @Test
    fun `fetchHeroByName - thrown exception returns failure with message`() = runTest {
        omedaCityRepository = OmedaCityRepositoryImpl(
            playerApiService = FakeOmedaCityService()
        )
        val actual = omedaCityRepository.fetchHeroByName("test").exceptionOrNull()
        val expected = "Something went wrong"
        assertTrue(actual is Exception)
        assertEquals(expected, actual?.message)
    }

    @Test
    fun `fetchAllAllHeroes - successful response returns a list of HeroDetails`() = runTest {
        val actual = omedaCityRepository.fetchAllHeroes().getOrNull()
        val expected = listOf(
            fakeHeroDto.create()
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `fetchAllAllHeroes - non-successful response returns exception with message`() = runTest {
        omedaCityRepository = OmedaCityRepositoryImpl(
            playerApiService = FakeOmedaCityService(404)
        )
        val actual = omedaCityRepository.fetchAllHeroes().exceptionOrNull()
        val expected = "Failed to fetch heroes"
        assertTrue(actual is Exception)
        assertEquals(expected, actual?.message)
    }

    @Test
    fun `fetchAllAllHeroes - thrown exception returns failure with message`() = runTest {
        omedaCityRepository = OmedaCityRepositoryImpl(
            playerApiService = FakeOmedaCityService()
        )
        val actual = omedaCityRepository.fetchAllHeroes().exceptionOrNull()
        val expected = "Something went wrong"
        assertTrue(actual is Exception)
        assertEquals(expected, actual?.message)
    }

    // fetchHeroStatisticsById

    @Test
    fun `fetchHeroStatisticsById - successful response returns a HeroStatistics`() = runTest {
        val actual = omedaCityRepository.fetchHeroStatisticsById("123").getOrNull()
        val expected = fakeHeroStatisticsDto.create()
        assertEquals(expected, actual)
    }

    @Test
    fun `fetchHeroStatisticsById - non-successful response returns exception with message`() =
        runTest {
            omedaCityRepository = OmedaCityRepositoryImpl(
                playerApiService = FakeOmedaCityService(404)
            )
            val actual = omedaCityRepository.fetchHeroStatisticsById("123").exceptionOrNull()
            val expected = "Failed to fetch hero statistics"
            assertTrue(actual is Exception)
            assertEquals(expected, actual?.message)
        }

    @Test
    fun `fetchHeroStatisticsById - thrown exception returns failure with message`() = runTest {
        omedaCityRepository = OmedaCityRepositoryImpl(
            playerApiService = FakeOmedaCityService()
        )
        val actual = omedaCityRepository.fetchHeroStatisticsById("123").exceptionOrNull()
        val expected = "Something went wrong"
        assertTrue(actual is Exception)
        assertEquals(expected, actual?.message)
    }

    // fetchAllBuilds
    @Test
    fun `fetchAllBuilds - successful response returns a list of BuildListItem`() = runTest {
        val actual = omedaCityRepository.fetchAllBuilds().getOrNull()
        val expected = listOf(
            fakeBuildDto.create()
        )
        assertEquals(expected.size, actual?.size)
    }

    @Test
    fun `fetchAllBuilds - non-successful response returns exception with message`() = runTest {
        omedaCityRepository = OmedaCityRepositoryImpl(
            playerApiService = FakeOmedaCityService(404)
        )
        val actual = omedaCityRepository.fetchAllBuilds().exceptionOrNull()
        val expected = "Failed to fetch builds"
        assertTrue(actual is Exception)
        assertEquals(expected, actual?.message)
    }

    @Test
    fun `fetchAllBuilds - thrown exception returns failure with message`() = runTest {
        omedaCityRepository = OmedaCityRepositoryImpl(
            playerApiService = FakeOmedaCityService()
        )
        val actual = omedaCityRepository.fetchAllBuilds().exceptionOrNull()
        val expected = "Something went wrong"
        assertTrue(actual is Exception)
        assertEquals(expected, actual?.message)
    }

    // fetchBuildById

    @Test
    fun `fetchBuildById - successful response returns a BuildListItem`() = runTest {
        val actual = omedaCityRepository.fetchBuildById("123").getOrNull()
        val expected = fakeBuildDto.create()
        assertEquals(expected.title , actual?.title)
        assertEquals(expected.buildItems, actual?.buildItems)
    }

    @Test
    fun `fetchBuildById - non-successful response returns exception with message`() = runTest {
        omedaCityRepository = OmedaCityRepositoryImpl(
            playerApiService = FakeOmedaCityService(404)
        )
        val actual = omedaCityRepository.fetchBuildById("123").exceptionOrNull()
        val expected = "Failed to fetch build"
        assertTrue(actual is Exception)
        assertEquals(expected, actual?.message)
    }

    @Test
    fun `fetchBuildById - thrown exception returns failure with message`() = runTest {
        omedaCityRepository = OmedaCityRepositoryImpl(
            playerApiService = FakeOmedaCityService()
        )
        val actual = omedaCityRepository.fetchBuildById("123").exceptionOrNull()
        val expected = "Something went wrong"
        assertTrue(actual is Exception)
        assertEquals(expected, actual?.message)
    }
}