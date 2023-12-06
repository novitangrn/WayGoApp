package com.example.waygo.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.waygo.data.database.TourismDb
import com.example.waygo.data.response.TourismItems
import com.example.waygo.pagging.TourismPagging
import com.example.waygo.retrofit.ApiService

class TourismRepo (
    private val storyDatabase: TourismDb?,
    private val apiService: ApiService?,
    private val context: Context?,
) {
    fun getTourism(): LiveData<PagingData<TourismItems>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                TourismPagging(apiService!!, context)
            }
        ).liveData
    }
}