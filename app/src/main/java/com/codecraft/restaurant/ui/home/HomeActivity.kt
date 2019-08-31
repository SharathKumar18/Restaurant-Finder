package com.codecraft.restaurant.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.app.ActivityCompat
import com.codecraft.restaurant.R
import com.codecraft.restaurant.rxbus.RxEvent
import com.codecraft.restaurant.rxbus.RxEvent.Companion.EVENT_LOAD_HOME
import com.codecraft.restaurant.ui.base.BaseActivity
import com.codecraft.restaurant.ui.splash.SplashFragment
import com.codecraft.restaurant.utils.AppConstants
import com.codecraft.restaurant.utils.FragmentNavigator
import com.codecraft.restaurant.utils.LocationHelperUtil
import kotlinx.android.synthetic.main.layout_toolbar.*

class HomeActivity : BaseActivity() {


    override fun getLayoutId(): Int {
        return R.layout.activity_home
    }

    override fun getContainer(): Int {
        return R.id.homeContainer
    }

    override fun initViews() {
        loadSplashFragment()
        val isPermissionGranted = LocationHelperUtil.checkLocationPermission(this)
        if (isPermissionGranted) {
            findUserLocation()
        }
    }

    private fun findUserLocation() {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            LOCATION_REFRESH_TIME,
            LOCATION_REFRESH_DISTANCE.toFloat(), locationListener
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            AppConstants.GET_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (LocationHelperUtil.checkSelfPermission(this)) {
                        findUserLocation()
                    }
                }
            }
        }
    }

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            Log.i("location received", "" + location.latitude + location.longitude)
            val event = RxEvent(RxEvent.EVENT_LOCATION_UPDATED, location)
            rxBus?.send(event)
        }

        override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {

        }

        override fun onProviderEnabled(s: String) {

        }

        override fun onProviderDisabled(s: String) {

        }
    }

    override fun handleBusCallback(event: Any) {
        val rxEvent = event as RxEvent<*>
        when (rxEvent.eventTag) {
            EVENT_LOAD_HOME -> {
                loadHomeFragment()
            }
        }
        Log.i("handled", "" + rxEvent.eventTag)
    }

    private fun loadSplashFragment() {
        setIsToolbarRequired(false)
        FragmentNavigator.replaceFragment(
            this, supportFragmentManager,
            getContainer(), SplashFragment.newInstance(), null, false,
            SplashFragment::class.java.simpleName
        )
    }

    private fun loadHomeFragment() {
        setIsToolbarRequired(true)
        FragmentNavigator.replaceFragment(
            this, supportFragmentManager,
            getContainer(), HomeFragment.newInstance(), null, false,
            HomeFragment::class.java.simpleName
        )
    }

    private fun setIsToolbarRequired(value: Boolean) {
        if (value) {
            toolbar.visibility = VISIBLE
        } else {
            toolbar.visibility = GONE
        }
    }

    companion object {
        private const val LOCATION_REFRESH_TIME: Long = 5000
        private const val LOCATION_REFRESH_DISTANCE: Long = 1000
        private val LOCATION_SETTINGS_REQUEST = 1
    }
}
