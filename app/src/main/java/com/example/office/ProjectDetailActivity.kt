package com.example.office

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
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.office.presentation.common.components.BottomNavigationBar
import com.example.office.presentation.common.components.HeaderNavigationBar
import com.example.office.presentation.common.components.NavigationItem


@Suppress("DEPRECATION")
class ProjectDetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false

        setContent {
            ProjectDetailsScreen()
        }
    }
}

@Composable
fun ProjectDetailsScreen() {
    Scaffold(
        containerColor = Color(0xFF161616)
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            color = Color.Transparent
        ) {
            Column {
                HeaderNavigationBar("Project Details", true)
                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp)
                ){
                    Spacer(modifier = Modifier.height(26.dp))
                    ProjectTitle()
                    Spacer(modifier = Modifier.height(26.dp))
                    TeamMembers()
                    Spacer(modifier = Modifier.height(16.dp))
                    ProjectInfo()
                    Spacer(modifier = Modifier.height(16.dp))
                    TodoHeader()
                    Spacer(modifier = Modifier.height(16.dp))
                    ProjectToDoList()
                }

            }
        }
    }
}



@Composable
fun ProjectTitle() {
    Text(
        text = "💽 BoxSite Design System",
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White
    )
}

@Composable
fun Member(text: String) {
    Row {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(20.dp)
        ) {
            Canvas(modifier = Modifier.size(40.dp)) {
                drawArc(
                    color = Color.White,
                    startAngle = 1f,
                    sweepAngle = 360f,
                    useCenter = true,
                    style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round),
                    size = Size(size.width, size.height)
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = Color.White,
            fontSize = 16.sp
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TeamMembers() {
    val members = listOf(
        "Anto Utomo",
        "Evan Alex",
        "Alshad Armando"
    )
    Row {
        Text(
            text = "In project  :",
            fontSize = 20.sp,
            color = Color.Gray,
        )
        Spacer(modifier = Modifier.width(16.dp))
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            members.forEach { member ->
                Member(member)
                Spacer(modifier = Modifier.width(8.dp))
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
fun ProjectInfo() {
    Column {
        InfoItem("Dataline", "29 March 2023", Color.White)
        InfoItem("Status", "Doing✨", Color(0xFFF5A623))
        InfoItem("Service", "UIUX Design and Frontend dev", Color.White)
        InfoItem("Notes", "The development stage involves the implementation of the components.", Color.White)
    }
}

@Composable
fun InfoItem(label: String, value: String, valueColor: Color = Color.Black) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Divider(color = Color(0xFF282828), thickness = 1.dp)
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            // Фиксированная ширина для метки (например, 100.dp)
            Text(
                text = "$label:",
                modifier = Modifier.width(100.dp), // Выравнивание по ширине
                fontSize = 20.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Normal
            )

            Text(
                text = value,
                fontSize = 18.sp,
                color = valueColor,
                fontWeight = if (label == "Status") FontWeight.Bold else FontWeight.Normal
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun TodoHeader() {
    Text(
        text = "Project to-do",
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White
    )
}

@Composable
fun ProjectToDoList() {
    val tasks = remember { mutableStateListOf(
        ProjectTaskItem("Blockchain system redesign web page", true),
        ProjectTaskItem("Eliezer brand guideline & landing page", true),
        ProjectTaskItem("Quantumn frontend development", false),
        ProjectTaskItem("Blockchain system redesign web page", true),
        ProjectTaskItem("Eliezer brand guideline & landing page", true),
        ProjectTaskItem("Quantumn frontend development", false),
        ProjectTaskItem("Additional task 1", false),
        ProjectTaskItem("Additional task 2", true),
        ProjectTaskItem("Additional task 3", false),
    ) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()) // Добавляем вертикальный скролл
    ) {
        tasks.forEach { task ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color(0xFF2E2E2E))
                    .padding(horizontal = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Кастомный чекбокс
                    val isChecked = remember { mutableStateOf(task.done) }
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (isChecked.value) Color(0xFF9ED1C0) else Color.Transparent)
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
                                tint = Color.Black,
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
                        color = Color.White,
                        textDecoration = if (task.done) TextDecoration.LineThrough else TextDecoration.None
                    )
                }
            }
        }
    }
}

data class ProjectTaskItem(val title: String, var done: Boolean)