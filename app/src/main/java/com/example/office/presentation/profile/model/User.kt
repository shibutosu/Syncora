package com.example.office.presentation.profile.model

data class User(
    val id: Long,
    val fullName: String,
    val username: String,
    val email: String,
    val phoneNumber: String,
    val department: String,
    val position: String,
    val employeeCode: String
)