package com.aowen.monolith.data.database.di

import android.content.Context
import androidx.room.Room
import com.aowen.monolith.data.database.MonolithDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val DATABASE_NAME = "monolith-database"

    @Provides
    @Singleton
    fun providesMonolithDatabase(
        @ApplicationContext context: Context,
    ): MonolithDatabase = Room.databaseBuilder(
        context,
        MonolithDatabase::class.java,
        DATABASE_NAME,
    ).build()
}