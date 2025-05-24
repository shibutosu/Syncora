package com.example.office

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.office.presentation.login.LoginActivity
import com.example.office.presentation.main.MainActivity

class App : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPrefs = getSharedPreferences("auth", MODE_PRIVATE)
        val token = sharedPrefs.getString("jwt", null)

        val destination = if (token.isNullOrEmpty()) {
            LoginActivity::class.java
        } else {
            MainActivity::class.java
        }

        startActivity(Intent(this, destination))
        finish() // Чтобы нельзя было вернуться назад
    }
}