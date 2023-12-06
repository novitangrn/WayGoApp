package com.example.waygo.data.response

import com.google.gson.annotations.SerializedName

data class ResponseTourism(
    @field:SerializedName("listStory")
    val listStory: List<TourismItems>,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

