package com.example.office

//noinspection SuspiciousImport
import android.R
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.office.ui.theme.AppTheme
import com.example.office.ui.theme.ThemeState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import androidx.core.content.edit
import kotlinx.coroutines.FlowPreview
import com.example.office.presentation.common.components.BottomNavigationBar
import com.example.office.presentation.common.components.NavigationItem

interface UnsplashApi {
    @GET("search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("client_id") clientId: String
    ): UnsplashResponse
}

data class UnsplashResponse(val results: List<UnsplashPhoto>)
data class UnsplashPhoto(val id: String, val urls: UnsplashUrls)
data class UnsplashUrls(val regular: String)

@Suppress("DEPRECATION")
class SearchApiActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)

        setContent {
            val context = LocalContext.current
            val themeState = remember { ThemeState(context) }

            AppTheme(themeState) {
                SearchApiScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, FlowPreview::class)
@Composable
fun SearchApiScreen() {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()

    var searchQuery by rememberSaveable { mutableStateOf("") }
    var photos by remember { mutableStateOf(emptyList<UnsplashPhoto>()) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val prefs = context.getSharedPreferences("search_history", Context.MODE_PRIVATE)
    var searchHistory by remember { mutableStateOf(prefs.getStringSet("history", setOf())!!.toList()) }

    val showHistory = remember { mutableStateOf(false) }

    val shouldShowHistory by remember(searchQuery, photos, error, showHistory.value, searchHistory) {
        mutableStateOf(
            searchQuery.isEmpty() &&
                    photos.isEmpty() &&
                    !error &&
                    searchHistory.isNotEmpty() &&
                    showHistory.value
        )
    }

    val api = remember {
        Retrofit.Builder()
            .baseUrl("https://api.unsplash.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UnsplashApi::class.java)
    }

    suspend fun performSearch(query: String) {
        if (query.isBlank()) return
        isLoading = true
        error = false
        errorMessage = ""
        showHistory.value = false

        try {
            val response = api.searchPhotos(query, "KzCfXM__nx6vs37xdKbsSNBxRy3CCm4aXYta7RHtM18")
            photos = response.results

            val historySet = prefs.getStringSet("history", mutableSetOf())!!.toMutableList()
            historySet.remove(query)
            historySet.add(0, query)
            if (historySet.size > 10) historySet.removeAt(historySet.lastIndex)
            prefs.edit { putStringSet("history", historySet.toSet()) }
            searchHistory = historySet
        } catch (e: Exception) {
            error = true
            errorMessage = e.message ?: "Unknown error"
            photos = emptyList()
        } finally {
            isLoading = false
        }
    }

    // Автопоиск через 2 секунды
    LaunchedEffect(Unit) {
        snapshotFlow { searchQuery }
            .debounce(2000)
            .filter { it.isNotBlank() }
            .distinctUntilChanged()
            .collectLatest {
                performSearch(it)
                keyboardController?.hide()
            }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            BottomNavigationBar(selectedItem = NavigationItem.Search)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            SearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                onSearch = {
                    coroutineScope.launch { performSearch(searchQuery) }
                    keyboardController?.hide()
                },
                onClear = {
                    searchQuery = ""
                    photos = emptyList()
                    error = false
                    showHistory.value = true
                },
                onFocus = {
                    showHistory.value = true
                }
            )

            // 🔽 Полоска под строкой поиска
            if (isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            when {
                shouldShowHistory -> {
                    Text(
                        text = "История поиска",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyColumn {
                        items(searchHistory) { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .padding(12.dp)
                                    .clickable {
                                        searchQuery = item
                                        coroutineScope.launch { performSearch(item) }
                                        keyboardController?.hide()
                                    }
                            ) {
                                Text(text = item, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    TextButton(onClick = {
                        prefs.edit { remove("history") }
                        searchHistory = emptyList()
                        showHistory.value = false
                    }) {
                        Text("Очистить историю", color = MaterialTheme.colorScheme.error)
                    }
                }

                error -> Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Ошибка: $errorMessage",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    IconButton(
                        onClick = { coroutineScope.launch { performSearch(searchQuery) } },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "Повторить",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                photos.isEmpty() -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (searchQuery.isBlank()) {
                            "Введите запрос для поиска"
                        } else {
                            "Нет результатов для \"$searchQuery\""
                        },
                        color = MaterialTheme.colorScheme.outline,
                        fontSize = 18.sp
                    )
                }

                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(photos) { photo ->
                        Image(
                            painter = rememberAsyncImagePainter(photo.urls.regular),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .height(240.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}


@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClear: () -> Unit,
    onFocus: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(colors.surfaceVariant, RoundedCornerShape(28.dp))
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Поиск",
            tint = colors.onSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            textStyle = TextStyle(color = colors.onSurface, fontSize = 16.sp),
            modifier = Modifier
                .weight(1f)
                .onFocusChanged {
                    if (it.isFocused) onFocus()
                },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearch() }),
            decorationBox = { innerTextField ->
                if (query.isEmpty()) {
                    Text(
                        "Поиск изображений...",
                        color = colors.outline,
                        fontSize = 16.sp
                    )
                }
                innerTextField()
            }
        )

        if (query.isNotEmpty()) {
            IconButton(
                onClick = onClear,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Очистить",
                    tint = colors.onSurfaceVariant
                )
            }
        }
    }
}
