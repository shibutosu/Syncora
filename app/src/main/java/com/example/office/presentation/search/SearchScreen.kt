package com.example.office.presentation.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.office.presentation.common.components.BottomNavigationBar
import com.example.office.presentation.common.components.NavigationItem
import com.example.office.ui.theme.ThemeState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun SearchScreen(
    themeState: ThemeState,
    searchHistory: List<String>,
    onAddToHistory: (String) -> Unit,
    onClearHistory: () -> Unit,
) {
    val insets = WindowInsets.statusBars.asPaddingValues()
    var searchQuery by rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue("")) }
    var isLoading by remember { mutableStateOf(false) }
    var searchError by remember { mutableStateOf(false) }
    var showHistory by remember { mutableStateOf(false) }
    var searchResults by remember { mutableStateOf<List<String>>(emptyList()) }
    var lastQuery by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    var searchJob by remember { mutableStateOf<Job?>(null) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    var showFilters by remember { mutableStateOf(false) }
    var filterIconOffset by remember { mutableStateOf(IntOffset.Zero) }

    fun performSearch(query: String) {
        isLoading = true
        searchError = false
        lastQuery = query
        coroutineScope.launch {
            delay(1000) // имитация задержки поиска
            isLoading = false
            if (query == "error") {
                searchError = true
                searchResults = emptyList()
            } else {
                searchResults = if (query.isBlank()) emptyList() else List(3) { "$query результат ${it + 1}" }
                if (searchResults.isNotEmpty()) onAddToHistory(query)
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = { BottomNavigationBar(NavigationItem.Search) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                // ↓↓↓ САМЫЙ КОМПАКТНЫЙ ОТСТУП ↓↓↓
                .padding(top = insets.calculateTopPadding().coerceAtMost(8.dp), start = 16.dp, end = 16.dp, bottom = 0.dp)
        ) {
            // Поисковая строка как ты просил
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSecondaryContainer)
                Spacer(modifier = Modifier.width(8.dp))

                BasicTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        showHistory = it.text.isEmpty() && searchHistory.isNotEmpty()
                        searchError = false
                        searchJob?.cancel()
                        if (it.text.isNotBlank()) {
                            searchJob = coroutineScope.launch {
                                delay(1000)
                                performSearch(it.text)
                            }
                        } else {
                            searchResults = emptyList()
                        }
                    },
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        fontSize = 16.sp
                    ),
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester)
                        .onFocusChanged {
                            if (it.isFocused && searchQuery.text.isEmpty() && searchHistory.isNotEmpty()) showHistory = true
                        },
                    decorationBox = { innerTextField ->
                        if (searchQuery.text.isEmpty()) {
                            Text(
                                "Search...",
                                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.5f)
                            )
                        }
                        innerTextField()
                    }
                )

                if (searchQuery.text.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear",
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable {
                                searchQuery = TextFieldValue("")
                                keyboardController?.hide()
                                showHistory = searchHistory.isNotEmpty()
                                searchResults = emptyList()
                            }
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Filter",
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier
                        .size(20.dp)
                        .onGloballyPositioned {
                            val offset = it.localToWindow(Offset.Zero)
                            filterIconOffset = IntOffset(offset.x.roundToInt(), offset.y.roundToInt())
                        }
                        .clickable { showFilters = true }
                )
            }

            // История поиска
            if (showHistory && searchHistory.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Column {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("История поиска")
                            TextButton(onClick = {
                                onClearHistory()
                                showHistory = false
                            }) {
                                Text("Очистить историю")
                            }
                        }
                        Divider()
                        searchHistory.forEach { historyItem ->
                            ListItem(
                                modifier = Modifier.clickable {
                                    searchQuery = TextFieldValue(historyItem)
                                    showHistory = false
                                    performSearch(historyItem)
                                    focusRequester.freeFocus()
                                    keyboardController?.hide()
                                },
                                headlineContent = { Text(historyItem) }
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Плейсхолдеры и результаты
            when {
                isLoading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                searchError -> {
                    Column(
                        Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("Произошла ошибка поиска")
                        Spacer(Modifier.height(8.dp))
                        Button(onClick = { performSearch(lastQuery) }) {
                            Text("Обновить")
                        }
                    }
                }
                searchQuery.text.isNotBlank() && searchResults.isEmpty() && !isLoading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Ничего не найдено")
                    }
                }
                searchResults.isNotEmpty() -> {
                    LazyColumn {
                        items(searchResults) { result ->
                            ListItem(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onAddToHistory(result)
                                    },
                                headlineContent = { Text(result) }
                            )
                            Divider()
                        }
                    }
                }
            }
        }
    }
}