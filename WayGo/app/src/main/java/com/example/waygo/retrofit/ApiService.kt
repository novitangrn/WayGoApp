package com.example.waygo.retrofit

import com.example.waygo.data.model.LoginUser
import com.example.waygo.data.model.SignupUser
import com.example.waygo.data.response.ResponseDetail
import com.example.waygo.data.response.ResponseLoginUser
import com.example.waygo.data.response.ResponseNewTourism
import com.example.waygo.data.response.ResponseSignupUser
import com.example.waygo.data.response.ResponseTourism
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("register")
    fun signupUser(@Body signupUser: SignupUser): Call<ResponseSignupUser>

    @POST("login")
    fun loginUser(@Body loginUser: LoginUser): Call<ResponseLoginUser>


    @GET("stories")
    fun getStories(
        @Header("Authorization") token: String,
        @Query("location") location: Int = 1,
    ): Call<ResponseTourism>

    @GET("stories")
    suspend fun getStoriesWithPaging(
        @Header("Authorization") token: String,
        @Query("page") page: Int?,
        @Query("location") location: Int = 0,
    ): Response<ResponseTourism>

    @GET("stories/{id}")
    fun getDetail(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<ResponseDetail>

    @Multipart
    @POST("stories")
    fun upStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Header("Authorization") token: String? = null
    ): Call<ResponseNewTourism>

//food end point
//    @GET("stories")
//    suspend fun getFoodWithPaging(
//        @Header("Authorization") token: String,
//        @Query("page") page: Int?,
//        @Query("location") location: Int = 0,
//    ): Response<ResponseFood>

//    @GET("stories/{id}")
//    fun getDetail(
//        @Header("Authorization") token: String,
//        @Path("id") id: String
//    ): Call<ResponseFoodDetail>
//
//    @Multipart
//    @POST("stories")
//    fun upStory(
//        @Part file: MultipartBody.Part,
//        @Part("description") description: RequestBody,
//        @Header("Authorization") token: String? = null
//    ): Call<ResponseNewFood>
}
