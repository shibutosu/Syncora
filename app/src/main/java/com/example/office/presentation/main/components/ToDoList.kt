package com.example.office.presentation.main.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.office.data.remote.ApiClient
import com.example.office.data.repository.TaskRepository
import com.example.office.presentation.main.viewmodel.TaskViewModel
import com.example.office.presentation.main.viewmodel.TaskViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun ToDoList() {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var editingTaskId by remember { mutableStateOf<Long?>(null) }
    var newTitle by remember { mutableStateOf("") }
    var newDescription by remember { mutableStateOf("") }
    var editingTaskDone by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val tokenProvider = {
        context.getSharedPreferences("auth", 0).getString("jwt", "") ?: ""
    }

    val viewModel: TaskViewModel = viewModel(
        factory = TaskViewModelFactory(
            repository = TaskRepository(
                api = ApiClient.api,
                tokenProvider = tokenProvider
            )
        )
    )

    val tasks by remember { derivedStateOf { viewModel.tasks } }
    val isLoading by remember { derivedStateOf { viewModel.isLoading } }

    val backgroundColor = MaterialTheme.colorScheme.background
    val taskBackground = MaterialTheme.colorScheme.surfaceVariant
    val taskTextColor = MaterialTheme.colorScheme.onSurface
    val hintTextColor = MaterialTheme.colorScheme.outline

    LaunchedEffect(Unit) {
        viewModel.loadTasks()
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(backgroundColor)) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Text("To do today", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = taskTextColor)
            Spacer(modifier = Modifier.height(8.dp))

            when {
                isLoading -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Загрузка...", color = hintTextColor)
                }

                tasks.isEmpty() -> Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text("У вас пока нет задач", color = hintTextColor)
                    Spacer(modifier = Modifier.height(80.dp))
                }

                else -> {
                    tasks.forEach { task ->
                        var isChecked by remember { mutableStateOf(task.done) }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .background(taskBackground, RoundedCornerShape(25.dp))
                                .clickable {
                                    editingTaskId = task.id
                                    newTitle = task.title
                                    newDescription = task.description
                                    showDialog = true
                                }
                                .padding(2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = { checked ->
                                    isChecked = checked
                                    coroutineScope.launch {
                                        viewModel.updateTask(task.id, task.title, task.description, checked)
                                    }
                                }
                            )

                            Spacer(modifier = Modifier.width(2.dp))

                            Text(
                                text = task.title,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = taskTextColor,
                                textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(55.dp))
                }
            }
        }

        FloatingActionButton(
            onClick = {
                showDialog = true
                editingTaskId = null
                newTitle = ""
                newDescription = ""
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Добавить")
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = {
                    Text(if (editingTaskId == null) "Новая задача" else "Редактировать задачу")
                },
                text = {
                    Column {
                        OutlinedTextField(
                            value = newTitle,
                            onValueChange = { newTitle = it },
                            label = { Text("Название") },
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = newDescription,
                            onValueChange = { newDescription = it },
                            label = { Text("Описание") },
                            maxLines = 3
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (editingTaskId == null) {
                            viewModel.createTask(newTitle, newDescription)
                            Toast.makeText(context, "Задача добавлена", Toast.LENGTH_SHORT).show()
                        } else {
                            viewModel.updateTask(editingTaskId!!, newTitle, newDescription, editingTaskDone)
                            Toast.makeText(context, "Изменения сохранены", Toast.LENGTH_SHORT).show()
                        }

                        viewModel.loadTasks()
                        showDialog = false
                        newTitle = ""
                        newDescription = ""
                        editingTaskId = null
                    }) {
                        Text(if (editingTaskId == null) "Добавить" else "Изменить")
                    }
                },
                dismissButton = {
                    Row {
                        TextButton(onClick = {
                            showDialog = false
                            newTitle = ""
                            newDescription = ""
                            editingTaskId = null
                        }) {
                            Text("Отмена")
                        }
                        if (editingTaskId != null) {
                            TextButton(onClick = {
                                editingTaskId?.let {
                                    viewModel.deleteTask(it)
                                    Toast.makeText(context, "Задача удалена", Toast.LENGTH_SHORT).show()
                                }
                                showDialog = false
                                editingTaskId = null
                            }) {
                                Text("Удалить", color = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            )
        }
    }
}