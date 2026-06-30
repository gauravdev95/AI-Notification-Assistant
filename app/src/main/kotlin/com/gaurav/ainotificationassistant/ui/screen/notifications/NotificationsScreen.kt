package com.gaurav.ainotificationassistant.ui.screen.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gaurav.ainotificationassistant.R
import com.gaurav.ainotificationassistant.domain.model.NotificationItem

@Composable
fun NotificationsScreen(
    viewModel: NotificationsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()
    val filter by viewModel.filter.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = stringResource(R.string.notifications),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AssistChip(
                onClick = { viewModel.filterByImportance("ALL") },
                label = { Text("All") },
                modifier = Modifier.padding(4.dp)
            )
            AssistChip(
                onClick = { viewModel.filterByImportance("HIGH") },
                label = { Text("High") },
                modifier = Modifier.padding(4.dp)
            )
            AssistChip(
                onClick = { viewModel.filterByImportance("MEDIUM") },
                label = { Text("Medium") },
                modifier = Modifier.padding(4.dp)
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(notifications) { notification ->
                NotificationCard(
                    notification = notification,
                    onDelete = { viewModel.deleteNotification(notification.id) },
                    onToggleStar = { starred ->
                        viewModel.toggleStar(notification.id, starred)
                    }
                )
            }
        }
    }
}

@Composable
fun NotificationCard(
    notification: NotificationItem,
    onDelete: () -> Unit = {},
    onToggleStar: (Boolean) -> Unit = {}
) {
    androidx.compose.material3.Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = notification.appLabel,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = notification.title ?: "",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                IconButton(
                    onClick = { onToggleStar(notification.isStarred) },
                    modifier = Modifier.padding(0.dp)
                ) {
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = "Star",
                        tint = if (notification.isStarred)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.outline
                    )
                }
            }

            if (notification.body != null) {
                Text(
                    text = notification.body,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = notification.importance.toString(),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(top = 8.dp),
                color = when (notification.importance.toString()) {
                    "HIGH" -> androidx.compose.ui.graphics.Color.Red
                    "MEDIUM" -> androidx.compose.ui.graphics.Color.Yellow
                    else -> androidx.compose.ui.graphics.Color.Gray
                }
            )
        }
    }
}
