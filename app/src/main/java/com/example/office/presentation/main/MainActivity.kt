package com.example.office.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import com.example.office.data.local.TokenDataStore
import com.example.office.data.remote.ApiClient
import com.example.office.data.repository.TaskRepository
import com.example.office.presentation.main.viewmodel.TaskViewModel
import com.example.office.ui.theme.AppTheme
import com.example.office.ui.theme.ThemeState
import kotlinx.coroutines.flow.first
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import com.example.office.presentation.login.LoginScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        setContent {
            val themeState = remember { ThemeState(this) }

            var token: String? by remember { mutableStateOf(null) }
            var isTokenLoaded by remember { mutableStateOf(false) }

            // 🔁 Загружаем токен из DataStore
            LaunchedEffect(Unit) {
                token = TokenDataStore.getToken(this@MainActivity).first()
                isTokenLoaded = true
            }

            AppTheme(themeState) {
                when {
                    !isTokenLoaded -> LoadingScreen()
                    token.isNullOrBlank() -> LoginScreen()
                    else -> {
                        val tokenProvider: suspend () -> String = { token!! }

                        val taskViewModel = remember {
                            val repository = TaskRepository(ApiClient.api, tokenProvider)
                            val factory = TaskViewModelFactory(repository)
                            ViewModelProvider(this@MainActivity, factory)[TaskViewModel::class.java]
                        }

                        MainScreen(themeState = themeState, taskViewModel = taskViewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}