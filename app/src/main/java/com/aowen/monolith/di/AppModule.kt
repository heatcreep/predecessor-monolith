package com.aowen.monolith.di

import com.aowen.monolith.network.AuthRepository
import com.aowen.monolith.network.AuthRepositoryImpl
import com.aowen.monolith.network.OmedaCityApi
import com.aowen.monolith.network.OmedaCityRepository
import com.aowen.monolith.network.OmedaCityRepositoryImpl
import com.aowen.monolith.network.RetrofitHelper
import com.aowen.monolith.network.UserRepository
import com.aowen.monolith.network.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.postgrest.Postgrest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

const val SupabaseUrl = "https://maidzolnycrzsszzursm.supabase.co"
const val SupabaseKey =
    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im1haWR6b2xueWNyenNzenp1cnNtIiwicm9sZSI6ImFub24iLCJpYXQiOjE2ODg5OTY3MDYsImV4cCI6MjAwNDU3MjcwNn0.pSx1pT3nx0G3Lm03UBhWDSc_iaiBgw1ID6zHbIPZTyA"

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
            supabaseUrl = SupabaseUrl,
            supabaseKey = SupabaseKey
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
    fun provideOmedaCityApi(retrofit: Retrofit): OmedaCityApi {
        return retrofit.create(OmedaCityApi::class.java)
    }

    @Provides
    @Singleton
    fun provideOmedaCityRepository(api: OmedaCityApi): OmedaCityRepository =
        OmedaCityRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideAuthRepository(client: SupabaseClient): AuthRepository =
        AuthRepositoryImpl(client)

    @Provides
    @Singleton
    fun provideUserRepository(client: SupabaseClient): UserRepository =
        UserRepositoryImpl(client)
}