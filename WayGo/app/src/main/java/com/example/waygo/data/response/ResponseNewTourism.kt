package com.example.waygo.data.response

import com.google.gson.annotations.SerializedName

data class ResponseNewTourism(

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)
