package com.example.office.data.remote.dto

data class RegisterRequest(
    val fullName: String = "",
    val username: String,
    val password: String,
    val email: String,
    val phoneNumber: String = "",
    val department: String = "",
    val position: String = "",
    val employeeCode: String = ""
)