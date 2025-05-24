package com.example.office.presentation.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.office.presentation.search.model.OfficeSearchResult
import com.example.office.presentation.search.components.widgets.*
import com.example.office.ui.theme.ThemeState

@Composable
fun SearchScreen(themeState: ThemeState) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var showFilters by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf("All") }

    val filters = listOf("All", "Tasks", "Workspace", "Mail", "Calendar", "Documents", "Projects")

    val searchResults = listOf(
        OfficeSearchResult("BoxSite Design System", "Project", "Workspace"),
        OfficeSearchResult("Eliezer brand guideline", "Task", "Tasks"),
        OfficeSearchResult("Meeting with team", "Event", "Calendar"),
        OfficeSearchResult("Project requirements", "Document", "Documents"),
        OfficeSearchResult("Invoice March", "Mail", "Mail"),
        OfficeSearchResult("Ultraflag dashboard", "Task", "Tasks")
    )

    // Используем цвета темы
    val colors = MaterialTheme.colorScheme

    Scaffold(
        containerColor = colors.background,
        bottomBar = {
            com.example.office.presentation.common.components.BottomNavigationBar(
                com.example.office.presentation.common.components.NavigationItem.Search
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            // Используй свои компоненты, но убедись что цвета у них из темы!
            SearchHeader(
                searchQuery = searchQuery,
                onQueryChange = { searchQuery = it },
                onFilterClick = { showFilters = true }
            )

            FilterDropdown(
                filters = filters,
                showFilters = showFilters,
                selectedFilter = selectedFilter,
                onDismiss = { showFilters = false },
                onSelect = {
                    selectedFilter = it
                    showFilters = false
                }
            )

            if (selectedFilter != "All") {
                FilterChip(
                    selectedFilter = selectedFilter,
                    onClear = { selectedFilter = "All" }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(
                    searchResults.filter {
                        (selectedFilter == "All" || it.category == selectedFilter) &&
                                (searchQuery.text.isEmpty() || it.title.contains(searchQuery.text, ignoreCase = true))
                    }
                ) { result ->
                    SearchResultItem(result) // исправленный под MaterialTheme (см. ниже)
                    Divider(
                        color = colors.outlineVariant,
                        thickness = 1.dp
                    )
                }
            }
        }
    }
}