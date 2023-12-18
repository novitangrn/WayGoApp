package com.example.waygo.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.waygo.data.Repository

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class HomeViewModel(private val repository: Repository) : ViewModel() {

    fun getAllTourist() = repository.getAllTourist().cachedIn(viewModelScope)

    suspend fun getSession() {
        repository.getSession().first()
    }
}