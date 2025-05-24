package com.example.office.presentation.calendar.model

data class TimeSlotTask(
    val time: String,
    val mainTask: String,
    val subTasks: List<String>
)