package com.example.waygo.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.waygo.data.response.TourismItems
import com.example.waygo.repository.TourismRepo

class MainVM (storyRepository: TourismRepo) : ViewModel() {
    val story: LiveData<PagingData<TourismItems>> = storyRepository.getTourism().cachedIn(viewModelScope)
}