package com.example.office.presentation.settings

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.office.presentation.common.components.HeaderNavigationBar
import com.example.office.presentation.settings.components.ThemeOption
import com.example.office.presentation.settings.components.ThemeSettingsSection
import com.example.office.ui.theme.ThemeMode
import com.example.office.ui.theme.ThemeState
import com.example.office.ui.theme.THEME_KEY
import com.example.office.ui.theme.dataStore
import kotlinx.coroutines.flow.first

@Composable
fun SettingsScreen(themeState: ThemeState) {
    val context = LocalContext.current
    val activity = context as? ComponentActivity

    val selectedTheme = remember {
        mutableStateOf(ThemeOption.valueOf(themeState.themeMode.name))
    }

    LaunchedEffect(Unit) {
        val stored = context.dataStore.data.first()[THEME_KEY]
        val themeMode = ThemeMode.valueOf(stored ?: ThemeMode.SYSTEM.name)

        selectedTheme.value = ThemeOption.valueOf(themeMode.name)
        themeState.update(themeMode)
    }

    Scaffold(containerColor = MaterialTheme.colorScheme.background) { paddingValues ->
        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            color = Color.Transparent
        ) {
            Column {
                HeaderNavigationBar("Settings", false)
                Spacer(modifier = Modifier.height(20.dp))

                ThemeSettingsSection(
                    currentTheme = selectedTheme.value,
                    onThemeSelected = {
                        val newMode = ThemeMode.valueOf(it.name)
                        themeState.update(newMode)
                        selectedTheme.value = it
                        activity?.recreate()
                    }
                )
            }
        }
    }
}