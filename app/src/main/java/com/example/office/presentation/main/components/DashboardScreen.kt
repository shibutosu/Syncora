package com.example.office.presentation.main.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun DashboardScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp)
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = "Wow, your work is almost done!",
            fontSize = 42.sp,
            color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        ) {
            Row(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.weight(4f)) {
                    Box(modifier = Modifier.weight(5f)) {
                        PriorityTaskCard()
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(modifier = Modifier.weight(3f)) {
                        DailyTasksCard(viewModel = viewModel())
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Box(
                    modifier = Modifier
                        .weight(3f)
                        .padding(bottom = 16.dp)
                ) {
                    WorkspaceCard()
                }
            }
        }
    }
}