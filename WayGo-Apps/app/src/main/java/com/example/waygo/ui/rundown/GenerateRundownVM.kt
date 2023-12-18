package com.example.waygo.ui.rundown

import androidx.lifecycle.ViewModel
import com.example.waygo.data.Repository
import kotlinx.coroutines.flow.first

class GenerateRundownVM(private val repository: Repository) : ViewModel() {

    suspend fun getSession() {
        repository.getSession().first()
    }
}