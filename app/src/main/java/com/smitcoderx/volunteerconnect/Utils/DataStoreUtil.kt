package com.smitcoderx.volunteerconnect.Utils

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.smitcoderx.volunteerconnect.Utils.Constants.ID
import com.smitcoderx.volunteerconnect.Utils.Constants.LOGGEDIN
import com.smitcoderx.volunteerconnect.Utils.Constants.ROLE
import com.smitcoderx.volunteerconnect.Utils.Constants.TOKEN

class DataStoreUtil(context: Context) {

    private var prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)


    fun setLoggedIn(loggedIn: Boolean) {
        prefs.edit().putBoolean(LOGGEDIN, loggedIn).apply()
    }

    fun getLoggedIn(): Boolean {
        return prefs.getBoolean(LOGGEDIN, false)
    }

    fun setRole(role: String?) {
        prefs.edit().putString(ROLE, role).apply()
    }

    fun getRole(): String? {
        return prefs.getString(ROLE, "")
    }

    fun setToken(token: String?) {
        prefs.edit().putString(TOKEN, "Bearer $token").apply()
    }

    fun getToken(): String? {
        return prefs.getString(TOKEN, "")
    }

    fun setID(id: String?) {
        return prefs.edit().putString(ID, id).apply()
    }

    fun getID(): String? {
        return prefs.getString(ID, "")
    }


    fun logoutUser() {
        prefs.edit().clear().apply()
    }

}