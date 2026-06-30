package com.gaurav.ainotificationassistant.di

import android.content.Context
import com.gaurav.ainotificationassistant.data.local.datastore.UserPreferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideUserPreferencesDataStore(
        @ApplicationContext context: Context
    ): UserPreferencesDataStore = UserPreferencesDataStore(context)
}
