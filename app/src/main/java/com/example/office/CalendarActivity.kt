package com.example.office

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.office.ui.theme.AppTheme
import com.example.office.ui.theme.ThemeState

@Suppress("DEPRECATION")
class TodayTaskActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        setContent {
            val context = LocalContext.current
            val themeState = remember { ThemeState(context) }
            AppTheme(themeState) {
                TodayTaskScreen()
            }
        }
    }
}

@Composable
fun TodayTaskScreen() {
    val colors = MaterialTheme.colorScheme

    Scaffold(
        containerColor = colors.background,
        bottomBar = {
            BottomNavigationBar(selectedItem = NavigationItem.Calendar)
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            color = Color.Transparent
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {
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

@Composable
fun CalendarSection() {
    val colors = MaterialTheme.colorScheme

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "March 2023",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = colors.onBackground
            )
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primaryContainer,
                    contentColor = colors.onPrimaryContainer
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(text = "Timeline", fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val days = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
            val dates = listOf("24", "26", "27", "28", "29", "30", "31")

            for (i in days.indices) {
                val isToday = i == 0
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(if (isToday) colors.primary else Color.Transparent),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = days[i],
                            fontSize = 14.sp,
                            color = if (isToday) colors.onPrimary else colors.onSurfaceVariant
                        )
                        Text(
                            text = dates[i],
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isToday) colors.onPrimary else colors.onBackground
                        )
                    }
                }
            }
        }
    }
}

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
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "✅",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "Layerio Daily Task",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.onSurface
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "2 Meeting • 28 Task",
                fontSize = 16.sp,
                color = colors.onSurfaceVariant
            )
        }
    }
}

@Composable
fun TimeSlotTasks() {
    val timeSlots = listOf(
        TimeSlotTask("1 AM", "BoxSite Design System", listOf()),
        TimeSlotTask("4 AM", "Eliezer brand guideline &...", listOf("Flash Payment Hi-Fi Design")),
        TimeSlotTask("6 AM", "Ultraflag dashboard & landing...", listOf()),
        TimeSlotTask("8 AM", "Waves brand guideline &...", listOf())
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

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = timeSlot.time,
            fontSize = 18.sp,
            color = colors.onSurfaceVariant,
            modifier = Modifier.width(80.dp)
        )

        Column(modifier = Modifier.weight(1f)) {
            if (timeSlot.mainTask.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(colors.primary)
                        .padding(vertical = 12.dp, horizontal = 16.dp)
                ) {
                    Text(
                        text = timeSlot.mainTask,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.onPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            timeSlot.subTasks.forEach { subTask ->
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(colors.secondaryContainer)
                        .padding(vertical = 8.dp, horizontal = 12.dp)
                ) {
                    Text(
                        text = subTask,
                        fontSize = 14.sp,
                        color = colors.onSecondaryContainer
                    )
                }
            }
        }
    }
}

data class TimeSlotTask(
    val time: String,
    val mainTask: String,
    val subTasks: List<String>
)
