package com.example.office.presentation.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.office.data.repository.TaskRepository
import com.example.office.presentation.main.model.TaskItem
import kotlinx.coroutines.launch
import androidx.compose.runtime.*
import kotlin.collections.map


class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    var tasks by mutableStateOf<List<TaskItem>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun createTask(title: String, description: String) {
        viewModelScope.launch {
            repository.createTask(title, description)
            loadTasks() // обновляем список
        }
    }

    fun loadTasks() {
        viewModelScope.launch {
            isLoading = true
            try {
                tasks = repository.fetchTasks().map {
                    TaskItem(it.id, it.title, it.description, it.done)
                }
            } catch (e: Exception) {
                // Обработка ошибки (например, лог)
            }
            isLoading = false
        }
    }

    fun updateTask(id: Long, title: String, description: String, done: Boolean) {
        viewModelScope.launch {
            repository.updateTask(id, title, description, done)
        }
    }

    fun deleteTask(id: Long) {
        viewModelScope.launch {
            repository.deleteTask(id)
            loadTasks()
        }
    }
}