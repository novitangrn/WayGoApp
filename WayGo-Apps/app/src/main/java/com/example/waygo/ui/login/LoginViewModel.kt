package com.example.waygo.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.waygo.data.Repository
import com.example.waygo.data.pref.UserModel
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: Repository
) : ViewModel() {

    fun setLogin(email : String, password : String) = repository.setLogin(email, password)

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}