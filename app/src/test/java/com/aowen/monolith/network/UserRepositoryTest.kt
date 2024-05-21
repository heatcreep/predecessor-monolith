package com.aowen.monolith.network

import com.aowen.monolith.data.UserInfo
import com.aowen.monolith.data.create
import com.aowen.monolith.fakes.FakeOmedaCityService
import com.aowen.monolith.fakes.FakeSupabaseAuthService
import com.aowen.monolith.fakes.FakeSupabasePostgrestService
import com.aowen.monolith.fakes.data.fakePlayerDto
import com.aowen.monolith.fakes.data.fakePlayerStatsDto
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.UUID

class UserRepositoryTest {

    private lateinit var userRepository: UserRepository


    @Test
    fun `getUser returns user session on 200`() = runTest {
        userRepository = UserRepositoryImpl(
            authService = FakeSupabaseAuthService(200),
            postgrestService = FakeSupabasePostgrestService(),
            omedaCityRepository = OmedaCityRepositoryImpl(
                playerApiService = FakeOmedaCityService(),
            )
        )

        val actual = userRepository.getUser()
        val expected = UserInfo(
            id = UUID.fromString("addc8bb3-20ad-462a-a9f8-8b32bbf57514"),
            updatedAt = null,
            email = "",
            fullName = "",
            avatarUrl = "",
            playerId = ""
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `getUser returns user session on non 200`() = runTest {
        userRepository = UserRepositoryImpl(
            authService = FakeSupabaseAuthService(),
            postgrestService = FakeSupabasePostgrestService(),
            omedaCityRepository = OmedaCityRepositoryImpl(
                playerApiService = FakeOmedaCityService(),
            )
        )

        val actual = userRepository.getUser()
        assertEquals(null, actual)
    }

    @Test
    fun `getClaimedUser returns claimedUser on 200`() = runTest {
        userRepository = UserRepositoryImpl(
            authService = FakeSupabaseAuthService(200),
            postgrestService = FakeSupabasePostgrestService(),
            omedaCityRepository = OmedaCityRepositoryImpl(
                playerApiService = FakeOmedaCityService(200),
            )
        )

        val actual = userRepository.getClaimedUser().getOrNull()
        val expectedPlayerDetails = fakePlayerDto.create()
        val expectedPlayerStats = fakePlayerStatsDto.create()
        assertEquals(
            ClaimedUser(
                playerDetails = expectedPlayerDetails,
                playerStats = expectedPlayerStats
            ),
            actual
        )
    }

    @Test
    fun `getClaimedUser returns exception on null id`() = runTest {
        userRepository = UserRepositoryImpl(
            authService = FakeSupabaseAuthService(null),
            postgrestService = FakeSupabasePostgrestService(),
            omedaCityRepository = OmedaCityRepositoryImpl(
                playerApiService = FakeOmedaCityService(200),
            )
        )

        val actual = userRepository.getClaimedUser().exceptionOrNull()
        assertEquals(
            "No User Profile found.",
            actual?.message
        )
    }

    @Test
    fun `getClaimedUser returns exception when fetch player info fails`() = runTest {
        userRepository = UserRepositoryImpl(
            authService = FakeSupabaseAuthService(200),
            postgrestService = FakeSupabasePostgrestService(),
            omedaCityRepository = OmedaCityRepositoryImpl(
                playerApiService = FakeOmedaCityService(400),
            )
        )

        val actual = userRepository.getClaimedUser().exceptionOrNull()
        assertEquals(
            "Failed to fetch player info.",
            actual?.message
        )
    }

    @Test
    fun `setClaimedUser updates claimedUser state`() = runTest {
        userRepository = UserRepositoryImpl(
            authService = FakeSupabaseAuthService(200),
            postgrestService = FakeSupabasePostgrestService(),
            omedaCityRepository = OmedaCityRepositoryImpl(
                playerApiService = FakeOmedaCityService(200),
            )
        )

        val expectedPlayerDetails = fakePlayerDto.create()
        val expectedPlayerStats = fakePlayerStatsDto.create()

        userRepository.setClaimedUser(
            playerStats = expectedPlayerStats,
            playerDetails = expectedPlayerDetails
        )

        assertEquals(
            ClaimedUser(
                playerDetails = expectedPlayerDetails,
                playerStats = expectedPlayerStats
            ),
            (userRepository as UserRepositoryImpl).claimedPlayer.value
        )
    }
}