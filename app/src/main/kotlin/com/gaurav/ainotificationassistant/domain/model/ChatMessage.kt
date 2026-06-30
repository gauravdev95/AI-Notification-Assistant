package com.gaurav.ainotificationassistant.domain.model

data class ChatMessage(
    val id: Long = 0,
    val sessionId: String = "",
    val role: String,
    val content: String,
    val timestamp: Long,
    val isStreaming: Boolean = false,
    val tokenCount: Int? = null
)
