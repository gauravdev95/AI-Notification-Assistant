package com.gaurav.ainotificationassistant.data.remote

import com.gaurav.ainotificationassistant.BuildConfig
import com.gaurav.ainotificationassistant.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GeminiDataSource @Inject constructor() {

    private var apiKey: String = ""

    fun initialize(apiKey: String) {
        this.apiKey = apiKey
    }

    fun streamResponse(
        userMessage: String,
        history: List<ChatMessage>
    ): Flow<String> = flow {
        if (apiKey.isEmpty()) {
            emit("Error: API key not configured. Please set it in Settings.")
            return@flow
        }

        try {
            emit(buildMockResponse(userMessage, history))
        } catch (e: Exception) {
            emit("Error: ${e.message ?: "Unknown error occurred"}")
        }
    }

    private suspend fun buildMockResponse(userMessage: String, history: List<ChatMessage>): String {
        return """
            Based on your notifications:

            I'm analyzing your notification history to provide a helpful response.
            In a live environment, this would be powered by Gemini AI to understand
            your notifications naturally.

            You asked: "$userMessage"

            To enable full AI functionality:
            1. Set your Gemini API key in Settings
            2. Ensure you have internet connectivity
            3. Grant notification access permission

            Current History: ${history.size} messages
        """.trimIndent()
    }

    companion object {
        private const val SYSTEM_PROMPT = """
            You are a personal notification assistant. Your role is to help the user understand and manage their Android notifications.

            When answering questions about notifications:
            1. Be concise and helpful
            2. Organize information clearly
            3. Highlight important actions the user should take
            4. Do not make up notifications that weren't provided to you
            5. If you don't have relevant notification data to answer a question, let the user know

            Format your responses in a friendly, conversational manner. Use lists and sections to organize information when appropriate.
        """
    }
}
