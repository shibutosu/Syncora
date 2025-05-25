package com.example.office.presentation.search.components.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@Composable
fun FilterDropdown(
    filters: List<String>,
    showFilters: Boolean,
    selectedFilter: String,
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit,
    offset: DpOffset = DpOffset.Zero
) {
    DropdownMenu(
        expanded = showFilters,
        onDismissRequest = onDismiss,
        offset = offset
    ) {
        filters.forEach { filter ->
            DropdownMenuItem(
                text = { Text(filter) },
                onClick = { onSelect(filter) }
            )
        }
    }
}