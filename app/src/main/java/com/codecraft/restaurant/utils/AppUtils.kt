package com.codecraft.restaurant.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

import com.codecraft.restaurant.application.RestaurantApp

import java.util.Objects
import java.util.regex.Pattern

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
