package com.example.covary.login.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("covary_prefs", Context.MODE_PRIVATE)

    fun setHasLoggedIn(value: Boolean) {
        prefs.edit().putBoolean("has_logged_in", value).apply()
    }

    fun hasLoggedIn(): Boolean {
        return prefs.getBoolean("has_logged_in", false)
    }
}