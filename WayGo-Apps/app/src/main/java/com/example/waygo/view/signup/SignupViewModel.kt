package com.example.waygo.view.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.waygo.data.model.SignupUser
import com.example.waygo.data.response.ResponseSignupUser
import com.example.waygo.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupViewModel : ViewModel() {
    private val button = MutableLiveData<Boolean>()
    val enableButton: LiveData<Boolean> = button

    private val registerFail = MutableLiveData<Boolean>()
    val isRegisterUnsuccessful: LiveData<Boolean> = registerFail

    private val responsMessage = MutableLiveData<String>()
    val responseMessage: LiveData<String> = responsMessage

    private val load = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = load

    private val toast = MutableLiveData<Boolean>()
    val isToast: LiveData<Boolean> = toast

    fun signupUser(signupUser: SignupUser) {
        val client = ApiConfig.getService()
            .signupUser(signupUser)
        button.value = false
        load.value = true
        client.enqueue(object : Callback<ResponseSignupUser> {
            override fun onResponse(
                call: Call<ResponseSignupUser>,
                response: Response<ResponseSignupUser>
            ) {
                try {
                    val body = response.body()
                    if (response.isSuccessful && body != null) {
                        registerFail.value = body.error
                        button.value = true
                        load.value = false
                    } else {
                        registerFail.value = false
                        toast.value = true
                        responsMessage.value = body?.message
                        toast.value = false
                        button.value = true
                        load.value = false
                    }
                } catch (e: Exception) {
                    registerFail.value = false
                    toast.value = true
                    responsMessage.value = e.toString()
                    toast.value = false
                    button.value = true
                    load.value = false
                }
            }

            override fun onFailure(call: Call<ResponseSignupUser>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}