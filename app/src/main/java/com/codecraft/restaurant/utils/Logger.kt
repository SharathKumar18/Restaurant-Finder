package com.codecraft.restaurant.utils

import com.codecraft.restaurant.BuildConfig

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

    fun d(tag: String, string: String) {
        if (isDebug) android.util.Log.d(tag, string)
    }

    fun v(tag: String, string: String) {
        if (isDebug) android.util.Log.v(tag, string)
    }

    fun w(tag: String, string: String) {
        if (isDebug) android.util.Log.w(tag, string)
    }
}