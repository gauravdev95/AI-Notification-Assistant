package com.gaurav.ainotificationassistant.ui.screen.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gaurav.ainotificationassistant.domain.model.Importance
import com.gaurav.ainotificationassistant.domain.model.NotificationItem
import com.gaurav.ainotificationassistant.domain.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    private val _notifications = MutableStateFlow<List<NotificationItem>>(emptyList())
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    private val _filter = MutableStateFlow("ALL")
    val filter: StateFlow<String> = _filter.asStateFlow()

    init {
        loadNotifications()
    }

    private fun loadNotifications() {
        viewModelScope.launch {
            notificationRepository.getRecentNotifications(100).collect { items ->
                _notifications.update { items }
            }
        }
    }

    fun filterByImportance(importance: String) {
        _filter.update { importance }
        viewModelScope.launch {
            when (importance) {
                "HIGH" -> notificationRepository.getHighPriorityNotifications(100)
                "MEDIUM" -> notificationRepository.getMediumPriorityNotifications(100)
                else -> notificationRepository.getRecentNotifications(100)
            }.collect { items ->
                _notifications.update { items }
            }
        }
    }

    fun deleteNotification(id: Long) {
        viewModelScope.launch {
            notificationRepository.deleteNotification(id)
            loadNotifications()
        }
    }

    fun toggleStar(id: Long, starred: Boolean) {
        viewModelScope.launch {
            notificationRepository.toggleStar(id, !starred)
            loadNotifications()
        }
    }
}
