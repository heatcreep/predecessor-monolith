package com.aowen.predcompanion.core.network.di

import com.aowen.predcompanion.core.common.di.SupabaseApiKey
import com.aowen.predcompanion.core.common.network.RetrofitHelper
import com.aowen.predcompanion.core.network.BuildConfig
import com.aowen.predcompanion.core.network.OmedaCityService
import com.aowen.predcompanion.core.network.SupabaseAuthService
import com.aowen.predcompanion.core.network.SupabaseAuthServiceImpl
import com.aowen.predcompanion.core.network.SupabasePostgrestService
import com.aowen.predcompanion.core.network.SupabasePostgrestServiceImpl
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
internal object NetworkModule {

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun providesNetworkJson(): Json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    @Provides
    @SupabaseApiKey
    fun providesSupabaseApiKey(): String = BuildConfig.SUPABASE_API_KEY

    @Provides
    @Singleton
    fun providesRetrofit(json: Json): Retrofit =
        Retrofit.Builder()
            .baseUrl(RetrofitHelper.baseUrl)
            .addConverterFactory(
                json.asConverterFactory("application/json".toMediaType())
            ).build()

    @Provides
    @Singleton
    fun providesSupabaseClient(): SupabaseClient {
        val httpClient = HttpClient(OkHttp)
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
    fun provideSupabaseAuth(client: SupabaseClient): Auth = client.auth

    @Provides
    @Singleton
    fun provideSupabaseFunctions(client: SupabaseClient): Functions = client.functions

    @Provides
    @Singleton
    fun provideSupabaseAuthService(
        auth: Auth,
        functions: Functions,
        @SupabaseApiKey supabaseApiKey: String
    ): SupabaseAuthService = SupabaseAuthServiceImpl(auth, functions, supabaseApiKey)


    @Provides
    @Singleton
    fun provideSupabasePostgrest(client: SupabaseClient): Postgrest = client.postgrest

    @Provides
    @Singleton
    fun provideSupabasePostgrestService(postgrest: Postgrest): SupabasePostgrestService =
        SupabasePostgrestServiceImpl(postgrest)


    @Provides
    @Singleton
    fun provideOmedaCityApi(retrofit: Retrofit): OmedaCityService =
        retrofit.create(OmedaCityService::class.java)
}