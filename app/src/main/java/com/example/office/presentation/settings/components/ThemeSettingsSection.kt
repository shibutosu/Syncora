package com.example.office.presentation.settings.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

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