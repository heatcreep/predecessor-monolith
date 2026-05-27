package com.aowen.predcompanion.core.data.repository

import com.aowen.predcompanion.core.model.data.MatchesDetails
import com.aowen.predcompanion.core.model.data.asMatchDetails
import com.aowen.predcompanion.core.data.repository.matches.MatchRepository
import com.aowen.predcompanion.core.data.repository.matches.OmedaCityMatchRepository
import com.aowen.predcompanion.core.network.Resource
import com.aowen.predcompanion.core.network.getOrThrow
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkMatch
import com.aowen.predcompanion.core.testing.fakes.service.TestPredCompanionNetworkDataSource
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class MatchRepositoryTest : BaseRepositoryTest() {

    private lateinit var omedaCityMatchRepository: MatchRepository

    @Before
    fun setup() {
        omedaCityMatchRepository = OmedaCityMatchRepository(
            retrofitOmedaCityNetwork = TestPredCompanionNetworkDataSource(200)
        )
    }

    @Test
    fun `fetchMatchesById - successful response returns a list of MatchDetails`() = runTest {
        val actual = omedaCityMatchRepository.fetchMatchesById("123").getOrThrow()
        val expected = MatchesDetails(listOf(fakeNetworkMatch.asMatchDetails()), "cursor")
        assertEquals(expected, actual)
    }

    @Test
    fun `fetchMatchesById - non-successful response returns exception with message`() = runTest {
        omedaCityMatchRepository = OmedaCityMatchRepository(
            retrofitOmedaCityNetwork = TestPredCompanionNetworkDataSource(404)
        )
        val actual = omedaCityMatchRepository.fetchMatchesById("123")
        val expected = ERROR_MESSAGE_404
        Assert.assertTrue(actual is Resource.NetworkError)
        Assert.assertEquals(expected, (actual as Resource.NetworkError).errorMessage)
    }

    @Test
    fun `fetchMatchesById - thrown exception returns failure with message`() = runTest {
        omedaCityMatchRepository = OmedaCityMatchRepository(
            retrofitOmedaCityNetwork = TestPredCompanionNetworkDataSource()
        )
        val actual = omedaCityMatchRepository.fetchMatchesById("123")
        val expected = ERROR_MESSAGE_UNKNOWN
        Assert.assertTrue(actual is Resource.GenericError)
        Assert.assertEquals(expected, (actual as Resource.GenericError).errorMessage)
    }

    // fetchMatchById

    @Test
    fun `fetchMatchById - successful response returns a MatchDetails`() = runTest {
        val actual = omedaCityMatchRepository.fetchMatchById("123").getOrThrow()
        val expected = fakeNetworkMatch.asMatchDetails()
        assertEquals(expected, actual)
    }

    @Test
    fun `fetchMatchById - non-successful response returns exception with message`() = runTest {
        omedaCityMatchRepository = OmedaCityMatchRepository(
            retrofitOmedaCityNetwork = TestPredCompanionNetworkDataSource(404)
        )
        val actual = omedaCityMatchRepository.fetchMatchById("Error")
        val expected = ERROR_MESSAGE_404
        Assert.assertTrue(actual is Resource.NetworkError)
        Assert.assertEquals(expected, (actual as Resource.NetworkError).errorMessage)
    }

    @Test
    fun `fetchMatchById - thrown exception returns failure with message`() = runTest {
        omedaCityMatchRepository = OmedaCityMatchRepository(
            retrofitOmedaCityNetwork = TestPredCompanionNetworkDataSource()
        )
        val actual = omedaCityMatchRepository.fetchMatchById("123")
        val expected = ERROR_MESSAGE_UNKNOWN
        Assert.assertTrue(actual is Resource.GenericError)
        Assert.assertEquals(expected, (actual as Resource.GenericError).errorMessage)
    }
}