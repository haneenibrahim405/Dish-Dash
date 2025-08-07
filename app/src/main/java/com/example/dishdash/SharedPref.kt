package com.example.dishdash

import android.content.Context
import android.content.SharedPreferences

class SharedPref(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("dishdash_prefs", Context.MODE_PRIVATE)

    fun setLogin(email: String) {
        prefs.edit()
            .putString("user_email", email)
            .putBoolean("is_logged_in", true)
            .apply()
    }

    fun isLoggedIn(): Boolean = prefs.getBoolean("is_logged_in", false)

    fun getUserEmail(): String? = prefs.getString("user_email", null)

    fun logout() {
        prefs.edit().clear().apply()
    }
}
