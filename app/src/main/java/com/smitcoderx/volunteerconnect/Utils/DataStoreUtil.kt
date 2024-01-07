package com.smitcoderx.volunteerconnect.Utils

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.smitcoderx.volunteerconnect.Utils.Constants.LOGGEDIN
import com.smitcoderx.volunteerconnect.Utils.Constants.TOKEN

class DataStoreUtil(context: Context) {

    private var prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun setLoggedIn(loggedIn: Boolean) {
        prefs.edit().putBoolean(LOGGEDIN, loggedIn).apply()
    }

    fun getLoggedIn(): Boolean {
        return prefs.getBoolean(LOGGEDIN, false)
    }

    fun setToken(token: String?) {
        prefs.edit().putString(TOKEN, "Bearer $token").apply()
    }

    fun getToken(): String? {
        return prefs.getString(TOKEN, "")
    }

    fun logoutUser() {
        prefs.edit().clear().apply()
    }

}