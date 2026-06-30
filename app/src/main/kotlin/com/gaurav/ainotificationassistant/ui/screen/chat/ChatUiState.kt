package com.gaurav.ainotificationassistant.ui.screen.chat

import com.gaurav.ainotificationassistant.domain.model.ChatMessage

data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val sessionId: String = ""
)
