package com.demo.restaurant.utils

import android.content.Context
import android.net.ConnectivityManager

import com.demo.restaurant.application.RestaurantApp

object AppUtils {

    fun isNetworkConnected(): Boolean {
        val connectivityManager =
            RestaurantApp.getContext()?.getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}
