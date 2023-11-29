package com.example.waygo.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.waygo.data.model.LoginUser
import com.example.waygo.data.response.ResponseLoginUser
import com.example.waygo.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel: ViewModel() {
    private val buttonEnable = MutableLiveData<Boolean>()
    val enableBtn: LiveData<Boolean> = buttonEnable

    private val loginFail = MutableLiveData<Boolean>()
    val isLoginFail: LiveData<Boolean> = loginFail

    private val msgResponse = MutableLiveData<String>()
    val responseMsg: LiveData<String> = msgResponse

    private val apiKeys = MutableLiveData<String>()
    val apiKey: LiveData<String> = apiKeys

    private val load = MutableLiveData<Boolean>()
    val isLoad: LiveData<Boolean> = load

    private val toast = MutableLiveData<Boolean>()
    val isToast: LiveData<Boolean> = toast

    fun login(login: LoginUser) {
        val client = ApiConfig.getService()
            .loginUser(login)
        buttonEnable.value = false
        load.value = true
        client.enqueue(object : Callback<ResponseLoginUser> {
            override fun onResponse(
                call: Call<ResponseLoginUser>,
                response: Response<ResponseLoginUser>
            ) {
                try {
                    val body = response.body()
                    if (response.isSuccessful && body != null) {
                        if (body.error == false) {
                            apiKeys.value = body.loginResult?.token
                            loginFail.value = false
                            buttonEnable.value = true
                            load.value = false
                        } else {
                            loginFail.value = true
                            buttonEnable.value = true
                            load.value = false
                            toast.value = true
                            msgResponse.value = body.message
                            toast.value = false
                        }
                    } else {
                        loginFail.value = true
                        toast.value = true
                        msgResponse.value = body?.message
                        toast.value = false
                        buttonEnable.value = true
                        load.value = false
                    }
                } catch (e: Exception) {
                    loginFail.value = true
                    toast.value = true
                    msgResponse.value = e.toString()
                    toast.value = false
                    buttonEnable.value = true
                    load.value = false
                }
            }

            override fun onFailure(call: Call<ResponseLoginUser>, t: Throwable) {
                loginFail.value = true
                toast.value = true
                msgResponse.value = t.localizedMessage
                toast.value = false
                buttonEnable.value = true
                load.value = false
            }

        })
    }

}