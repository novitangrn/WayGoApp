package com.example.waygo.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.waygo.data.Repository
import com.example.waygo.data.pref.UserModel

class SplashVm (private val repository: Repository): ViewModel() {

    fun getUser(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}