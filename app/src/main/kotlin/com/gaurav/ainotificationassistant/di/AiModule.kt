package com.gaurav.ainotificationassistant.di

import com.gaurav.ainotificationassistant.data.remote.GeminiDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AiModule {

    @Provides
    @Singleton
    fun provideGeminiDataSource(): GeminiDataSource = GeminiDataSource()
}
