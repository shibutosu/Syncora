package com.example.office.presentation.login

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.office.data.repository.AuthRepository
import com.example.office.presentation.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.core.content.edit

@Composable
fun LoginScreen() {
    var isRegister by remember { mutableStateOf(false) }
    var fullName by remember { mutableStateOf(TextFieldValue("")) }
    var username by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var login by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var confirmPassword by remember { mutableStateOf(TextFieldValue("")) }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val colors = MaterialTheme.colorScheme
    val repository = remember { AuthRepository() }

    Scaffold(containerColor = colors.background) { paddingValues ->
        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            color = Color.Transparent
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(colors.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isRegister) "R" else "L",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.onPrimary
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = if (isRegister) "Create Account" else "Login to Your Account",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.onBackground
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (isRegister) {
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Full Name", color = colors.onSurfaceVariant) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )

                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Username", color = colors.onSurfaceVariant) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email", color = colors.onSurfaceVariant) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )
                } else {
                    OutlinedTextField(
                        value = login,
                        onValueChange = { login = it },
                        label = { Text("Login", color = colors.onSurfaceVariant) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )
                }

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password", color = colors.onSurfaceVariant) },
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(
                                imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (isPasswordVisible) "Hide password" else "Show password"
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )

                if (isRegister) {
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Confirm Password", color = colors.onSurfaceVariant) },
                        visualTransformation = if (isConfirmVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { isConfirmVisible = !isConfirmVisible }) {
                                Icon(
                                    imageVector = if (isConfirmVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = if (isConfirmVisible) "Hide password" else "Show password"
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (message != null) {
                    Text(
                        text = message ?: "",
                        color = if (message!!.contains("успешно", ignoreCase = true) || message!!.contains("success", ignoreCase = true)) Color(0xFF4CAF50) else Color.Red,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }

                if (isLoading) {
                    CircularProgressIndicator(color = colors.primary)
                } else {
                    Button(
                        onClick = {
                            message = null
                            if (isRegister) {
                                if (password.text != confirmPassword.text) {
                                    message = "Пароли не совпадают"
                                    return@Button
                                }
                                if (
                                    fullName.text.isBlank() ||
                                    username.text.isBlank() ||
                                    email.text.isBlank() ||
                                    password.text.isBlank()
                                ) {
                                    message = "Заполните все поля"
                                    return@Button
                                }
                                isLoading = true
                                CoroutineScope(Dispatchers.IO).launch {
                                    val result = repository.registerAndLogin(
                                        fullName = fullName.text,
                                        username = username.text,
                                        email = email.text,
                                        password = password.text
                                    )
                                    withContext(Dispatchers.Main) {
                                        isLoading = false
                                        result.fold(
                                            onSuccess = { authResponse ->
                                                // успешная регистрация и логин, сохрани токен и перейди в MainActivity
                                                context.getSharedPreferences("auth", 0).edit {
                                                    putString("jwt", authResponse.token)
                                                }
                                                message = "Регистрация и вход прошли успешно!"
                                                context.startActivity(Intent(context, MainActivity::class.java))
                                            },
                                            onFailure = { e ->
                                                message = e.localizedMessage ?: "Ошибка регистрации"
                                            }
                                        )
                                    }
                                }
                            } else {
                                if (login.text.isBlank() || password.text.isBlank()) {
                                    message = "Введите логин и пароль"
                                    return@Button
                                }
                                isLoading = true
                                CoroutineScope(Dispatchers.IO).launch {
                                    val response = repository.login(login.text, password.text)
                                    withContext(Dispatchers.Main) {
                                        isLoading = false
                                        if (response.token != null) {
                                            context.getSharedPreferences("auth", 0).edit {
                                                putString("jwt", response.token)
                                            }
                                            message = "Вход выполнен успешно!"
                                            context.startActivity(Intent(context, MainActivity::class.java))
                                        } else {
                                            message = response.message ?: "Ошибка входа"
                                        }
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .height(50.dp),
                        shape = RoundedCornerShape(25.dp)
                    ) {
                        Text(
                            text = if (isRegister) "Register" else "Login",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(onClick = {
                    isRegister = !isRegister
                    message = null
                }) {
                    Text(
                        text = if (isRegister) "Already have an account? Login" else "Don't have an account? Register",
                        color = colors.primary
                    )
                }

                if (!isRegister) {
                    TextButton(onClick = {
                        message = "Функция восстановления пока недоступна"
                    }) {
                        Text(
                            text = "Забыли пароль?",
                            color = colors.primary,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}