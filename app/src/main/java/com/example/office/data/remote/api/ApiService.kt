package com.example.office.data.remote.api

import com.example.office.data.remote.dto.AuthResponse
import com.example.office.data.remote.dto.CreateTaskRequest
import com.example.office.data.remote.dto.LoginRequest
import com.example.office.data.remote.dto.RegisterRequest
import com.example.office.data.remote.dto.TaskDto
import com.example.office.data.remote.dto.UpdateTaskRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("/auth/register")
    suspend fun register(@Body request: RegisterRequest): retrofit2.Response<AuthResponse>

    @POST("/auth/login")
    suspend fun login(@Body request: LoginRequest): retrofit2.Response<AuthResponse>

    @GET("api/tasks")
    suspend fun getTasks(@Header("Authorization") token: String): List<TaskDto>

    @POST("api/tasks")
    suspend fun createTask(
        @Header("Authorization") token: String,
        @Body request: CreateTaskRequest
    )

    @PATCH("api/tasks/{id}")
    suspend fun updateTask(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Body request: UpdateTaskRequest
    )

    @DELETE("api/tasks/{id}")
    suspend fun deleteTask(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    )
}