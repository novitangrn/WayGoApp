package com.example.waygo.datainject

import android.content.Context
import com.example.waygo.data.database.TourismDb
import com.example.waygo.repository.TourismRepo
import com.example.waygo.retrofit.ApiConfig


object Injection {
    fun provideRepository(context: Context): TourismRepo {
        val database = TourismDb.getDatabase(context)
        val apiService = ApiConfig.getService()
        return TourismRepo(database, apiService, context)
    }

}