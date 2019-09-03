package com.codecraft.restaurant.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.codecraft.restaurant.R
import com.codecraft.restaurant.application.RestaurantApp
import com.codecraft.restaurant.data.response.Location
import com.codecraft.restaurant.data.response.Result
import com.codecraft.restaurant.utils.AppConstants
import com.codecraft.restaurant.utils.PreferenceHelper
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import javax.inject.Inject
import android.net.Uri
import com.codecraft.restaurant.utils.ApiConstants.DESTINATION_ADDRESS
import com.codecraft.restaurant.utils.ApiConstants.MAPS_BASE_URL
import com.google.android.gms.maps.model.*


open class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    @Inject
    lateinit var preferenceHelper: PreferenceHelper
    private lateinit var mMap: GoogleMap
    private var mapFragment: SupportMapFragment? = null
    private var mapLocationList = ArrayList<Result>()
    private lateinit var currentLocation: LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        RestaurantApp.getContext()?.getApplicationComponent()?.inject(this)
        mapLocationList = intent.getParcelableArrayListExtra(AppConstants.MAP_LOCATIONS)
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment?.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        var line: Polyline? = null
        mMap = googleMap
        showAllNearByLocationsOnMap()
        showCurrentLocationOnMap()
        mapFragment?.onResume()
        mMap.setOnMarkerClickListener { marker ->
            line?.remove()
            for (item in mapLocationList) {
                if (item.name.equals(marker?.title)) {
                    marker.showInfoWindow()
                    val markerLocation = item.geometry?.location
                    redirectToMap(markerLocation)
                }
            }
            true
        }
    }

    private fun redirectToMap(markerLocation: Location?) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(getMapAddress(markerLocation?.lat, markerLocation?.lng))
        )
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun getMapAddress(latitude: Double?, longitude: Double?): String {
        return StringBuilder().append(MAPS_BASE_URL)
            .append(DESTINATION_ADDRESS)
            .append(latitude)
            .append(",")
            .append(longitude)
            .toString()
    }

    private fun showCurrentLocationOnMap() {
        currentLocation = LatLng(
            preferenceHelper.getPrefFloat(AppConstants.KEY_LATITUDE).toDouble(),
            preferenceHelper.getPrefFloat(AppConstants.KEY_LONGITUDE).toDouble()
        )
        val marker = mMap.addMarker(
            MarkerOptions().position(currentLocation).title(getString(R.string.current_location))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        )
        marker.showInfoWindow()
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation))
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                currentLocation,
                AppConstants.MAP_ZOOM
            )
        )
    }

    private fun showAllNearByLocationsOnMap() {
        for (item in mapLocationList) {
            val markerLocation = item.geometry?.location
            if (markerLocation != null) {
                item.name?.let { createMarker(markerLocation, it, item.vicinity!!) }
            }
        }
    }

    private fun createMarker(
        location: Location,
        title: String,
        snippet: String
    ): Marker? {
        return if (location.lat != null && location.lng != null) {
            return mMap.addMarker(
                MarkerOptions()
                    .position(LatLng(location.lat!!, location.lng!!))
                    .anchor(0.5f, 0.5f)
                    .title(title)
                    .snippet(snippet)
            )
        } else {
            null
        }
    }
}
