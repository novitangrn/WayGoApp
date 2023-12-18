package com.example.waygo.ui.rundown

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.waygo.data.Repository
import kotlinx.coroutines.flow.first

class CustomRundownVM(private val repository: Repository) : ViewModel() {

    suspend fun getSession() {
        repository.getSession().first()
    }
}