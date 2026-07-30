package com.aowen.predcompanion.core.data.repository.di

import com.aowen.predcompanion.core.data.repository.auth.AppAuthRepository
import com.aowen.predcompanion.core.data.repository.auth.NewAuthRepository
import com.aowen.predcompanion.core.data.repository.auth.PredGgAuthTokenProvider
import com.aowen.predcompanion.core.data.repository.user.NetworkUserRecentSearchRepository
import com.aowen.predcompanion.core.data.repository.user.OfflineFirstUserRepository
import com.aowen.predcompanion.core.data.repository.user.OfflineFirstUserClaimedPlayerRepository
import com.aowen.predcompanion.core.data.repository.user.OfflineFirstUserFavoriteBuildsRepository
import com.aowen.predcompanion.core.data.repository.user.UserClaimedPlayerRepository
import com.aowen.predcompanion.core.data.repository.user.UserFavoriteBuildsRepository
import com.aowen.predcompanion.core.data.repository.user.UserRecentSearchRepository
import com.aowen.predcompanion.core.data.repository.user.UserRepository
import com.aowen.predcompanion.core.network.AuthTokenProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    internal abstract fun bindAuthTokenProvider(tokenProvider: PredGgAuthTokenProvider): AuthTokenProvider

    @Binds
    @Singleton
    internal abstract fun bindsNewAuthRepository(
        authRepository: AppAuthRepository
    ): NewAuthRepository

    @Binds
    @Singleton
    internal abstract fun bindsUserRepository(
        userRepository: OfflineFirstUserRepository
    ): UserRepository

    @Binds
    @Singleton
    internal abstract fun bindsUserClaimedPlayerRepository(
        userClaimedPlayerRepository: OfflineFirstUserClaimedPlayerRepository
    ): UserClaimedPlayerRepository

    @Binds
    @Singleton
    internal abstract fun bindsUserRecentSearchRepository(
        userRecentSearchRepository: NetworkUserRecentSearchRepository
    ): UserRecentSearchRepository

    @Binds
    @Singleton
    internal abstract fun bindsUserFavoriteBuildsRepository(
        userFavoriteBuildsRepository: OfflineFirstUserFavoriteBuildsRepository
    ): UserFavoriteBuildsRepository
}