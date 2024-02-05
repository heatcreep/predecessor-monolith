package com.aowen.monolith.network

import com.aowen.monolith.fakes.FakeSupabaseAuthService
import com.aowen.monolith.fakes.FakeSupabasePostgrestService
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthRepositoryTest {

    private lateinit var authRepository: AuthRepository


    @Test
    fun `signInWithDiscord calls authService loginWithDiscord()`() = runTest {
        authRepository = AuthRepositoryImpl(
            authService = FakeSupabaseAuthService(),
            postgrestService = FakeSupabasePostgrestService()
        )

        val result = authRepository.signInWithDiscord()
        assertTrue(result.isSuccess)
    }

    @Test
    fun `signInWithDiscord returns failure result on timeout`() = runTest {
        authRepository = AuthRepositoryImpl(
            authService = FakeSupabaseAuthService(resCode = 408),
            postgrestService = FakeSupabasePostgrestService()
        )

        val actual = authRepository.signInWithDiscord().exceptionOrNull()
        assertEquals("Error 408: Response.error()", actual?.message)
    }

    @Test
    fun `signInWithDiscord returns failure result on rest exception`() = runTest {
        authRepository = AuthRepositoryImpl(
            authService = FakeSupabaseAuthService(resCode = 400),
            postgrestService = FakeSupabasePostgrestService()
        )

        val actual = authRepository.signInWithDiscord().exceptionOrNull()
        assertEquals("Error 400: Response.error()", actual?.message)
    }

    @Test
    fun `signInWithDiscord returns failure result on http exception`() = runTest {
        authRepository = AuthRepositoryImpl(
            authService = FakeSupabaseAuthService(resCode = 500),
            postgrestService = FakeSupabasePostgrestService()
        )

        val actual = authRepository.signInWithDiscord().exceptionOrNull()
        assertEquals("Error 500: Response.error()", actual?.message)
    }

    @Test
    fun `getPlayer returns user session on 200`() = runTest {
        authRepository = AuthRepositoryImpl(
            authService = FakeSupabaseAuthService(resCode = 200),
            postgrestService = FakeSupabasePostgrestService()
        )

        val actual = authRepository.getPlayer()
        assertEquals(UserProfile("fake-player-id"), actual.getOrNull())
    }

    @Test
    fun `getPlayer returns null on not 200`() = runTest {
        authRepository = AuthRepositoryImpl(
            authService = FakeSupabaseAuthService(),
            postgrestService = FakeSupabasePostgrestService()
        )

        val actual = authRepository.getPlayer()
        assertEquals(null, actual.getOrNull())
    }

    @Test
    fun `deleteUserAccount returns success on 200`() = runTest {
        authRepository = AuthRepositoryImpl(
            authService = FakeSupabaseAuthService(resCode = 200),
            postgrestService = FakeSupabasePostgrestService()
        )

        val actual = authRepository.deleteUserAccount("fake-user-id")
        assertEquals("Your account has been deleted.", actual.getOrNull())
    }

    @Test
    fun `deleteUserAccount returns failure on not 200`() = runTest {
        authRepository = AuthRepositoryImpl(
            authService = FakeSupabaseAuthService(),
            postgrestService = FakeSupabasePostgrestService()
        )

        val actual = authRepository.deleteUserAccount("fake-user-id").exceptionOrNull()
        assertEquals("Error 400: Response.error()", actual?.message)
    }
}