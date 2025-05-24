package com.example.office.data.repository

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.office.data.remote.api.ApiService
import com.example.office.data.remote.api.TaskApiService
import com.example.office.data.remote.dto.CreateTaskRequest
import com.example.office.data.remote.dto.TaskDto
import com.example.office.data.remote.dto.UpdateTaskRequest

class TaskRepository(
    private val api: ApiService,
    private val tokenProvider: () -> String
) {
    suspend fun fetchTasks(): List<TaskDto> {
        val token = tokenProvider()
        return api.getTasks("Bearer $token")
    }
    suspend fun createTask(title: String, description: String) {
        val token = tokenProvider()
        val request = CreateTaskRequest(title, description)
        api.createTask("Bearer $token", request)
    }

    suspend fun updateTask(id: Long, title: String, description: String, done: Boolean) {
        val token = tokenProvider()
        val request = UpdateTaskRequest(title, description, done)
        api.updateTask("Bearer $token", id, request)
    }

    suspend fun deleteTask(id: Long) {
        val token = tokenProvider()
        api.deleteTask("Bearer $token", id)
    }
}