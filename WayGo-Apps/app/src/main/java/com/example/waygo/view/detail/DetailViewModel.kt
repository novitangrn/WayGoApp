package com.example.waygo.view.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.waygo.data.response.ResponseDetail
import com.example.waygo.data.response.Tourism
import com.example.waygo.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel  : ViewModel() {

    private val _nameNotes = MutableLiveData<String?>()
    val nameNotes: LiveData<String?> = _nameNotes
    private val _imageNotes = MutableLiveData<String?>()
    val imageNotes: LiveData<String?> = _imageNotes
    private val _descriptionNotes = MutableLiveData<String?>()
    val descriptionNotes: LiveData<String?> = _descriptionNotes
    private val _longNotes = MutableLiveData<String>()
    val longNotes: LiveData<String?> = _longNotes
    private val _latNotes = MutableLiveData<String>()
    val latNotes: LiveData<String?> = _latNotes
    private val _catNotes = MutableLiveData<String?>()
    val catNotes: LiveData<String?> = _catNotes
    private val _toastMsg = MutableLiveData<String>()
    val toastMsg: LiveData<String> = _toastMsg
    private val _showToast = MutableLiveData<Boolean>()
    val showToast: LiveData<Boolean> = _showToast
    fun getDetail(id: String, token: String) {
        val client = ApiConfig.getService().getDetail("Bearer $token", id)
        client.enqueue(object : Callback<ResponseDetail> {
            override fun onResponse(
                call: Call<ResponseDetail>,
                response: Response<ResponseDetail>,
            ) {
                try {
                    val respon = response.body()
                    if (respon?.error == false && response.isSuccessful) {
                        setDetail(respon.story)
                    }
                } catch (e: Exception) {
                    _showToast.value = true
                    _toastMsg.value = e.toString()
                }
            }

            override fun onFailure(call: Call<ResponseDetail>, t: Throwable) {
                _showToast.value = true
                _toastMsg.value = t.localizedMessage
            }

        })
    }

    fun setDetail(tourism: Tourism?) {
        _nameNotes.value = tourism?.name
        _latNotes.value = tourism?.lat.toString()
        _longNotes.value = tourism?.lon.toString()
        _catNotes.value = tourism?.createdAt
        _imageNotes.value = tourism?.photoUrl
        _descriptionNotes.value = tourism?.description
    }
}