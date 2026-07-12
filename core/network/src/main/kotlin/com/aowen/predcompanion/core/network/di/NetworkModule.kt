package com.aowen.predcompanion.core.network.di

import android.content.Context
import com.aowen.predcompanion.core.common.di.SupabaseApiKey
import com.aowen.predcompanion.core.network.BuildConfig
import com.aowen.predcompanion.core.network.SupabaseAuthService
import com.aowen.predcompanion.core.network.SupabaseAuthServiceImpl
import com.aowen.predcompanion.core.network.SupabasePostgrestService
import com.aowen.predcompanion.core.network.SupabasePostgrestServiceImpl
import com.aowen.predcompanion.core.network.apollo.type.DateTime
import com.aowen.predcompanion.core.network.interceptors.AuthApolloInterceptor
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Adapter
import com.apollographql.apollo.api.CustomScalarAdapters
import com.apollographql.apollo.api.json.JsonReader
import com.apollographql.apollo.api.json.JsonWriter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.ExternalAuthAction
import io.github.jan.supabase.auth.FlowType
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.functions.Functions
import io.github.jan.supabase.functions.functions
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import net.openid.appauth.AuthorizationService
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton
import kotlin.time.Instant

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
    fun providesSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_API_KEY
        ) {
            install(Postgrest)
            install(Auth) {
                scheme = "monolith"
                host = "login"
                flowType = FlowType.PKCE
                defaultExternalAuthAction = ExternalAuthAction.CustomTabs()
            }
            install(Functions)
        }
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
    fun okHttpCallFactory(): Call.Factory = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor()
                .apply {
                    if (BuildConfig.DEBUG) {
                        setLevel(HttpLoggingInterceptor.Level.BODY)
                    }
                }
        ).build()

    @Provides
    @Singleton
    fun provideSupabasePostgrestService(postgrest: Postgrest): SupabasePostgrestService =
        SupabasePostgrestServiceImpl(postgrest)

    @Provides
    @Singleton
    fun authorizationService(@ApplicationContext ctx: Context) = AuthorizationService(ctx)

    @Provides
    @Singleton
    fun provideApolloClient(
        authInterceptor: AuthApolloInterceptor
    ): ApolloClient =
        ApolloClient.Builder()
            .serverUrl("https://pred.gg/gql")
            .addHttpInterceptor(authInterceptor)
            .addCustomScalarAdapter(DateTime.type, dateTimeAdapter)
            .build()

    val dateTimeAdapter = object : Adapter<Instant> {
        override fun fromJson(
            reader: JsonReader,
            customScalarAdapters: CustomScalarAdapters
        ): Instant {
            return Instant.parse(reader.nextString()!!)
        }

        override fun toJson(
            writer: JsonWriter,
            customScalarAdapters: CustomScalarAdapters,
            value: Instant
        ) {
            writer.value(value.toString())
        }
    }
}