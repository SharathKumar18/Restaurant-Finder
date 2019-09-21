package com.demo.restaurant.utils

import com.demo.restaurant.BuildConfig

object Logger {

    private var isDebug = false

    init {
        isDebug = BuildConfig.DEBUG
    }

    fun i(tag: String, string: String) {
        if (isDebug) android.util.Log.i(tag, string)
    }

    fun e(tag: String, string: String) {
        if (isDebug) android.util.Log.e(tag, string)
    }

}