package com.example.waygo.view.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.waygo.data.response.ResponseTourism
import com.example.waygo.data.response.TourismItems
import com.example.waygo.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapViewModel : ViewModel() {
    private val _data = MutableLiveData<List<TourismItems>>()
    val data: LiveData<List<TourismItems>> = _data

    fun getLocation(apikey: String) {
        val client = ApiConfig.getService().getStories("Bearer $apikey")
        client.enqueue(object : Callback<ResponseTourism> {
            override fun onResponse(call: Call<ResponseTourism>, response: Response<ResponseTourism>) {
                if (response.isSuccessful && response.body()?.error == false) {
                    val body = response.body()
                    _data.postValue(body?.listStory)
                    Log.d("GETLOC", body?.listStory.toString())
                }
            }

            override fun onFailure(call: Call<ResponseTourism>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}