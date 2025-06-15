package com.aowen.monolith.network

import com.aowen.monolith.data.PlayerInfo
import com.aowen.monolith.data.create
import com.aowen.monolith.data.asMatchDetails
import com.aowen.monolith.fakes.FakeOmedaCityService
import com.aowen.monolith.fakes.data.fakeBuildDto
import com.aowen.monolith.fakes.data.fakeMatchDto
import com.aowen.monolith.fakes.data.fakePlayerDto
import com.aowen.monolith.fakes.data.fakePlayerHeroStatsDto
import com.aowen.monolith.fakes.data.fakePlayerStatsDto
import com.aowen.monolith.fakes.repo.FakeOmedaCityMatchRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class OmedaCityRepositoryTest {

    private lateinit var omedaCityRepository: OmedaCityRepository

    private var omedaCityMatchRepository = FakeOmedaCityMatchRepository()

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