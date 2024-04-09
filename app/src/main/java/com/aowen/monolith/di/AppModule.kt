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
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.gotrue.gotrue
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
            install(GoTrue) {
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
    fun provideSupabaseGoTrue(client: SupabaseClient): GoTrue {
        return client.gotrue
    }

    @Provides
    @Singleton
    fun provideSupabaseFunctions(client: SupabaseClient): Functions {
        return client.functions
    }

    @Provides
    @Singleton
    fun provideSupabaseAuthService(goTrue: GoTrue, functions: Functions): SupabaseAuthService {
        return SupabaseAuthServiceImpl(goTrue, functions)
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
        postgrestService: SupabasePostgrestService
    ): AuthRepository =
        AuthRepositoryImpl(authService, postgrestService)

    @Provides
    @Singleton
    fun provideUserRepository(
        authService: SupabaseAuthService,
        postgrest: SupabasePostgrestService,
        repository: OmedaCityRepository
    ): UserRepository =
        UserRepositoryImpl(authService, postgrest, repository)

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
}