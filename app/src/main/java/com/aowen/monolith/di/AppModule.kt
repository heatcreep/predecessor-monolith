package com.aowen.monolith.di

import android.content.Context
import com.aowen.monolith.BuildConfig
import com.aowen.monolith.network.AuthRepository
import com.aowen.monolith.network.AuthRepositoryImpl
import com.aowen.monolith.network.ClaimedPlayerPreferencesManager
import com.aowen.monolith.network.ClaimedPlayerPreferencesManagerImpl
import com.aowen.monolith.network.OmedaCityRepository
import com.aowen.monolith.network.OmedaCityRepositoryImpl
import com.aowen.monolith.network.OmedaCityService
import com.aowen.monolith.network.RetrofitHelper
import com.aowen.monolith.network.SupabaseAuthService
import com.aowen.monolith.network.SupabaseAuthServiceImpl
import com.aowen.monolith.network.SupabasePostgrestService
import com.aowen.monolith.network.SupabasePostgrestServiceImpl
import com.aowen.monolith.network.UserFavoriteBuildsRepository
import com.aowen.monolith.network.UserFavoriteBuildsRepositoryImpl
import com.aowen.monolith.network.UserPreferencesManager
import com.aowen.monolith.network.UserPreferencesManagerImpl
import com.aowen.monolith.network.UserRecentSearchRepository
import com.aowen.monolith.network.UserRecentSearchRepositoryImpl
import com.aowen.monolith.network.UserRepository
import com.aowen.monolith.network.UserRepositoryImpl
import com.aowen.monolith.network.utils.NetworkUtil.getOkHttpClientWithCache
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.functions.Functions
import io.github.jan.supabase.functions.functions
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providesClaimedPlayerPreferencesManager(@ApplicationContext appContext: Context): ClaimedPlayerPreferencesManager {
        return ClaimedPlayerPreferencesManagerImpl(appContext)
    }

    @Provides
    @Singleton
    fun providesUserPreferencesManager(@ApplicationContext appContext: Context): UserPreferencesManager {
        return UserPreferencesManagerImpl(appContext)
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun providesNetworkJson(): Json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    @Provides
    @Singleton
    fun provideRetrofit(@ApplicationContext appContext: Context, json: Json): Retrofit {

        return Retrofit.Builder()
            .baseUrl(RetrofitHelper.baseUrl)
            .addConverterFactory(
                json.asConverterFactory("application/json".toMediaType())
            )
            .client(getOkHttpClientWithCache(appContext))
            .build()
    }

    @Provides
    @Singleton
    fun provideSupabaseClient(@ApplicationContext appContext: Context): SupabaseClient {
        val httpClient = HttpClient(OkHttp) {
            engine {
                preconfigured = getOkHttpClientWithCache(appContext)
            }
        }
        val client = createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_API_KEY
        ) {
            httpEngine = httpClient.engine
            install(Postgrest)
            install(Auth) {
                scheme = "monolith"
                host = "login"
            }
            install(Functions)
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
    fun provideSupabaseAuth(client: SupabaseClient): Auth {
        return client.auth
    }

    @Provides
    @Singleton
    fun provideSupabaseFunctions(client: SupabaseClient): Functions {
        return client.functions
    }

    @Provides
    @Singleton
    fun provideSupabaseAuthService(auth: Auth, functions: Functions): SupabaseAuthService {
        return SupabaseAuthServiceImpl(auth, functions)
    }

    @Provides
    @Singleton
    fun provideSupabasePostgrestService(postgrest: Postgrest): SupabasePostgrestService {
        return SupabasePostgrestServiceImpl(postgrest)
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
    fun provideAuthRepository(
        authService: SupabaseAuthService,
        postgrestService: SupabasePostgrestService,
        userPreferencesManager: UserPreferencesManager
    ): AuthRepository =
        AuthRepositoryImpl(authService,postgrestService, userPreferencesManager)

    @Provides
    @Singleton
    fun provideUserRepository(
        authService: SupabaseAuthService,
        postgrest: SupabasePostgrestService,
        userPreferencesManager: UserPreferencesManager,
        repository: OmedaCityRepository
    ): UserRepository =
        UserRepositoryImpl(authService, postgrest,userPreferencesManager, repository)

    @Provides
    @Singleton
    fun provideUserRecentSearchRepository(
        postgrestService: SupabasePostgrestService,
        omedaCityRepository: OmedaCityRepository,
        repository: UserRepository
    ): UserRecentSearchRepository =
        UserRecentSearchRepositoryImpl(
            postgrestService = postgrestService,
            omedaCityRepository = omedaCityRepository,
            userRepository = repository
        )

    @Provides
    @Singleton
    fun provideFavoriteBuildsRepository(
        postgrestService: SupabasePostgrestService,
        repository: UserRepository
    ): UserFavoriteBuildsRepository =
        UserFavoriteBuildsRepositoryImpl(
            postgrestService = postgrestService,
            userRepository = repository
        )
}