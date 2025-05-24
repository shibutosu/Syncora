package com.example.office.data.repository

import com.example.office.data.remote.api.ApiService
import com.example.office.data.remote.dto.AuthResponse
import com.example.office.data.remote.dto.LoginRequest
import com.example.office.data.remote.dto.RegisterRequest
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

// --- Репозиторий ---
class AuthRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080") // Замени на свой IP, если тестируешь на устройстве
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(ApiService::class.java)

    // Регистрация (только регистрация)
    suspend fun register(
        fullName: String = "",
        username: String,
        email: String,
        password: String,
        phoneNumber: String = "",
        department: String = "",
        position: String = "",
        employeeCode: String = ""
    ): Result<String> {
        val request = RegisterRequest(
            fullName = fullName,
            username = username,
            password = password,
            email = email,
            phoneNumber = phoneNumber,
            department = department,
            position = position,
            employeeCode = employeeCode
        )
        return try {
            val response = api.register(request)
            if (response.isSuccessful) {
                Result.success(response.body() ?: "Регистрация прошла успешно")
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMsg = try {
                    JSONObject(errorBody ?: "").optString("message", "Ошибка регистрации")
                } catch (e: Exception) {
                    errorBody ?: "Ошибка регистрации"
                }
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        } as Result<String>
    }

    // Вход
    suspend fun login(
        username: String,
        password: String
    ): AuthResponse {
        return try {
            val response = api.login(LoginRequest(username, password))
            response.body() ?: AuthResponse(null, "Ошибка авторизации")
        } catch (e: Exception) {
            AuthResponse(null, e.localizedMessage ?: "Ошибка авторизации")
        }
    }

    // Регистрация и сразу вход
    suspend fun registerAndLogin(
        fullName: String = "",
        username: String,
        email: String,
        password: String,
        phoneNumber: String = "",
        department: String = "",
        position: String = "",
        employeeCode: String = ""
    ): Result<AuthResponse> {
        val regResult = register(fullName, username, email, password, phoneNumber, department, position, employeeCode)
        if (regResult.isSuccess) {
            val loginResponse = login(username, password)
            if (loginResponse.token != null) {
                return Result.success(loginResponse)
            } else {
                return Result.failure(Exception(loginResponse.message ?: "Ошибка авторизации"))
            }
        } else {
            return Result.failure(Exception(regResult.exceptionOrNull()?.localizedMessage ?: "Ошибка регистрации"))
        }
    }
}