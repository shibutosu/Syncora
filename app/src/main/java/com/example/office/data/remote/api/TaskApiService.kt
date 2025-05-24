package com.example.office.data.remote.api

import com.example.office.data.remote.dto.TaskDto
import retrofit2.http.GET
import retrofit2.http.Header

interface TaskApiService {
    @GET("api/tasks")
    suspend fun getTasks(@Header("Authorization") token: String): List<TaskDto>
}