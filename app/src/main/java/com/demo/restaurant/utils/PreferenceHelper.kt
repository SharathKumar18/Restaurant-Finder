package com.demo.restaurant.utils

import android.content.Context
import android.content.SharedPreferences
import com.demo.restaurant.application.RestaurantApp

class PreferenceHelper {

    private var preference: SharedPreferences? = null

    init {
        preference = RestaurantApp.getContext()?.getSharedPreferences(AppConstants.PREF_NAME,
            Context.MODE_PRIVATE)
    }

    fun editPrefLong(name: String, value: Float) {
        preference!!.edit().putFloat(name, value).apply()
    }

    fun getPrefFloat(name: String): Float {
        return preference!!.getFloat(name, 0f)
    }

    /*companion object{
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
    }*/
}