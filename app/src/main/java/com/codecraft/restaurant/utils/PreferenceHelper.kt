package com.codecraft.restaurant.utils

import android.content.Context
import android.content.SharedPreferences
import com.codecraft.restaurant.application.RestaurantApp

class PreferenceHelper {

    fun editPrefString(name: String, value: String) {
        preference!!.edit().putString(name, value).apply()
    }

    fun editPrefLong(name: String, value: Float) {
        preference!!.edit().putFloat(name, value).apply()
    }

    fun getPrefFloat(name: String): Float {
        return preference!!.getFloat(name, 0f)
    }

    fun getPrefString(name: String): String? {
        return preference!!.getString(name, null)
    }

    companion object{
        private var helperClass: PreferenceHelper? = null
        private var preference: SharedPreferences? = null
        fun getInstance(): PreferenceHelper {
            if (helperClass == null) {
                preference = RestaurantApp.getContext()?.getSharedPreferences(AppConstants.PREF_NAME,
                    Context.MODE_PRIVATE)
                helperClass = PreferenceHelper()
            }
            return helperClass as PreferenceHelper
        }
    }
}