package com.aowen.predcompanion.core.data.repository

import com.aowen.predcompanion.core.model.data.AbilityDetails
import com.aowen.predcompanion.core.model.data.HeroClass
import com.aowen.predcompanion.core.model.data.HeroDetails
import com.aowen.predcompanion.core.model.data.HeroRole
import com.aowen.predcompanion.core.model.data.asHeroDetails
import com.aowen.predcompanion.core.model.data.create
import com.aowen.predcompanion.core.data.repository.heroes.HeroRepository
import com.aowen.predcompanion.core.data.repository.heroes.OmedaCityHeroRepository
import com.aowen.predcompanion.core.network.Resource
import com.aowen.predcompanion.core.network.getOrThrow
import com.aowen.predcompanion.core.testing.fakes.data.defaultHeroBaseStats
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkHero
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkHeroStatistics
import com.aowen.predcompanion.core.testing.fakes.service.TestPredCompanionNetworkDataSource
import com.aowen.predcompanion.core.testing.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
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
            retrofitOmedaCityNetwork = TestPredCompanionNetworkDataSource(200)
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
                classes = listOf(HeroClass.ASSASSIN, HeroClass.FIGHTER),
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
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `fetchAllHeroes - non-successful response returns Resource NetworkError`() = runTest {
        repository = OmedaCityHeroRepository(
            retrofitOmedaCityNetwork = TestPredCompanionNetworkDataSource(404)
        )
        val actual = repository.fetchAllHeroes()
        val expected = ERROR_MESSAGE_404
        Assert.assertTrue(actual is Resource.NetworkError)
        Assert.assertEquals(expected, (actual as Resource.NetworkError).errorMessage)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `fetchAllHeroes - thrown exception returns Resource Generic Error`() = runTest {
        repository = OmedaCityHeroRepository(
            retrofitOmedaCityNetwork = TestPredCompanionNetworkDataSource()
        )
        val actual = repository.fetchAllHeroes()
        val expected = ERROR_MESSAGE_UNKNOWN
        Assert.assertTrue(actual is Resource.GenericError)
        Assert.assertEquals(expected, (actual as Resource.GenericError).errorMessage)
    }

    // fetchHeroByName

    @Test
    fun `fetchHeroByName - successful response returns a HeroDetails`() = runTest {
        val actual = repository.getHeroByName("test").getOrThrow()
        val expected = fakeNetworkHero.asHeroDetails()
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `fetchHeroByName - non-successful response returns exception with message`() = runTest {
        repository = OmedaCityHeroRepository(
            retrofitOmedaCityNetwork = TestPredCompanionNetworkDataSource(404)
        )
        val actual = repository.getHeroByName("test")
        val expected = ERROR_MESSAGE_404
        Assert.assertTrue(actual is Resource.NetworkError)
        Assert.assertEquals(expected, (actual as Resource.NetworkError).errorMessage)
    }

    @Test
    fun `fetchHeroByName - thrown exception returns failure with message`() = runTest {
        repository = OmedaCityHeroRepository(
            retrofitOmedaCityNetwork = TestPredCompanionNetworkDataSource()
        )
        val actual = repository.getHeroByName("test")
        val expected = ERROR_MESSAGE_UNKNOWN
        Assert.assertTrue(actual is Resource.GenericError)
        Assert.assertEquals(expected, (actual as Resource.GenericError).errorMessage)
    }

    // fetchHeroStatisticsById

    @Test
    fun `fetchHeroStatisticsById - successful response returns a HeroStatistics`() = runTest {
        val actual = repository.fetchHeroStatisticsById("123").getOrThrow()
        val expected = fakeNetworkHeroStatistics.create()
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `fetchHeroStatisticsById - non-successful response returns exception with message`() =
        runTest {
            repository = OmedaCityHeroRepository(
                retrofitOmedaCityNetwork = TestPredCompanionNetworkDataSource(404)
            )
            val actual = repository.fetchHeroStatisticsById("123")
            val expected = ERROR_MESSAGE_404
            Assert.assertTrue(actual is Resource.NetworkError)
            Assert.assertEquals(expected, (actual as Resource.NetworkError).errorMessage)
        }

    @Test
    fun `fetchHeroStatisticsById - thrown exception returns failure with message`() = runTest {
        repository = OmedaCityHeroRepository(
            retrofitOmedaCityNetwork = TestPredCompanionNetworkDataSource()
        )
        val actual = repository.fetchHeroStatisticsById("123")
        val expected = ERROR_MESSAGE_UNKNOWN
        Assert.assertTrue(actual is Resource.GenericError)
        Assert.assertEquals(expected, (actual as Resource.GenericError).errorMessage)
    }


}