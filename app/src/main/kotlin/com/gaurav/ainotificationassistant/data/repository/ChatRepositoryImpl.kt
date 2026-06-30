package com.gaurav.ainotificationassistant.data.repository

import com.gaurav.ainotificationassistant.data.local.db.dao.ChatMessageDao
import com.gaurav.ainotificationassistant.data.local.db.entity.ChatMessageEntity
import com.gaurav.ainotificationassistant.domain.model.ChatMessage
import com.gaurav.ainotificationassistant.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatMessageDao: ChatMessageDao
) : ChatRepository {

    override fun getSessionMessages(sessionId: String): Flow<List<ChatMessage>> =
        chatMessageDao.getBySessionId(sessionId).map { entities -> entities.map { it.toDomain() } }

    override fun getRecentMessages(limit: Int): Flow<List<ChatMessage>> =
        chatMessageDao.getRecentMessages(limit).map { entities -> entities.map { it.toDomain() } }

    override suspend fun saveMessage(message: ChatMessage) {
        chatMessageDao.insert(message.toEntity())
    }

    override suspend fun deleteSessionMessages(sessionId: String) {
        chatMessageDao.deleteSession(sessionId)
    }

    override suspend fun deleteOlderThan(beforeTimestamp: Long): Int =
        chatMessageDao.deleteOlderThan(beforeTimestamp)

    override suspend fun deleteAll() {
        chatMessageDao.deleteAll()
    }

    override suspend fun getSessionMessageCount(sessionId: String): Int =
        chatMessageDao.getSessionMessageCount(sessionId)

    private fun ChatMessageEntity.toDomain(): ChatMessage =
        ChatMessage(
            id = id,
            sessionId = sessionId,
            role = role,
            content = content,
            timestamp = timestamp,
            isStreaming = isStreaming,
            tokenCount = tokenCount
        )

    private fun ChatMessage.toEntity(): ChatMessageEntity =
        ChatMessageEntity(
            id = id,
            sessionId = sessionId,
            role = role,
            content = content,
            timestamp = timestamp,
            isStreaming = isStreaming,
            tokenCount = tokenCount
        )
}
