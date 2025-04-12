package com.example.office

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.office.ui.theme.AppTheme
import com.example.office.ui.theme.THEME_KEY
import com.example.office.ui.theme.ThemeMode
import com.example.office.ui.theme.ThemeState
import com.example.office.ui.theme.dataStore
import kotlinx.coroutines.flow.first

@Suppress("DEPRECATION")
class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        setContent {
            val context = LocalContext.current
            val themeState = remember { ThemeState(context) }

            AppTheme(themeState) {
                SettingsScreen(themeState)
            }
        }
    }
}

@Composable
fun ThemeSettingsSection(
    currentTheme: ThemeOption,
    onThemeSelected: (ThemeOption) -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Тема",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colors.onBackground,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ThemeOption.entries.forEach { theme ->
                val isSelected = currentTheme == theme
                Button(
                    onClick = { onThemeSelected(theme) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) colors.primaryContainer else colors.surfaceVariant,
                        contentColor = colors.onSurface
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp)
                        .height(80.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = theme.icon,
                            contentDescription = theme.displayName,
                            modifier = Modifier.size(24.dp),
                            tint = colors.onSurface
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = theme.displayName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = colors.onSurface
                        )
                    }
                }
            }
        }
    }
}

enum class ThemeOption(val displayName: String, val icon: ImageVector) {
    LIGHT("Светлая", Icons.Default.LightMode),
    DARK("Тёмная", Icons.Default.DarkMode),
    SYSTEM("Систем.", Icons.Default.Settings)
}

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

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
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