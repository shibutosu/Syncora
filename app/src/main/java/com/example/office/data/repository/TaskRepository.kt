package com.example.office.data.repository

import com.example.office.data.remote.api.ApiService
import com.example.office.data.remote.dto.CreateTaskRequest
import com.example.office.data.remote.dto.TaskDto
import com.example.office.data.remote.dto.UpdateTaskRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TaskRepository(
    private val api: ApiService,
    private val tokenProvider: suspend () -> String
) {
    private suspend fun getAuthHeader(): String {
        val token = tokenProvider().trim()
        require(token.isNotBlank()) { "Token is blank or missing!" }
        return "Bearer $token"
    }

    suspend fun fetchTasks(): List<TaskDto> = withContext(Dispatchers.IO) {
        api.getTasks(getAuthHeader())
    }

    suspend fun createTask(title: String, description: String) = withContext(Dispatchers.IO) {
        val request = CreateTaskRequest(title, description)
        api.createTask(getAuthHeader(), request)
    }

    suspend fun updateTask(id: Long, title: String, description: String, done: Boolean) = withContext(Dispatchers.IO) {
        val request = UpdateTaskRequest(title, description, done)
        api.updateTask(getAuthHeader(), id, request)
    }

    suspend fun deleteTask(id: Long) = withContext(Dispatchers.IO) {
        api.deleteTask(getAuthHeader(), id)
    }
}