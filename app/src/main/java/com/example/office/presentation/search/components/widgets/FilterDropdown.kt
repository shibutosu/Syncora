package com.example.office.presentation.search.components.widgets

import androidx.compose.foundation.background
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun FilterDropdown(
    filters: List<String>,
    showFilters: Boolean,
    selectedFilter: String,
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit
) {
    val colors = MaterialTheme.colorScheme

    DropdownMenu(
        expanded = showFilters,
        onDismissRequest = onDismiss,
        modifier = Modifier.background(colors.surface)
    ) {
        filters.forEach { filter ->
            DropdownMenuItem(
                text = {
                    Text(
                        text = filter,
                        color = if (filter == selectedFilter) colors.primary else colors.onSurfaceVariant
                    )
                },
                onClick = { onSelect(filter) }
            )
        }
    }
}