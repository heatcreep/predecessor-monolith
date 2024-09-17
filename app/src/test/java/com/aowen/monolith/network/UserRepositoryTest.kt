package com.aowen.monolith.network

import com.aowen.monolith.data.UserInfo
import com.aowen.monolith.fakes.FakeSupabaseAuthService
import com.aowen.monolith.fakes.FakeSupabasePostgrestService
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
            postgrestService = FakeSupabasePostgrestService()
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
            postgrestService = FakeSupabasePostgrestService()
        )

        val actual = userRepository.getUser()
        assertEquals(null, actual)
    }
}