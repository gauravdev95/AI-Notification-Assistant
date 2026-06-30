package com.gaurav.ainotificationassistant.di

import android.content.Context
import androidx.room.Room
import com.gaurav.ainotificationassistant.data.local.db.AppDatabase
import com.gaurav.ainotificationassistant.data.local.db.DatabaseKeyManager
import com.gaurav.ainotificationassistant.data.local.db.dao.ChatMessageDao
import com.gaurav.ainotificationassistant.data.local.db.dao.NotificationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.zetetic.database.sqlcipher.SupportFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabaseKeyManager(
        @ApplicationContext context: Context
    ): DatabaseKeyManager = DatabaseKeyManager(context)

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        keyManager: DatabaseKeyManager
    ): AppDatabase {
        val passphrase = keyManager.getOrCreatePassphrase()
        val factory = SupportFactory(passphrase)
        passphrase.fill(0)

        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "ai_assistant.db"
        )
            .openHelperFactory(factory)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideNotificationDao(database: AppDatabase): NotificationDao =
        database.notificationDao()

    @Provides
    @Singleton
    fun provideChatMessageDao(database: AppDatabase): ChatMessageDao =
        database.chatMessageDao()
}
