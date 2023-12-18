package com.example.waygo.ui.detail

import androidx.lifecycle.ViewModel
import com.example.waygo.data.Repository

class DetailViewModel(private val repository: Repository): ViewModel() {

    fun getTouristById(id: String) = repository.getDetailTourism(id)
}