package com.gaurav.ainotificationassistant.domain.repository

import com.gaurav.ainotificationassistant.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getSessionMessages(sessionId: String): Flow<List<ChatMessage>>
    fun getRecentMessages(limit: Int): Flow<List<ChatMessage>>
    suspend fun saveMessage(message: ChatMessage)
    suspend fun deleteSessionMessages(sessionId: String)
    suspend fun deleteOlderThan(beforeTimestamp: Long): Int
    suspend fun deleteAll()
    suspend fun getSessionMessageCount(sessionId: String): Int
}
