package com.example.waygo.ui.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.waygo.data.Repository
import com.example.waygo.data.pref.UserModel
import kotlinx.coroutines.launch

class WelcomeViewModel(val repository: Repository): ViewModel() {

    fun login(email: String, password: String) = repository.setLogin(email, password)

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}