package com.aowen.monolith.data.repository.build

import com.aowen.monolith.data.asBuildListItem
import com.aowen.monolith.data.repository.builds.BuildRepository
import com.aowen.monolith.data.repository.builds.OmedaCityBuildRepository
import com.aowen.monolith.fakes.FakeOmedaCityService
import com.aowen.monolith.fakes.data.fakeBuildDto
import com.aowen.monolith.network.Resource
import com.aowen.monolith.network.getOrThrow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class BuildRepositoryTest {
    
    lateinit var omedaCityBuildRepository: BuildRepository
    
    @Before
    fun setup() {
        omedaCityBuildRepository = OmedaCityBuildRepository(
            omedaCityService = FakeOmedaCityService(200)
        )
    }

    // fetchAllBuilds
    @Test
    fun `fetchAllBuilds - successful response returns a list of BuildListItem`() = runTest {
        val actual = omedaCityBuildRepository.fetchAllBuilds().getOrThrow()
        val expected = listOf(
            fakeBuildDto.asBuildListItem()
        )
        assertEquals(expected.size, actual.size)
    }

    @Test
    fun `fetchAllBuilds - non-successful response returns exception with message`() = runTest {
        omedaCityBuildRepository = OmedaCityBuildRepository(
            omedaCityService = FakeOmedaCityService(404)
        )
        val actual = omedaCityBuildRepository.fetchAllBuilds()
        val expected = "Failed to fetch data. (Code: 404 - Not Found)"
        assertTrue(actual is Resource.NetworkError)
        assertEquals(expected, (actual as Resource.NetworkError).errorMessage)
    }

    @Test
    fun `fetchAllBuilds - thrown exception returns failure with message`() = runTest {
        omedaCityBuildRepository = OmedaCityBuildRepository(
            omedaCityService = FakeOmedaCityService()
        )
        val actual = omedaCityBuildRepository.fetchAllBuilds()
        val expected = "Something went wrong"
        assertTrue(actual is Resource.GenericError)
        assertEquals(expected, (actual as Resource.GenericError).errorMessage)
    }

    // fetchBuildById

    @Test
    fun `fetchBuildById - successful response returns a BuildListItem`() = runTest {
        val actual = omedaCityBuildRepository.fetchBuildById("123").getOrThrow()
        val expected = fakeBuildDto.asBuildListItem()
        assertEquals(expected.title , actual.title)
        assertEquals(expected.buildItems, actual.buildItems)
    }

    @Test
    fun `fetchBuildById - non-successful response returns exception with message`() = runTest {
        omedaCityBuildRepository = OmedaCityBuildRepository(
            omedaCityService = FakeOmedaCityService(404)
        )
        val actual = omedaCityBuildRepository.fetchBuildById("123")
        val expected = "Failed to fetch data. (Code: 404 - Not Found)"
        assertTrue(actual is Resource.NetworkError)
        assertEquals(expected, (actual as Resource.NetworkError).errorMessage)
    }

    @Test
    fun `fetchBuildById - thrown exception returns failure with message`() = runTest {
        omedaCityBuildRepository = OmedaCityBuildRepository(
            omedaCityService = FakeOmedaCityService()
        )
        val actual = omedaCityBuildRepository.fetchBuildById("123")
        val expected = "Something went wrong"
        assertTrue(actual is Resource.GenericError)
        assertEquals(expected, (actual as Resource.GenericError).errorMessage)
    }
}