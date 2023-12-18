package com.example.waygo.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.waygo.data.paging.VacationPaging
import com.example.waygo.data.pref.UserModel
import com.example.waygo.data.pref.UserPreference
import com.example.waygo.data.response.AllTouristSpotsItem
import com.example.waygo.data.response.ErrorResponse
import com.example.waygo.data.retrofit.ApiService
import com.example.waygo.helper.Result
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow

class Repository  public constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference,
){

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun registerUser(
        name: String,
        email: String,
        password: String

    ) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.register(name, email, password)
            if (response.isSuccessful) {
                emit(Result.Success(response.body()!!))
            } else {
                val errorResponse = Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                emit(Result.Error(errorResponse.message.toString()))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun setLogin(email : String, password: String) = liveData{
        emit(Result.Loading)
        try {
            val response = apiService.login(email, password)
            if (response.isSuccessful) {
                emit(Result.Success(response.body()!!))
            } else {
                val errorResponse = Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                emit(Result.Error(errorResponse.message.toString()))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }
//


    fun getAllTourist(): LiveData<PagingData<AllTouristSpotsItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                VacationPaging(apiService)
            }
        ).liveData
    }

    fun getDetailTourism(id: String) = liveData{
        emit(Result.Loading)
        try {
            val response = apiService.getDetailTourist(id)
            if (response.isSuccessful) {
                emit(Result.Success(response.body()!!))
            } else {
                val errorResponse = Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                emit(Result.Error(errorResponse.message.toString()))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getDetailUser(id :String) = liveData {
        emit(Result.Loading)
        try {
            val response =apiService.getDetailUser(id)
            if (response.isSuccessful)
            {
                Log.d(TAG, "getDetailSuccess: ${response.body()}")
                emit(Result.Success(response.body()!!))
            } else {
                val errorResponse = Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                Log.e(TAG, "getDetailError: $errorResponse", )
                emit(Result.Error(errorResponse.message.toString()))
            }
        }catch (e:Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    companion object{
        private const val TAG = "Repository"

        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference,
        ): Repository =
            instance ?: synchronized(this){
                instance ?: Repository(apiService, userPreference)
            }.also { instance = it }

        fun clearInstance(){
            instance = null
        }
    }
}