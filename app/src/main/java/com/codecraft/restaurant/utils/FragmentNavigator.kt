package com.codecraft.restaurant.utils

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

object FragmentNavigator {

    fun addFragment(
        activity: Activity,
        fm: FragmentManager?,
        containerId: Int,
        fragment: Fragment,
        args: Bundle?,
        addToBackStack: Boolean,
        tag: String
    ) {
        if (activity.isFinishing && fm == null) return
        try {
            if (args != null) {
                fragment.arguments = args
            }
            val fTransaction = fm!!.beginTransaction()
            fTransaction.add(containerId, fragment, tag)
            fTransaction.addToBackStack(tag).commitAllowingStateLoss()
            fm.executePendingTransactions()
        } catch (e: Exception) {
            Log.e("exceptionTransaction:", e.toString())
        }

    }

    fun replaceFragment(
        activity: Activity,
        fm: FragmentManager?,
        containerId: Int,
        fragment: Fragment,
        args: Bundle?,
        addToBackStack: Boolean,
        tag: String
    ) {
        if (activity.isFinishing && fm == null) return
        try {
            if (args != null) {
                fragment.arguments = args
            }
            val fTransaction = fm!!.beginTransaction()
            if (addToBackStack) {
                fTransaction.replace(containerId, fragment, tag)
                fTransaction.addToBackStack(tag).commitAllowingStateLoss()
                fm!!.executePendingTransactions()
            } else {
                fTransaction.replace(containerId, fragment, tag)
                fTransaction.commitAllowingStateLoss()
            }
        } catch (e: Exception) {
            Log.e("exceptionTransaction:", e.toString())
        }
    }
}
