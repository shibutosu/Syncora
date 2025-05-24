package com.example.office.data.remote.api

import com.example.office.presentation.profile.model.User
import retrofit2.http.GET
import retrofit2.http.Header

interface UserApi {
    @GET("/api/user/me")
    suspend fun getProfile(@Header("Authorization") token: String): User
}