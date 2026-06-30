package com.gaurav.ainotificationassistant.di

import com.gaurav.ainotificationassistant.data.repository.ChatRepositoryImpl
import com.gaurav.ainotificationassistant.data.repository.NotificationRepositoryImpl
import com.gaurav.ainotificationassistant.domain.repository.ChatRepository
import com.gaurav.ainotificationassistant.domain.repository.NotificationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(
        impl: NotificationRepositoryImpl
    ): NotificationRepository

    @Binds
    @Singleton
    abstract fun bindChatRepository(
        impl: ChatRepositoryImpl
    ): ChatRepository
}
