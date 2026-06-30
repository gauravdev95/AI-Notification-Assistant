package com.gaurav.ainotificationassistant.ui.screen.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gaurav.ainotificationassistant.domain.model.ChatMessage
import com.gaurav.ainotificationassistant.domain.usecase.LoadChatHistoryUseCase
import com.gaurav.ainotificationassistant.domain.usecase.SendChatMessageUseCase
import com.gaurav.ainotificationassistant.data.local.datastore.UserPreferencesDataStore
import com.gaurav.ainotificationassistant.data.remote.GeminiDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val sendChatMessageUseCase: SendChatMessageUseCase,
    private val loadChatHistoryUseCase: LoadChatHistoryUseCase,
    private val geminiDataSource: GeminiDataSource,
    private val userPreferencesDataStore: UserPreferencesDataStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState(sessionId = UUID.randomUUID().toString()))
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    init {
        initializeGemini()
        loadChatHistory()
    }

    private fun initializeGemini() {
        viewModelScope.launch {
            userPreferencesDataStore.geminiApiKey.collect { apiKey ->
                if (apiKey.isNotEmpty()) {
                    geminiDataSource.initialize(apiKey)
                }
            }
        }
    }

    private fun loadChatHistory() {
        viewModelScope.launch {
            loadChatHistoryUseCase(_uiState.value.sessionId).collect { messages ->
                _uiState.update { it.copy(messages = messages) }
            }
        }
    }

    fun sendMessage(userText: String) {
        if (userText.isBlank()) return

        viewModelScope.launch {
            val userMsg = ChatMessage(
                role = "user",
                content = userText,
                timestamp = System.currentTimeMillis(),
                sessionId = _uiState.value.sessionId
            )
            _uiState.update { it.copy(messages = it.messages + userMsg) }
            _uiState.update { it.copy(isLoading = true) }

            val placeholderMsg = ChatMessage(
                role = "model",
                content = "",
                timestamp = System.currentTimeMillis(),
                sessionId = _uiState.value.sessionId,
                isStreaming = true
            )
            _uiState.update { it.copy(messages = it.messages + placeholderMsg) }

            sendChatMessageUseCase(userText, _uiState.value.messages)
                .collect { chunk ->
                    _uiState.update { state ->
                        val lastMsg = state.messages.last() as? ChatMessage ?: return@update state
                        val updatedMessages = state.messages.dropLast(1) + lastMsg.copy(
                            content = lastMsg.content + chunk,
                            isStreaming = true
                        )
                        state.copy(messages = updatedMessages)
                    }
                }

            _uiState.update { state ->
                val lastMsg = state.messages.last() as? ChatMessage ?: return@update state
                val updatedMessages = state.messages.dropLast(1) + lastMsg.copy(isStreaming = false)
                state.copy(messages = updatedMessages, isLoading = false)
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
