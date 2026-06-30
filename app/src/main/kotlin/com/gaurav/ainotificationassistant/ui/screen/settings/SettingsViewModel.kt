package com.gaurav.ainotificationassistant.ui.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gaurav.ainotificationassistant.data.local.datastore.UserPreferencesDataStore
import com.gaurav.ainotificationassistant.domain.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesDataStore: UserPreferencesDataStore,
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    private val _apiKey = MutableStateFlow("")
    val apiKey: StateFlow<String> = _apiKey.asStateFlow()

    private val _darkMode = MutableStateFlow("system")
    val darkMode: StateFlow<String> = _darkMode.asStateFlow()

    private val _retentionDays = MutableStateFlow(30L)
    val retentionDays: StateFlow<Long> = _retentionDays.asStateFlow()

    private val _databaseSize = MutableStateFlow(0L)
    val databaseSize: StateFlow<Long> = _databaseSize.asStateFlow()

    init {
        viewModelScope.launch {
            userPreferencesDataStore.geminiApiKey.collect { key ->
                _apiKey.update { key }
            }
        }

        viewModelScope.launch {
            userPreferencesDataStore.darkMode.collect { mode ->
                _darkMode.update { mode }
            }
        }

        viewModelScope.launch {
            userPreferencesDataStore.retentionDays.collect { days ->
                _retentionDays.update { days }
            }
        }

        calculateDatabaseSize()
    }

    fun saveApiKey(key: String) {
        viewModelScope.launch {
            userPreferencesDataStore.setGeminiApiKey(key)
        }
    }

    fun setDarkMode(mode: String) {
        viewModelScope.launch {
            userPreferencesDataStore.setDarkMode(mode)
        }
    }

    fun setRetentionDays(days: Long) {
        viewModelScope.launch {
            userPreferencesDataStore.setRetentionDays(days)
        }
    }

    fun deleteAllData() {
        viewModelScope.launch {
            notificationRepository.deleteAll()
            userPreferencesDataStore.clearAll()
            calculateDatabaseSize()
        }
    }

    private fun calculateDatabaseSize() {
        viewModelScope.launch {
            val size = notificationRepository.getTotalSize()
            _databaseSize.update { size }
        }
    }
}
