package com.aowen.predcompanion.core.data.repository

import com.aowen.predcompanion.core.model.data.asBuildListItem
import com.aowen.predcompanion.core.data.repository.builds.BuildRepository
import com.aowen.predcompanion.core.data.repository.builds.OmedaCityBuildRepository
import com.aowen.predcompanion.core.network.Resource
import com.aowen.predcompanion.core.network.getOrThrow
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkHeroBuild
import com.aowen.predcompanion.core.testing.fakes.service.TestPredCompanionNetworkDataSource
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class BuildRepositoryTest {

    lateinit var omedaCityBuildRepository: BuildRepository
    private val buildListItemDataMapper = BuildListItemDataMapper()

    @Before
    fun setup() {
        omedaCityBuildRepository = OmedaCityBuildRepository(
            retrofitOmedaCityNetwork = TestPredCompanionNetworkDataSource(200),
            buildListItemDataMapper = buildListItemDataMapper,
        )
    }

    // fetchAllBuilds
    @Test
    fun `fetchAllBuilds - successful response returns a list of BuildListItem`() = runTest {
        val actual = omedaCityBuildRepository.fetchAllBuilds().getOrThrow()
        val expected = listOf(
            fakeNetworkHeroBuild.asBuildListItem()
        )
        Assert.assertEquals(expected.size, actual.size)
    }

    @Test
    fun `fetchAllBuilds - non-successful response returns exception with message`() = runTest {
        omedaCityBuildRepository = OmedaCityBuildRepository(
            retrofitOmedaCityNetwork = TestPredCompanionNetworkDataSource(404),
            buildListItemDataMapper = BuildListItemDataMapper(),
        )
        val actual = omedaCityBuildRepository.fetchAllBuilds()
        val expected = "Failed to fetch data. (Code: 404 - Not Found)"
        Assert.assertTrue(actual is Resource.NetworkError)
        Assert.assertEquals(expected, (actual as Resource.NetworkError).errorMessage)
    }

    @Test
    fun `fetchAllBuilds - thrown exception returns failure with message`() = runTest {
        omedaCityBuildRepository = OmedaCityBuildRepository(
            retrofitOmedaCityNetwork = TestPredCompanionNetworkDataSource(),
            buildListItemDataMapper = BuildListItemDataMapper(),
        )
        val actual = omedaCityBuildRepository.fetchAllBuilds()
        val expected = "Something went wrong"
        Assert.assertTrue(actual is Resource.GenericError)
        Assert.assertEquals(expected, (actual as Resource.GenericError).errorMessage)
    }

    // fetchBuildById

    @Test
    fun `fetchBuildById - successful response returns a BuildListItem`() = runTest {
        val actual = omedaCityBuildRepository.fetchBuildById("123").getOrThrow()
        val expected = fakeNetworkHeroBuild.asBuildListItem()
        assertEquals(expected.title , actual.title)
        assertEquals(expected.buildItemIds, actual.buildItemIds)
    }

    @Test
    fun `fetchBuildById - non-successful response returns exception with message`() = runTest {
        omedaCityBuildRepository = OmedaCityBuildRepository(
            retrofitOmedaCityNetwork = TestPredCompanionNetworkDataSource(404),
            buildListItemDataMapper = BuildListItemDataMapper(),
        )
        val actual = omedaCityBuildRepository.fetchBuildById("123")
        val expected = "Failed to fetch data. (Code: 404 - Not Found)"
        Assert.assertTrue(actual is Resource.NetworkError)
        Assert.assertEquals(expected, (actual as Resource.NetworkError).errorMessage)
    }

    @Test
    fun `fetchBuildById - thrown exception returns failure with message`() = runTest {
        omedaCityBuildRepository = OmedaCityBuildRepository(
            retrofitOmedaCityNetwork = TestPredCompanionNetworkDataSource(),
            buildListItemDataMapper = BuildListItemDataMapper(),
        )
        val actual = omedaCityBuildRepository.fetchBuildById("123")
        val expected = "Something went wrong"
        Assert.assertTrue(actual is Resource.GenericError)
        Assert.assertEquals(expected, (actual as Resource.GenericError).errorMessage)
    }
}