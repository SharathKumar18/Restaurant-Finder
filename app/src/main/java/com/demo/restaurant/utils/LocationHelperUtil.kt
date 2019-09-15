package com.demo.restaurant.utils

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import com.demo.restaurant.R

object LocationHelperUtil {

    private var alertBuilder: AlertDialog.Builder? = null

    fun checkLocationPermission(context: Activity): Boolean {
        if (!checkSelfPermission(context)) {
            return if (ActivityCompat.shouldShowRequestPermissionRationale(context,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                alertBuilder = AlertDialog.Builder(context)
                    .setTitle(R.string.title_location_permission)
                    .setMessage(R.string.text_location_permission)
                    .setPositiveButton(R.string.ok) { var1, var2 ->
                        requestLocationPermission(context) }
                alertBuilder?.create()
                alertBuilder?.show()
                false
            } else {
                requestLocationPermission(context)
                false
            }
        } else {
            return true
        }
    }

    private fun requestLocationPermission(context: Activity) {
        ActivityCompat.requestPermissions(
            context, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            AppConstants.GET_LOCATION
        )
    }

    fun checkSelfPermission(context: Context): Boolean {
        val isGranted = checkSelfPermission(context,
            Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED
        if (isGranted) {
            alertBuilder = null
        }
        return isGranted
    }
}
