package com.example.office.data.remote.dto

data class TaskDto(
    val id: Long,
    val title: String,
    val description: String,
    val done: Boolean
)