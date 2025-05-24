package com.example.office.data.remote.dto

data class UpdateTaskRequest(
    val title: String,
    val description: String,
    val done: Boolean
)