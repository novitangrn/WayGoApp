package com.example.waygo

import android.content.Context
import com.example.waygo.data.Repository
import com.example.waygo.data.pref.UserPreference
import com.example.waygo.data.pref.dataStore
import com.example.waygo.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): Repository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.accessToken)
        return Repository.getInstance(apiService, pref)
    }
}