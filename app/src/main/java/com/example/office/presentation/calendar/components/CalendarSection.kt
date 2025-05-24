package com.example.office.presentation.calendar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CalendarSection() {
    val colors = MaterialTheme.colorScheme
    val days = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    val dates = listOf("24", "26", "27", "28", "29", "30", "31")

    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("March 2023", fontSize = 24.sp, color = colors.onBackground)
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primaryContainer,
                    contentColor = colors.onPrimaryContainer
                ),
                shape = CircleShape
            ) {
                Text("Timeline", fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            for (i in days.indices) {
                val isToday = i == 0
                Box(
                    modifier = Modifier.size(60.dp)
                        .clip(CircleShape)
                        .background(if (isToday) colors.primary else Color.Transparent),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(days[i], fontSize = 14.sp, color = if (isToday) colors.onPrimary else colors.onSurfaceVariant)
                        Text(dates[i], fontSize = 18.sp, color = if (isToday) colors.onPrimary else colors.onBackground)
                    }
                }
            }
        }
    }
}