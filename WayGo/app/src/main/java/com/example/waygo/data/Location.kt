package com.example.waygo.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Location(
    val name: String,
    val long: Double,
    val lang: Double
) : Parcelable

