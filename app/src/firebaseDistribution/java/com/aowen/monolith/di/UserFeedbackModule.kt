package com.aowen.monolith.di

import com.aowen.monolith.network.firebase.FirebaseUserFeedback
import com.aowen.monolith.network.firebase.UserFeedback
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UserFeedbackModule {

    @Provides
    fun provideUserFeedback(): UserFeedback {
        return FirebaseUserFeedback()
    }

}