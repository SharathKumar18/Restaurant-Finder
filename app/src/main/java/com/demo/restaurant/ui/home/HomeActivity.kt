package com.demo.restaurant.ui.home

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Parcelable
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import com.demo.restaurant.R
import com.demo.restaurant.data.response.Result
import com.demo.restaurant.rxbus.RxEvent
import com.demo.restaurant.rxbus.RxEvent.Companion.EVENT_LOAD_HOME
import com.demo.restaurant.rxbus.RxEvent.Companion.EVENT_RESTAURANT_ITEM_CLICKED
import com.demo.restaurant.rxbus.RxEvent.Companion.SHOW_TOOLBAR_HOME
import com.demo.restaurant.ui.MapsActivity
import com.demo.restaurant.ui.base.BaseActivity
import com.demo.restaurant.ui.detail.DetailFragment
import com.demo.restaurant.ui.splash.SplashFragment
import com.demo.restaurant.utils.AppConstants
import com.demo.restaurant.utils.FragmentNavigator
import com.demo.restaurant.utils.LocationHelperUtil
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import kotlinx.android.synthetic.main.layout_toolbar.*
import kotlinx.android.synthetic.main.layout_toolbar.view.*


class HomeActivity : BaseActivity(), LocationListener {

    override fun getLayoutId(): Int {
        return R.layout.activity_home
    }

    override fun getContainer(): Int {
        return R.id.homeContainer
    }

    override fun initViews() {
        loadSplashFragment()
        mapIcon.setOnClickListener {
            loadMapFragment()
        }
    }

    override fun onStart() {
        super.onStart()
        val isPermissionGranted = LocationHelperUtil.checkLocationPermission(this)
        if (isPermissionGranted) {
            fetchUserLocation()
        }
    }

    private fun fetchUserLocation() {
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
            LOCATION_REFRESH_DISTANCE.toFloat(), this
        )
        locationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            LOCATION_REFRESH_TIME,
            LOCATION_REFRESH_DISTANCE.toFloat(), this
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
                        requestLocation()
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOCATION_SETTINGS_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                fetchUserLocation()
            }
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onProviderEnabled(provider: String?) {
    }

    override fun onProviderDisabled(provider: String?) {
    }

    override fun onLocationChanged(location: Location) {
        preferenceHelper.editPrefLong(AppConstants.KEY_LATITUDE, location.latitude.toFloat())
        preferenceHelper.editPrefLong(AppConstants.KEY_LONGITUDE, location.longitude.toFloat())
        val event = RxEvent(RxEvent.EVENT_LOCATION_UPDATED, location)
        rxBus.send(event)
    }

    private fun requestLocation() {
        val mLocationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(10 * 1000)
            .setFastestInterval(1000)

        val settingsBuilder = LocationSettingsRequest.Builder()
            .addLocationRequest(mLocationRequest)
        settingsBuilder.setAlwaysShow(true)

        val settingsClient = LocationServices.getSettingsClient(this)
        val result = settingsClient.checkLocationSettings(settingsBuilder.build())
        result.addOnSuccessListener { fetchUserLocation() }
            .addOnFailureListener { e ->
                val statusCode = (e as ApiException).statusCode
                if (statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                    try {
                        val exception = e as ResolvableApiException
                        exception.startResolutionForResult(
                            this@HomeActivity,
                            LOCATION_SETTINGS_REQUEST
                        )
                    } catch (sie: IntentSender.SendIntentException) {
                    }
                }
            }
    }

    override fun onStop() {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        locationManager.removeUpdates(this)
        super.onStop()
    }

    override fun handleBusCallback(event: Any) {
        val rxEvent = event as RxEvent<*>
        when (rxEvent.eventTag) {
            EVENT_LOAD_HOME -> {
                loadHomeFragment()
            }
            EVENT_RESTAURANT_ITEM_CLICKED -> {
                loadDetailFragment(event.data as Result)
            }
            SHOW_TOOLBAR_HOME -> {
                toolbar.parentLayout.toolbaTitle.text = getString(R.string.title_home)
                toolbar.parentLayout.mapIcon.visibility = VISIBLE
            }
        }
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
        toolbar.parentLayout.toolbaTitle.text = getString(R.string.title_home)
        FragmentNavigator.replaceFragment(
            this, supportFragmentManager,
            getContainer(), HomeFragment.newInstance(), null, false,
            HomeFragment::class.java.simpleName
        )
    }

    fun loadDetailFragment(result: Result) {
        toolbar.parentLayout.toolbaTitle.text = getString(R.string.title_detail)
        toolbar.parentLayout.mapIcon.visibility = GONE
        FragmentNavigator.addFragment(
            this, supportFragmentManager,
            getContainer(), DetailFragment.newInstance(result), null, true,
            DetailFragment::class.java.simpleName
        )
    }

    private fun loadMapFragment() {
        if (preferenceHelper.getPrefFloat(AppConstants.KEY_LATITUDE) != 0f &&
            preferenceHelper.getPrefFloat(AppConstants.KEY_LONGITUDE) != 0f
        ) {
            var result = ArrayList<Result>()
            val currentFragment = supportFragmentManager.findFragmentById(getContainer())
            if (currentFragment is HomeFragment) {
                val mutableList: MutableLiveData<ArrayList<Result>>? =
                    currentFragment.getFragmentData()?.getRestaurantLiveData()
                if (mutableList?.value != null && mutableList.value is ArrayList<Result>) {
                    result = mutableList.value as ArrayList<Result>
                }
            }
            val intent = Intent(this, MapsActivity::class.java)
            intent.putParcelableArrayListExtra(
                AppConstants.MAP_LOCATIONS,
                result as ArrayList<out Parcelable>
            )
            startActivity(intent)
        }
    }

    private fun setIsToolbarRequired(value: Boolean) {
        if (value) {
            toolbar.visibility = VISIBLE
        } else {
            toolbar.visibility = GONE
        }
    }

    companion object {
        private const val LOCATION_REFRESH_TIME: Long = 1000
        private const val LOCATION_REFRESH_DISTANCE: Long = 1000
        private const val LOCATION_SETTINGS_REQUEST = 1
    }
}
