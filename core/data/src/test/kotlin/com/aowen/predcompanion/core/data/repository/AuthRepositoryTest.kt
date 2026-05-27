package com.aowen.predcompanion.core.data.repository

import com.aowen.predcompanion.core.data.repository.auth.AuthRepository
import com.aowen.predcompanion.core.data.repository.auth.SupabaseAuthRepository
import com.aowen.predcompanion.core.database.dao.FakeClaimedPlayerDao
import com.aowen.predcompanion.core.network.model.NetworkUserProfile
import com.aowen.predcompanion.core.testing.fakes.preferences.FakeUserPreferencesManager
import com.aowen.predcompanion.core.testing.fakes.service.FakeSupabaseAuthService
import com.aowen.predcompanion.core.testing.fakes.service.FakeSupabasePostgrestService
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthRepositoryTest {


    private val testScope = TestScope(UnconfinedTestDispatcher())

    private lateinit var authRepository: AuthRepository


    @Test
    fun `signInWithDiscord calls authService loginWithDiscord()`() = testScope.runTest {
        authRepository = SupabaseAuthRepository(
            authService = FakeSupabaseAuthService(),
            postgrestService = FakeSupabasePostgrestService(),
            userPreferencesManager = FakeUserPreferencesManager(),
            claimedPlayerDao = FakeClaimedPlayerDao()
        )

        val result = authRepository.signInWithDiscord()
        assertTrue(result.isSuccess)
    }

    @Test
    fun `signInWithDiscord returns failure result on timeout`() = testScope.runTest {
        authRepository = SupabaseAuthRepository(
            authService = FakeSupabaseAuthService(resCode = 408),
            postgrestService = FakeSupabasePostgrestService(),
            userPreferencesManager = FakeUserPreferencesManager(),
            claimedPlayerDao = FakeClaimedPlayerDao()
        )

        val actual = authRepository.signInWithDiscord().exceptionOrNull()
        assertEquals("Error 408: Response.error()", actual?.message)
    }

    @Test
    fun `signInWithDiscord returns failure result on rest exception`() = testScope.runTest {
        authRepository = SupabaseAuthRepository(
            authService = FakeSupabaseAuthService(resCode = 400),
            postgrestService = FakeSupabasePostgrestService(),
            userPreferencesManager = FakeUserPreferencesManager(),
            claimedPlayerDao = FakeClaimedPlayerDao()
        )

        val actual = authRepository.signInWithDiscord().exceptionOrNull()
        assertEquals("Error 400: Response.error()", actual?.message)
    }

    @Test
    fun `signInWithDiscord returns failure result on http exception`() = testScope.runTest {
        authRepository = SupabaseAuthRepository(
            authService = FakeSupabaseAuthService(resCode = 500),
            postgrestService = FakeSupabasePostgrestService(),
            userPreferencesManager = FakeUserPreferencesManager(),
            claimedPlayerDao = FakeClaimedPlayerDao()
        )

        val actual = authRepository.signInWithDiscord().exceptionOrNull()
        assertEquals("Error 500: Response.error()", actual?.message)
    }

    @Test
    fun `getPlayer returns user session on 200`() = testScope.runTest {
        authRepository = SupabaseAuthRepository(
            authService = FakeSupabaseAuthService(
                resCode = 200
            ),
            postgrestService = FakeSupabasePostgrestService(),
            userPreferencesManager = FakeUserPreferencesManager(),
            claimedPlayerDao = FakeClaimedPlayerDao()
        )

        authRepository.getCurrentSessionStatus()
        val actual = authRepository.getPlayer()
        assertEquals(NetworkUserProfile("fake-player-id"), actual.getOrNull())
    }

    @Test
    fun `getPlayer returns null on not 200`() = testScope.runTest {
        authRepository = SupabaseAuthRepository(
            authService = FakeSupabaseAuthService(),
            postgrestService = FakeSupabasePostgrestService(),
            userPreferencesManager = FakeUserPreferencesManager(),
            claimedPlayerDao = FakeClaimedPlayerDao()
        )

        val actual = authRepository.getPlayer()
        assertEquals(null, actual.getOrNull())
    }

    @Test
    fun `deleteUserAccount returns success on 200`() = testScope.runTest {
        authRepository = SupabaseAuthRepository(
            authService = FakeSupabaseAuthService(resCode = 200),
            postgrestService = FakeSupabasePostgrestService(),
            userPreferencesManager = FakeUserPreferencesManager(),
            claimedPlayerDao = FakeClaimedPlayerDao()
        )

        val actual = authRepository.deleteUserAccount("fake-user-id")
        assertEquals("Your account has been deleted.", actual.getOrNull())
    }

    @Test
    fun `deleteUserAccount returns failure on not 200`() = testScope.runTest {
        authRepository = SupabaseAuthRepository(
            authService = FakeSupabaseAuthService(),
            postgrestService = FakeSupabasePostgrestService(),
            userPreferencesManager = FakeUserPreferencesManager(),
            claimedPlayerDao = FakeClaimedPlayerDao()
        )

        val actual = authRepository.deleteUserAccount("fake-user-id").exceptionOrNull()
        assertEquals("Error 400: Response.error()", actual?.message)
    }
}