package com.dicoding.capstone.data.local

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import org.json.JSONObject

class UserPreference(context: Context?) {
    private val preferences: SharedPreferences =
        context!!.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "user_pref"
        private const val TOKEN = "token"
    }

    // Method to save the token
    fun setToken(token: String) {
        val editor = preferences.edit()
        editor.putString(TOKEN, token)
        editor.apply()
    }

    // Method to get the token
    fun getToken(): String? {
        return preferences.getString(TOKEN, null)
    }

    // Method to get the username from the token
    fun getUserName(): String? {
        val token = getToken() ?: return null
        return decodeToken(token)?.get("username") as String?
    }

    fun getEmail(): String? {
        val token = getToken() ?: return null
        return decodeToken(token)?.get("email") as String?
    }

    // Method to get the user role from the token
    fun getUserRole(): String? {
        val token = getToken() ?: return null
        return decodeToken(token)?.get("role") as String?
    }

    // Method to logout (clear the token)
    fun logout() {
        val editor = preferences.edit()
        editor.remove(TOKEN)
        editor.apply()
    }

    // Method to decode the token and return its payload as a map
    private fun decodeToken(token: String): Map<String, Any>? {
        try {
            val parts = token.split(".")
            if (parts.size == 3) {
                val payload = parts[1]
                val decodedBytes = Base64.decode(payload, Base64.URL_SAFE)
                val json = String(decodedBytes)
                val jsonObject = JSONObject(json)
                return jsonObject.toMap()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    // Helper method to convert JSONObject to a Map
    private fun JSONObject.toMap(): Map<String, Any> {
        val map = mutableMapOf<String, Any>()
        val keys = keys()
        while (keys.hasNext()) {
            val key = keys.next()
            val value = get(key)
            map[key] = value
        }
        return map
    }
}
