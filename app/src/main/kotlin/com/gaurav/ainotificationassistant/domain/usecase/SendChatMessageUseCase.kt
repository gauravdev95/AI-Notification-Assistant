package com.gaurav.ainotificationassistant.domain.usecase

import com.gaurav.ainotificationassistant.data.remote.GeminiDataSource
import com.gaurav.ainotificationassistant.domain.model.ChatMessage
import com.gaurav.ainotificationassistant.domain.repository.ChatRepository
import com.gaurav.ainotificationassistant.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class SendChatMessageUseCase @Inject constructor(
    private val geminiDataSource: GeminiDataSource,
    private val chatRepository: ChatRepository,
    private val notificationRepository: NotificationRepository
) {
    operator fun invoke(userMessage: String, history: List<ChatMessage>): Flow<String> {
        return geminiDataSource.streamResponse(
            userMessage = userMessage,
            history = history
        )
    }
}
