package com.example.waygo.view.tourism

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.waygo.data.response.ResponseNewTourism
import com.example.waygo.retrofit.ApiConfig
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddTourismVM : ViewModel() {

    private val toast = MutableLiveData<String?>()
    val toastMsg: LiveData<String?> = toast

    private val showToasts = MutableLiveData<Boolean>()
    val showToast: LiveData<Boolean> = showToasts

    private val showLoad = MutableLiveData<Boolean>()
    val showLoading: LiveData<Boolean> = showLoad

    private val fail = MutableLiveData<Boolean>()
    val failed: LiveData<Boolean> = fail

    fun uploadStory(file: MultipartBody.Part, description: RequestBody, token: String) {
        showLoad.value = true
        val client =
            ApiConfig.getService()
                .upStory(token = token, file = file, description = description)
        client.enqueue(object : Callback<ResponseNewTourism> {
            override fun onResponse(
                call: Call<ResponseNewTourism>,
                response: Response<ResponseNewTourism>
            ) {
                showLoad.value = false
                if (response.isSuccessful) {
                    val body = response.body()
                    fail.value = body?.error
                    if (body?.error == false) {
                        showToasts.value = true
                        toast.value = response.body()?.message
                    } else {
                        showToasts.value = true
                        toast.value = "Kabel fiber nya dimakan Hiu"
                    }
                }
            }

            override fun onFailure(call: Call<ResponseNewTourism>, t: Throwable) {
                showLoad.value = false
                showToasts.value = true
                toast.value = t.toString()
            }

        })
    }
}