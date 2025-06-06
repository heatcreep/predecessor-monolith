package com.aowen.monolith.data.repository.heroes

import com.aowen.monolith.data.AbilityDetails
import com.aowen.monolith.data.HeroClass
import com.aowen.monolith.data.HeroDetails
import com.aowen.monolith.data.HeroRole
import com.aowen.monolith.data.asHeroDetails
import com.aowen.monolith.data.create
import com.aowen.monolith.fakes.FakeOmedaCityService
import com.aowen.monolith.fakes.data.defaultHeroBaseStats
import com.aowen.monolith.fakes.data.fakeHeroDto
import com.aowen.monolith.fakes.data.fakeHeroStatisticsDto
import com.aowen.monolith.network.Resource
import com.aowen.monolith.network.getOrThrow
import com.aowen.monolith.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HeroRepositoryTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()


    private lateinit var repository: HeroRepository

    companion object {
        private const val ERROR_MESSAGE_UNKNOWN = "Something went wrong"
        private const val ERROR_MESSAGE_404 = "Failed to fetch data. (Code: 404 - Not Found)"
    }

    @Before
    fun setup() {
        repository = OmedaCityHeroRepository(
            omedaCityService = FakeOmedaCityService(200)
        )
    }

    // fetchAllHeroes

    @Test
    fun `fetchAllHeroes - successful response returns a list of HeroDetails`() = runTest {
        val actual = repository.fetchAllHeroes().getOrThrow()
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
    fun `fetchAllHeroes - non-successful response returns Resource NetworkError`() = runTest {
        repository = OmedaCityHeroRepository(
            omedaCityService = FakeOmedaCityService(404)
        )
        val actual = repository.fetchAllHeroes()
        val expected = ERROR_MESSAGE_404
        assertTrue(actual is Resource.NetworkError)
        assertEquals(expected, (actual as Resource.NetworkError).errorMessage)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `fetchAllHeroes - thrown exception returns Resource Generic Error`() = runTest {
        repository = OmedaCityHeroRepository(
            omedaCityService = FakeOmedaCityService()
        )
        val actual = repository.fetchAllHeroes()
        val expected = ERROR_MESSAGE_UNKNOWN
        assertTrue(actual is Resource.GenericError)
        assertEquals(expected, (actual as Resource.GenericError).errorMessage)
    }

    // fetchHeroByName

    @Test
    fun `fetchHeroByName - successful response returns a HeroDetails`() = runTest {
        val actual = repository.fetchHeroByName("test").getOrThrow()
        val expected = fakeHeroDto.asHeroDetails()
        assertEquals(expected, actual)
    }

    @Test
    fun `fetchHeroByName - non-successful response returns exception with message`() = runTest {
        repository = OmedaCityHeroRepository(
            omedaCityService = FakeOmedaCityService(404)
        )
        val actual = repository.fetchHeroByName("test")
        val expected = ERROR_MESSAGE_404
        assertTrue(actual is Resource.NetworkError)
        assertEquals(expected, (actual as Resource.NetworkError).errorMessage)
    }

    @Test
    fun `fetchHeroByName - thrown exception returns failure with message`() = runTest {
        repository = OmedaCityHeroRepository(
            omedaCityService = FakeOmedaCityService()
        )
        val actual = repository.fetchHeroByName("test")
        val expected = ERROR_MESSAGE_UNKNOWN
        assertTrue(actual is Resource.GenericError)
        assertEquals(expected, (actual as Resource.GenericError).errorMessage)
    }

    // fetchHeroStatisticsById

    @Test
    fun `fetchHeroStatisticsById - successful response returns a HeroStatistics`() = runTest {
        val actual = repository.fetchHeroStatisticsById("123").getOrThrow()
        val expected = fakeHeroStatisticsDto.create()
        assertEquals(expected, actual)
    }

    @Test
    fun `fetchHeroStatisticsById - non-successful response returns exception with message`() =
        runTest {
            repository = OmedaCityHeroRepository(
                omedaCityService = FakeOmedaCityService(404)
            )
            val actual = repository.fetchHeroStatisticsById("123")
            val expected = ERROR_MESSAGE_404
            assertTrue(actual is Resource.NetworkError)
            assertEquals(expected, (actual as Resource.NetworkError).errorMessage)
        }

    @Test
    fun `fetchHeroStatisticsById - thrown exception returns failure with message`() = runTest {
        repository = OmedaCityHeroRepository(
            omedaCityService = FakeOmedaCityService()
        )
        val actual = repository.fetchHeroStatisticsById("123")
        val expected = ERROR_MESSAGE_UNKNOWN
        assertTrue(actual is Resource.GenericError)
        assertEquals(expected, (actual as Resource.GenericError).errorMessage)
    }


}