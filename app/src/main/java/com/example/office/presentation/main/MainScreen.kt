package com.example.office.presentation.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.office.presentation.common.components.BottomNavigationBar
import com.example.office.presentation.common.components.NavigationItem
import com.example.office.presentation.main.components.*
import com.example.office.ui.theme.ThemeState

@Composable
fun MainScreen(themeState: ThemeState) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(selectedItem = NavigationItem.Home)
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            color = Color.Transparent
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                StudioHeader()
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = paddingValues.calculateBottomPadding())
                ) {
                    DashboardScreen()
                    ToDoList()
                }
            }
        }
    }
}
