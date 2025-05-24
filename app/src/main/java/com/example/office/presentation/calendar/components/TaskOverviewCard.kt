package com.example.office.presentation.calendar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TaskOverviewCard() {
    val colors = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(colors.surfaceVariant)
            .padding(16.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("✅", fontSize = 20.sp, modifier = Modifier.padding(end = 8.dp))
                Text("Layerio Daily Task", fontSize = 20.sp, color = colors.onSurface)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("2 Meeting • 28 Task", fontSize = 16.sp, color = colors.onSurfaceVariant)
        }
    }
}