package com.aowen.monolith.data.repository.matches

import com.aowen.monolith.data.MatchesDetails
import com.aowen.monolith.data.asMatchDetails
import com.aowen.monolith.data.repository.BaseRepositoryTest
import com.aowen.monolith.fakes.FakeOmedaCityService
import com.aowen.monolith.fakes.data.fakeMatchDto
import com.aowen.monolith.network.Resource
import com.aowen.monolith.network.getOrThrow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MatchRepositoryTest : BaseRepositoryTest() {

    private lateinit var omedaCityMatchRepository: MatchRepository

    @Before
    fun setup() {
        omedaCityMatchRepository = OmedaCityMatchRepository(
            omedaCityService = FakeOmedaCityService(200)
        )
    }

    @Test
    fun `fetchMatchesById - successful response returns a list of MatchDetails`() = runTest {
        val actual = omedaCityMatchRepository.fetchMatchesById("123").getOrThrow()
        val expected = MatchesDetails(listOf(fakeMatchDto.asMatchDetails()), "cursor")
        assertEquals(expected, actual)
    }

    @Test
    fun `fetchMatchesById - non-successful response returns exception with message`() = runTest {
        omedaCityMatchRepository = OmedaCityMatchRepository(
            omedaCityService = FakeOmedaCityService(404)
        )
        val actual = omedaCityMatchRepository.fetchMatchesById("123")
        val expected = ERROR_MESSAGE_404
        assertTrue(actual is Resource.NetworkError)
        assertEquals(expected, (actual as Resource.NetworkError).errorMessage)
    }

    @Test
    fun `fetchMatchesById - thrown exception returns failure with message`() = runTest {
        omedaCityMatchRepository = OmedaCityMatchRepository(
            omedaCityService = FakeOmedaCityService()
        )
        val actual = omedaCityMatchRepository.fetchMatchesById("123")
        val expected = ERROR_MESSAGE_UNKNOWN
        assertTrue(actual is Resource.GenericError)
        assertEquals(expected, (actual as Resource.GenericError).errorMessage)
    }

    // fetchMatchById

    @Test
    fun `fetchMatchById - successful response returns a MatchDetails`() = runTest {
        val actual = omedaCityMatchRepository.fetchMatchById("123").getOrThrow()
        val expected = fakeMatchDto.asMatchDetails()
        assertEquals(expected, actual)
    }

    @Test
    fun `fetchMatchById - non-successful response returns exception with message`() = runTest {
        omedaCityMatchRepository = OmedaCityMatchRepository(
            omedaCityService = FakeOmedaCityService(404)
        )
        val actual = omedaCityMatchRepository.fetchMatchById("Error")
        val expected = ERROR_MESSAGE_404
        assertTrue(actual is Resource.NetworkError)
        assertEquals(expected, (actual as Resource.NetworkError).errorMessage)
    }

    @Test
    fun `fetchMatchById - thrown exception returns failure with message`() = runTest {
        omedaCityMatchRepository = OmedaCityMatchRepository(
            omedaCityService = FakeOmedaCityService()
        )
        val actual = omedaCityMatchRepository.fetchMatchById("123")
        val expected = ERROR_MESSAGE_UNKNOWN
        assertTrue(actual is Resource.GenericError)
        assertEquals(expected, (actual as Resource.GenericError).errorMessage)
    }
}