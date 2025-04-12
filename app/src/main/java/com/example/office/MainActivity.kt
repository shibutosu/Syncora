package com.example.office

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.office.ui.theme.AppTheme
import com.example.office.ui.theme.ThemeState

@Suppress("DEPRECATION")
class MainActivity : ComponentActivity() {
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
                MainScreen(themeState)
            }
        }
    }
}

// Theme check ✅
@Composable
fun StudioHeader() {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "L",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            Column {
                Text(
                    text = "Layerio Studio",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Education plan - 4 Member",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }
        }

        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = "Filters",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}


@Composable
fun DailyTasksCard(tasksDone: Int, tasksTotal: Int) {
    val progress = if (tasksTotal > 0) tasksDone.toFloat() / tasksTotal else 0f

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFF282828))
                .padding(16.dp),
        ) {
            Column {
                Text(
                    text = "Daily tasks",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "$tasksDone out of $tasksTotal done",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(40.dp)
            ) {
                Canvas(modifier = Modifier.size(40.dp)) {
                    val strokeWidth = 4.dp.toPx()
                    drawArc(
                        color = Color.Gray,
                        startAngle = 0f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                        size = Size(size.width, size.height)
                    )

                    drawArc(
                        color = Color.White,
                        startAngle = -90f,
                        sweepAngle = 360 * progress,
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                        size = Size(size.width, size.height)
                    )
                }
                Text(
                    text = "${(progress * 100).toInt()}%",
                    fontSize = 10.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


@Composable
fun PriorityTaskCard() {
    val tasks = remember { mutableStateListOf(
        Task("Doing exercise 45 min", true),
        Task("Doing meditation"),
        Task("Read a self improvement book"),
        Task("Buy coffee in the shop")
    ) }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFC8A3D6))
            .padding(16.dp)
            .fillMaxHeight()
    ) {
        Column {
            Text(
                text = "My Priority Task",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF836190),
            )

            tasks.forEach { task ->
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Кастомный чекбокс
                    val isChecked = remember { mutableStateOf(task.done) }
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (isChecked.value) Color(0xFF553B59) else Color.Transparent)
                            .border(1.5.dp, Color.DarkGray, RoundedCornerShape(4.dp))
                            .clickable {
                                isChecked.value = !isChecked.value
                                task.done = isChecked.value
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isChecked.value) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Checked",
                                tint = Color.White,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = task.title,
                        fontSize = 14.sp,
                        color = if (task.done) Color.DarkGray else Color.Black,
                        style = if (task.done) androidx.compose.ui.text.TextStyle(textDecoration = TextDecoration.LineThrough)
                        else androidx.compose.ui.text.TextStyle(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 40.dp)
                    )
                }
            }
        }

        IconButton(
            onClick = { /* TODO */ },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(32.dp)
                .clip(RoundedCornerShape(50))
                .background(Color.Black)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowOutward,
                contentDescription = "Open",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}


data class Task(val title: String, var done: Boolean = false)

@Composable
fun WorkspaceCard(modifier: Modifier = Modifier) {
    val sharedWorkspaces = listOf(
        "✅ Layerio Daily",
        "🌎 Our project",
        "💼 Layeriotion"
    )

    val privateWorkspaces = listOf(
        "Human Resource",
        "Employee Salary"
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFEADABA))
            .padding(16.dp)
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column {
            Text(
                text = "Shared Workspace",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFBDAB8C)
            )

            sharedWorkspaces.forEach { workspace ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth() // Растягиваем Row на всю ширину
                        .padding(vertical = 4.dp)
                ) {
                    Text(
                        text = workspace,
                        fontSize = 16.sp,
                        color = Color.Black,
                        maxLines = 1, // Ограничиваем текст одной строкой
                        overflow = TextOverflow.Ellipsis, // Добавляем многоточие при обрезке
                        modifier = Modifier
                            .weight(1f) // Текст занимает оставшееся пространство
                            .padding(end = 10.dp) // Отступ справа, чтобы не заходить под кнопку
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Private Workspace",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFBDAB8C)
            )

            privateWorkspaces.forEach { workspace ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth() // Растягиваем Row на всю ширину
                        .padding(vertical = 4.dp)
                ) {
                    Text(
                        text = workspace,
                        fontSize = 16.sp,
                        color = Color.Black,
                        maxLines = 1, // Ограничиваем текст одной строкой
                        overflow = TextOverflow.Ellipsis, // Добавляем многоточие при обрезке
                        modifier = Modifier
                            .weight(1f) // Текст занимает оставшееся пространство
                            .padding(end = 10.dp) // Отступ справа, чтобы не заходить под кнопку
                    )
                }
            }
        }

        IconButton(
            onClick = { /* TODO */ },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(32.dp)
                .clip(RoundedCornerShape(50))
                .background(Color.Black)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowOutward,
                contentDescription = "Open",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}


@Composable
fun ToDoList() {
    val tasks = remember {
        mutableStateListOf(
            TaskItem("Blockchain system redesign web page", true),
            TaskItem("Eliezer brand guideline & landing page", true),
            TaskItem("Quantumn frontend development", false),
            TaskItem("Blockchain system redesign web page", true),
            TaskItem("Eliezer brand guideline & landing page", true),
            TaskItem("Quantumn frontend development", false)

        )
    }

    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Text(
            text = "To do today",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))

        tasks.forEach { task ->
            val isChecked = remember { mutableStateOf(task.done) }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                if (isChecked.value) MaterialTheme.colorScheme.primary else Color.Transparent
                            )
                            .border(1.5.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(4.dp))
                            .clickable {
                                isChecked.value = !isChecked.value
                                task.done = isChecked.value
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isChecked.value) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Checked",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 10.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        text = task.title,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        textDecoration = if (task.done) TextDecoration.LineThrough else TextDecoration.None
                    )
                }
            }
        }
    }
}

data class TaskItem(val title: String, var done: Boolean)

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
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp) // Можно убрать или сделать динамичным
        ) {
            Row(
                modifier = Modifier.fillMaxSize()
            ) {
                // Левая колонка
                Column(
                    modifier = Modifier
                        .weight(4f)
                        .fillMaxHeight()
                ) {
                    // PriorityTaskCard
                    Box(
                        modifier = Modifier
                            .weight(5f)
                            .fillMaxHeight()
                    ) {
                        PriorityTaskCard()
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // DailyTasksCard
                    Box(
                        modifier = Modifier
                            .weight(3f)
                            .fillMaxHeight()
                    ) {
                        DailyTasksCard(tasksDone = 8, tasksTotal = 10)
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Правая колонка
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


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(themeState: ThemeState) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedItem = NavigationItem.Home
            )
        },
        containerColor = MaterialTheme.colorScheme.background
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
            ) {
                // НЕскроллируемый заголовок
                StudioHeader()

                // Скроллируемый контент
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = paddingValues.calculateBottomPadding())
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    DashboardScreen()
                    Spacer(modifier = Modifier.height(10.dp))
                    ToDoList()
                }
            }
        }
    }
}