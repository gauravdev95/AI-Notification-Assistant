package com.gaurav.ainotificationassistant.domain.usecase

import com.gaurav.ainotificationassistant.domain.model.ChatMessage
import com.gaurav.ainotificationassistant.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoadChatHistoryUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(sessionId: String): Flow<List<ChatMessage>> {
        return chatRepository.getSessionMessages(sessionId)
    }

    fun loadRecentMessages(limit: Int): Flow<List<ChatMessage>> {
        return chatRepository.getRecentMessages(limit)
    }
}
