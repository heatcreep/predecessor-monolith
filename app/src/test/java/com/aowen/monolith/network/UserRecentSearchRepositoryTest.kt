package com.aowen.monolith.network

import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.data.create
import com.aowen.monolith.fakes.FakeSupabasePostgrestService
import com.aowen.monolith.fakes.FakeUserRepository
import com.aowen.monolith.fakes.RecentSearchStatus
import com.aowen.monolith.fakes.data.fakeExistingPlayerSearchDto
import com.aowen.monolith.fakes.data.fakePlayerDto
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class UserRecentSearchRepositoryTest {

    private lateinit var userRecentSearchRepository: UserRecentSearchRepository

    @Test
    fun `calling getRecentSearches() should return list of recent searches`() = runTest {
        val fakeSupabasePostgrestService = FakeSupabasePostgrestService()
        userRecentSearchRepository = UserRecentSearchRepositoryImpl(
            postgrestService = fakeSupabasePostgrestService,
            userRepository = FakeUserRepository()
        )
        val actual = userRecentSearchRepository.getRecentSearches()
        val expected  = listOf(fakeExistingPlayerSearchDto.create())
        assertEquals(expected, actual)
    }

    @Test
    fun `calling getRecentSearches() should return empty list of user id is null`() = runTest {
        val fakeSupabasePostgrestService = FakeSupabasePostgrestService()
        userRecentSearchRepository = UserRecentSearchRepositoryImpl(
            postgrestService = fakeSupabasePostgrestService,
            userRepository = FakeUserRepository(true)
        )
        val actual = userRecentSearchRepository.getRecentSearches()
        val expected  = emptyList<PlayerDetails>()
        assertEquals(expected, actual)
    }


    @Test
    fun `calling removeAllRecentSearches() should call postgrestService deleteAllRecentSearches()`() = runTest {
        val fakeSupabasePostgrestService = FakeSupabasePostgrestService()
        userRecentSearchRepository = UserRecentSearchRepositoryImpl(
            postgrestService = fakeSupabasePostgrestService,
            userRepository = FakeUserRepository()
        )
        assertTrue(fakeSupabasePostgrestService.searchCount.value == 5)
        userRecentSearchRepository.removeAllRecentSearches()
        assertTrue(fakeSupabasePostgrestService.searchCount.value == 0)
    }

    @Test
    fun `calling removeRecentSearch calls postgrestService deleteRecentSearch`() = runTest {
        val fakeSupabasePostgrestService = FakeSupabasePostgrestService()
        userRecentSearchRepository = UserRecentSearchRepositoryImpl(
            postgrestService = fakeSupabasePostgrestService,
            userRepository = FakeUserRepository()
        )
        assertTrue(fakeSupabasePostgrestService.searchCount.value == 5)
        userRecentSearchRepository.removeRecentSearch("addc8bb3-20ad-462a-a9f8-8b32bbf57514")
        assertTrue(fakeSupabasePostgrestService.searchCount.value == 4)
    }

    @Test
    fun `failed call to removeRecentSearch does not call postgrestService deleteRecentSearch`() = runTest {
        val fakeSupabasePostgrestService = FakeSupabasePostgrestService()
        userRecentSearchRepository = UserRecentSearchRepositoryImpl(
            postgrestService = fakeSupabasePostgrestService,
            userRepository = FakeUserRepository(true)
        )
        userRecentSearchRepository.removeRecentSearch("addc8bb3-20ad-462a-a9f8-8b32bbf57514")
        assertTrue(fakeSupabasePostgrestService.searchCount.value == 5)
    }

    @Test
    fun `calling addRecentSearch calls postgrestService updateRecentSearch when search exists`() = runTest {
        val fakeSupabasePostgrestService = FakeSupabasePostgrestService(RecentSearchStatus.UPDATE)
        userRecentSearchRepository = UserRecentSearchRepositoryImpl(
            postgrestService = fakeSupabasePostgrestService,
            userRepository = FakeUserRepository()
        )
        assertTrue(fakeSupabasePostgrestService.searchCount.value == 5)
        userRecentSearchRepository.addRecentSearch(fakePlayerDto.create())
        assertTrue(fakeSupabasePostgrestService.searchCount.value == 7)
    }

    @Test
    fun `calling addRecentSearch calls postgrestService updateRecentSearch when search list is full`() = runTest {
        val fakeSupabasePostgrestService = FakeSupabasePostgrestService(RecentSearchStatus.FULL)
        userRecentSearchRepository = UserRecentSearchRepositoryImpl(
            postgrestService = fakeSupabasePostgrestService,
            userRepository = FakeUserRepository()
        )
        assertTrue(fakeSupabasePostgrestService.searchCount.value == 5)
        userRecentSearchRepository.addRecentSearch(fakePlayerDto.create())
        assertTrue(fakeSupabasePostgrestService.searchCount.value == 7)
    }

    @Test
    fun `calling addRecentSearch calls postgrestService insertRecentSearch when search does not exist`() = runTest {
        val fakeSupabasePostgrestService = FakeSupabasePostgrestService(RecentSearchStatus.ADD)
        userRecentSearchRepository = UserRecentSearchRepositoryImpl(
            postgrestService = fakeSupabasePostgrestService,
            userRepository = FakeUserRepository()
        )
        assertTrue(fakeSupabasePostgrestService.searchCount.value == 5)
        userRecentSearchRepository.addRecentSearch(fakePlayerDto.create())
        assertTrue(fakeSupabasePostgrestService.searchCount.value == 6)
    }

    @Test
    fun `failed call to addRecentSearch does not call postgrestService insertRecentSearch`() = runTest {
        val fakeSupabasePostgrestService = FakeSupabasePostgrestService(RecentSearchStatus.ADD)
        userRecentSearchRepository = UserRecentSearchRepositoryImpl(
            postgrestService = fakeSupabasePostgrestService,
            userRepository = FakeUserRepository(true)
        )
        userRecentSearchRepository.addRecentSearch(fakePlayerDto.create())
        assertTrue(fakeSupabasePostgrestService.searchCount.value == 5)
    }
}