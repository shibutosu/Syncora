package com.example.office.presentation.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.office.data.remote.api.UserApi
import com.example.office.presentation.profile.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val api = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080") // или твой сервер
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(UserApi::class.java)

    fun loadProfile(token: String) {
        viewModelScope.launch {
            try {
                _user.value = api.getProfile("Bearer $token")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}