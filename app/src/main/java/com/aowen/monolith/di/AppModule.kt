package com.aowen.monolith.di

import com.aowen.monolith.BuildConfig
import com.aowen.monolith.network.AuthRepository
import com.aowen.monolith.network.AuthRepositoryImpl
import com.aowen.monolith.network.OmedaCityRepository
import com.aowen.monolith.network.OmedaCityRepositoryImpl
import com.aowen.monolith.network.OmedaCityService
import com.aowen.monolith.network.RetrofitHelper
import com.aowen.monolith.network.UserRecentSearchRepository
import com.aowen.monolith.network.UserRecentSearchRepositoryImpl
import com.aowen.monolith.network.UserRepository
import com.aowen.monolith.network.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(RetrofitHelper.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        val client = createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_API_KEY
        ) {
            install(Postgrest)
            install(GoTrue) {
                scheme = "monolith"
                host = "login"
            }
        }
        return client
    }

    @Provides
    @Singleton
    fun provideSupabaseDatabase(client: SupabaseClient): Postgrest {
        return client.postgrest
    }

    @Provides
    @Singleton
    fun provideSupabaseGoTrue(client: SupabaseClient): GoTrue {
        return client.gotrue
    }

    @Provides
    @Singleton
    fun provideOmedaCityApi(retrofit: Retrofit): OmedaCityService {
        return retrofit.create(OmedaCityService::class.java)
    }

    @Provides
    @Singleton
    fun provideOmedaCityRepository(api: OmedaCityService): OmedaCityRepository =
        OmedaCityRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideAuthRepository(goTrue: GoTrue, postgrest: Postgrest): AuthRepository =
        AuthRepositoryImpl(goTrue, postgrest)

    @Provides
    @Singleton
    fun provideUserRepository(
        client: SupabaseClient,
        repository: OmedaCityRepository
    ): UserRepository =
        UserRepositoryImpl(client, repository)

    @Provides
    @Singleton
    fun provideUserRecentSearchRepository(
        postgrest: Postgrest,
        repository: UserRepository
    ): UserRecentSearchRepository =
        UserRecentSearchRepositoryImpl(postgrest, repository)
}