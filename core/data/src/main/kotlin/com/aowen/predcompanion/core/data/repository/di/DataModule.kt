package com.aowen.predcompanion.core.data.repository.di

import com.aowen.predcompanion.core.data.repository.auth.AuthRepository
import com.aowen.predcompanion.core.data.repository.auth.SupabaseAuthRepository
import com.aowen.predcompanion.core.data.repository.user.NetworkUserRecentSearchRepository
import com.aowen.predcompanion.core.data.repository.user.NetworkUserRepository
import com.aowen.predcompanion.core.data.repository.user.OfflineFirstUserClaimedPlayerRepository
import com.aowen.predcompanion.core.data.repository.user.OfflineFirstUserFavoriteBuildsRepository
import com.aowen.predcompanion.core.data.repository.user.UserClaimedPlayerRepository
import com.aowen.predcompanion.core.data.repository.user.UserFavoriteBuildsRepository
import com.aowen.predcompanion.core.data.repository.user.UserRecentSearchRepository
import com.aowen.predcompanion.core.data.repository.user.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    internal abstract fun bindsAuthRepository(
        authRepository: SupabaseAuthRepository
    ): AuthRepository

    @Binds
    internal abstract fun bindsUserRepository(
        userRepository: NetworkUserRepository
    ): UserRepository

    @Binds
    internal abstract fun bindsUserClaimedPlayerRepository(
        userClaimedPlayerRepository: OfflineFirstUserClaimedPlayerRepository
    ): UserClaimedPlayerRepository

    @Binds
    internal abstract fun bindsUserRecentSearchRepository(
        userRecentSearchRepository: NetworkUserRecentSearchRepository
    ): UserRecentSearchRepository

    @Binds
    internal abstract fun bindsUserFavoriteBuildsRepository(
        userFavoriteBuildsRepository: OfflineFirstUserFavoriteBuildsRepository
    ): UserFavoriteBuildsRepository
}