package com.example.office

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat

@Suppress("DEPRECATION")
class SearchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        setContent {
            SearchScreen()
        }
    }
}

@Composable
fun SearchScreen() {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var showFilters by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf("All") }
    val focusRequester = remember { FocusRequester() }

    val filters = listOf(
        "All",
        "Tasks",
        "Workspace",
        "Mail",
        "Calendar",
        "Documents",
        "Projects"
    )

    // Mock search results with renamed class
    val searchResults = listOf(
        OfficeSearchResult("BoxSite Design System", "Project", "Workspace"),
        OfficeSearchResult("Eliezer brand guideline", "Task", "Tasks"),
        OfficeSearchResult("Meeting with team", "Event", "Calendar"),
        OfficeSearchResult("Project requirements", "Document", "Documents"),
        OfficeSearchResult("Invoice March", "Mail", "Mail"),
        OfficeSearchResult("Ultraflag dashboard", "Task", "Tasks")
    )

    Scaffold(
        containerColor = Color(0xFF161616),
        bottomBar = {
            BottomNavigationBar(
                selectedItem = NavigationItem.Search
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            color = Color.Transparent
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                SearchHeader(
                    searchQuery = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onFilterClick = { showFilters = true },
                    focusRequester = focusRequester
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.TopEnd)
                ) {
                    DropdownMenu(
                        expanded = showFilters,
                        onDismissRequest = { showFilters = false },
                        modifier = Modifier
                            .background(Color(0xFF282828))
                    ) {
                        filters.forEach { filter ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = filter,
                                        color = if (filter == selectedFilter) Color.White else Color.Gray
                                    )
                                },
                                onClick = {
                                    selectedFilter = filter
                                    showFilters = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (selectedFilter != "All") {
                    Row(
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF282828))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = selectedFilter,
                            color = Color.White,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear filter",
                            tint = Color.White,
                            modifier = Modifier
                                .size(16.dp)
                                .clickable { selectedFilter = "All" }
                        )
                    }
                }

                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(
                        searchResults.filter {
                            (selectedFilter == "All" || it.category == selectedFilter) &&
                                    (searchQuery.text.isEmpty() || it.title.contains(searchQuery.text, ignoreCase = true))
                        }
                    ) { result ->
                        SearchResultItem(result)
                        Divider(color = Color(0xFF282828), thickness = 1.dp)
                    }
                }
            }
        }
    }
}


@Composable
fun SearchHeader(
    searchQuery: TextFieldValue,
    onQueryChange: (TextFieldValue) -> Unit,
    onFilterClick: () -> Unit,
    focusRequester: FocusRequester
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Spacer(modifier = Modifier.width(12.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .height(40.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFF282828))
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                BasicTextField(
                    value = searchQuery,
                    onValueChange = onQueryChange,
                    textStyle = TextStyle(color = Color.White, fontSize = 16.sp),
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        if (searchQuery.text.isEmpty()) {
                            Text(
                                text = "Search...",
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        }
                        innerTextField()
                    }
                )

                if (searchQuery.text.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear",
                        tint = Color.Gray,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { onQueryChange(TextFieldValue("")) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.White)
                .clickable { onFilterClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = "Filters",
                tint = Color.Black,
                modifier = Modifier.size(20.dp)
            )
        }
    }

    Divider(
        modifier = Modifier.fillMaxWidth(),
        thickness = 1.dp,
        color = Color(0xFF282828)
    )
}

@Composable
fun SearchResultItem(result: OfficeSearchResult) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .clickable { /* Handle click */ },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF282828)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = when (result.category) {
                    "Tasks" -> "✓"
                    "Workspace" -> "◉"
                    "Mail" -> "✉"
                    "Calendar" -> "📅"
                    "Documents" -> "📄"
                    else -> "🔍"
                },
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = result.title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${result.type} • ${result.category}",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }

        Icon(
            imageVector = Icons.Default.ArrowOutward,
            contentDescription = "Open",
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )
    }
}

data class OfficeSearchResult(
    val title: String,
    val type: String,
    val category: String
)