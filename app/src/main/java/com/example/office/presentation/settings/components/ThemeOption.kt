package com.example.office.presentation.settings.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

enum class ThemeOption(val displayName: String, val icon: ImageVector) {
    LIGHT("Светлая", Icons.Default.LightMode),
    DARK("Тёмная", Icons.Default.DarkMode),
    SYSTEM("Систем.", Icons.Default.Settings);

    companion object {
        val entries = values().toList()
    }
}