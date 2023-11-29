package com.example.waygo.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class SessionUser (var api_key: String? = null) : Parcelable
