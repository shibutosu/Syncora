package com.example.office.presentation.search

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.office.ui.theme.AppTheme
import com.example.office.ui.theme.ThemeState

private const val PREFS_NAME = "search_history"
private const val KEY_ENTRIES = "entries"

// ЧИСТИМ SharedPreferences если лежит старый формат (Set вместо String)
fun clearBrokenHistory(context: Context) {
    context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        .edit().clear().apply()
}

fun saveHistory(context: Context, history: List<String>) {
    context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        .edit()
        .putString(KEY_ENTRIES, history.joinToString("|"))
        .apply()
}

fun loadHistory(context: Context): List<String> {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val value = prefs.all[KEY_ENTRIES]
    return when (value) {
        is String -> if (value.isEmpty()) emptyList() else value.split("|")
        else -> emptyList() // если раньше было Set, не падаем, а просто очищаем
    }
}

class SearchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Очистка старых кривых данных (ОСТАВЬ ЭТО на пару запусков!)
        clearBrokenHistory(this)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        setContent {
            val themeState = remember { ThemeState(this) }
            var searchHistory by remember { mutableStateOf(loadHistory(this)) }

            AppTheme(themeState) {
                SearchScreen(
                    themeState = themeState,
                    searchHistory = searchHistory,
                    onAddToHistory = { query ->
                        val updated = (listOf(query) + searchHistory.filter { it != query }).take(10)
                        saveHistory(this, updated)
                        searchHistory = updated
                    },
                    onClearHistory = {
                        saveHistory(this, emptyList())
                        searchHistory = emptyList()
                    }
                    // остальные параметры если надо
                )
            }
        }
    }
}