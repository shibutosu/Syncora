package com.example.office.presentation.profile

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.office.presentation.login.LoginActivity
import com.example.office.presentation.settings.SettingsActivity
import com.example.office.ui.theme.AppTheme
import com.example.office.ui.theme.ThemeState
import androidx.compose.material3.*

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        val token = getSharedPreferences("auth", MODE_PRIVATE).getString("jwt", null)
        val viewModel: ProfileViewModel by viewModels()

        setContent {
            val themeState = remember { ThemeState(this) }
            var showLogoutDialog by remember { mutableStateOf(false) }

            val userState by viewModel.user.collectAsState()

            LaunchedEffect(Unit) {
                token?.let { viewModel.loadProfile(it) }
            }

            AppTheme(themeState) {
                ProfileScreen(
                    user = userState,
                    onSettingsClicked = {
                        startActivity(Intent(this, SettingsActivity::class.java))
                    },
                    onLogout = {
                        showLogoutDialog = true
                    }
                )

                if (showLogoutDialog) {
                    AlertDialog(
                        onDismissRequest = { showLogoutDialog = false },
                        title = { Text("Выход из профиля") },
                        text = { Text("Вы уверены, что хотите выйти из учётной записи?") },
                        confirmButton = {
                            Button(
                                onClick = {
                                    getSharedPreferences("auth", MODE_PRIVATE).edit().clear().apply()
                                    val intent = Intent(this, LoginActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent)
                                    finish()
                                }
                            ) {
                                Text("Выйти")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showLogoutDialog = false }) {
                                Text("Отмена")
                            }
                        }
                    )
                }
            }
        }
    }
}