package com.codecraft.restaurant.ui.home

import android.Manifest
import android.content.Intent
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
import com.codecraft.restaurant.R
import com.codecraft.restaurant.data.response.Result
import com.codecraft.restaurant.rxbus.RxEvent
import com.codecraft.restaurant.rxbus.RxEvent.Companion.EVENT_LOAD_HOME
import com.codecraft.restaurant.rxbus.RxEvent.Companion.EVENT_RESTAURANT_ITEM_CLICKED
import com.codecraft.restaurant.rxbus.RxEvent.Companion.SHOW_TOOLBAR_HOME
import com.codecraft.restaurant.ui.MapsActivity
import com.codecraft.restaurant.ui.base.BaseActivity
import com.codecraft.restaurant.ui.detail.DetailFragment
import com.codecraft.restaurant.ui.splash.SplashFragment
import com.codecraft.restaurant.utils.AppConstants
import com.codecraft.restaurant.utils.FragmentNavigator
import com.codecraft.restaurant.utils.LocationHelperUtil
import com.codecraft.restaurant.utils.Logger
import kotlinx.android.synthetic.main.layout_toolbar.*
import kotlinx.android.synthetic.main.layout_toolbar.view.*


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
        mapIcon.setOnClickListener {
            loadMapFragment()
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
        locationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
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

    override fun onDestroy() {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        locationManager.removeUpdates(locationListener)
        super.onDestroy()
    }

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            Logger.i("RestaurantData", "" + location.latitude + location.longitude)
            preferenceHelper.editPrefLong(AppConstants.KEY_LATITUDE, location.latitude.toFloat())
            preferenceHelper.editPrefLong(AppConstants.KEY_LONGITUDE, location.longitude.toFloat())
            val event = RxEvent(RxEvent.EVENT_LOCATION_UPDATED, location)
            rxBus.send(event)
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

    private fun loadDetailFragment(result: Result) {
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
    }
}
