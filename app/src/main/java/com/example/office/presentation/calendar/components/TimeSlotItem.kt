package com.example.office.presentation.calendar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.office.presentation.calendar.model.TimeSlotTask

@Composable
fun TimeSlotTasks() {
    val timeSlots = listOf(
        TimeSlotTask("1 AM", "BoxSite Design System", listOf()),
        TimeSlotTask("4 AM", "Eliezer brand guideline...", listOf("Flash Payment Hi-Fi Design")),
        TimeSlotTask("6 AM", "Ultraflag dashboard...", listOf()),
        TimeSlotTask("8 AM", "Waves brand guideline...", listOf())
    )

    LazyColumn {
        items(timeSlots) { timeSlot ->
            TimeSlotItem(timeSlot)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun TimeSlotItem(timeSlot: TimeSlotTask) {
    val colors = MaterialTheme.colorScheme

    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(timeSlot.time, fontSize = 18.sp, color = colors.onSurfaceVariant, modifier = Modifier.width(80.dp))
        Column(modifier = Modifier.weight(1f)) {
            if (timeSlot.mainTask.isNotEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp))
                        .background(colors.primary).padding(12.dp)
                ) {
                    Text(
                        timeSlot.mainTask,
                        fontSize = 16.sp,
                        color = colors.onPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            timeSlot.subTasks.forEach { subTask ->
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp))
                        .background(colors.secondaryContainer).padding(8.dp)
                ) {
                    Text(subTask, fontSize = 14.sp, color = colors.onSecondaryContainer)
                }
            }
        }
    }
}