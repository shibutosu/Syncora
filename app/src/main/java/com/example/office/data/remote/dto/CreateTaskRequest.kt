package com.example.office.data.remote.dto

data class CreateTaskRequest(
    val title: String,
    val description: String,
    val done: Boolean = false // 🔧 Добавить это поле
)