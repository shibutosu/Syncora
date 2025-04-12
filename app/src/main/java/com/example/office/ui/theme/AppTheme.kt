package com.example.office.ui.theme

import android.content.Context
import android.content.res.Configuration
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

val Context.dataStore by preferencesDataStore(name = "settings")
val THEME_KEY = stringPreferencesKey("theme_mode")

enum class ThemeMode { LIGHT, DARK, SYSTEM }

class ThemeState(private val context: Context) {
    var themeMode by mutableStateOf(ThemeMode.SYSTEM)
        private set

    init {
        runBlocking {
            val stored = context.dataStore.data.first()[THEME_KEY]
            themeMode = stored?.let { ThemeMode.valueOf(it) } ?: ThemeMode.SYSTEM
        }
    }

    fun update(newTheme: ThemeMode) {
        themeMode = newTheme
        runBlocking {
            context.dataStore.edit { it[THEME_KEY] = newTheme.name }
        }
    }
}

@Composable
fun AppTheme(
    themeState: ThemeState,
    content: @Composable () -> Unit
) {
    val isDarkTheme = when (themeState.themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> {
            val context = LocalContext.current
            val nightModeFlags = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            nightModeFlags == Configuration.UI_MODE_NIGHT_YES
        }
    }

    val darkColors = darkColorScheme(
        background = Color(0xFF161616),
        surface = Color(0xFF1E1E1E),
        primary = Color(0xFFFFFFFF),
        onPrimary = Color.Black,
        onBackground = Color.White,
        onSurface = Color.White,
        outline = Color.Gray
    )

    val lightColors = lightColorScheme(
        background = Color(0xFFFFFFFF),
        surface = Color(0xFFF5F5F5),
        primary = Color(0xFF282828),
        onPrimary = Color.White,
        onBackground = Color.Black,
        onSurface = Color.Black,
        outline = Color.Gray
    )

    MaterialTheme(
        colorScheme = if (isDarkTheme) darkColors else lightColors,
        typography = Typography(bodyLarge = TextStyle(fontSize = 16.sp, fontFamily = FontFamily.Default)),
        content = content
    )
}
