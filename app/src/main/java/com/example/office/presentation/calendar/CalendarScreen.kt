package com.example.office.presentation.calendar

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.office.presentation.calendar.components.*
import com.example.office.presentation.common.components.BottomNavigationBar
import com.example.office.presentation.common.components.NavigationItem

@Composable
fun CalendarScreen() {
    val colors = MaterialTheme.colorScheme

    Scaffold(
        containerColor = colors.background,
        bottomBar = { BottomNavigationBar(selectedItem = NavigationItem.Calendar) }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            color = Color.Transparent
        ) {
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Spacer(modifier = Modifier.height(16.dp))
                CalendarSection()
                Spacer(modifier = Modifier.height(24.dp))
                TaskOverviewCard()
                Spacer(modifier = Modifier.height(24.dp))
                TimeSlotTasks()
            }
        }
    }
}