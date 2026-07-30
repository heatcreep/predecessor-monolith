package com.aowen.predcompanion.core.network.di

import android.content.Context
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
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import net.openid.appauth.AuthorizationService
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