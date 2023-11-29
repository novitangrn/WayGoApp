package com.example.waygo

import android.content.Context
import com.example.waygo.data.SessionUser

internal class SessionPrefs (context: Context) {
    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    fun setToken(value: SessionUser) {
        val editor = preferences.edit()
        editor.putString(API_KEY_NAME, value.api_key)
        editor.apply()
    }

    fun getToken(): SessionUser {
        val model = SessionUser()
        model.api_key = preferences.getString(API_KEY_NAME, null)
        return model
    }

    companion object {
        var PREFS_NAME = "token"
        var API_KEY_NAME = "api_key"
    }
}