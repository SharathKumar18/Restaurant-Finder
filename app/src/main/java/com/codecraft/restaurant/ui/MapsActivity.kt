package com.codecraft.restaurant.ui

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
import android.graphics.Color
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
        var line: Polyline?=null
        mMap = googleMap
        showAllNearByLocatinsOnMap()
        showCurrentLocationOnMap()
        mapFragment?.onResume()
        mMap.setOnMarkerClickListener { marker ->
            line?.remove()
            for (item in mapLocationList) {
                if (item.getName().equals(marker?.title)) {
                    marker.showInfoWindow()
                    Log.i("Mapclicked", "" + marker.title)
                    val markerLocation = item.getGeometry()?.location
                    line = mMap.addPolyline(
                        PolylineOptions()
                            .add(
                                LatLng(currentLocation.latitude, currentLocation.longitude),
                                markerLocation?.getLat()?.let {
                                    LatLng(it, markerLocation.getLng()!!)
                                }
                            )
                            .width(5f)
                            .color(Color.RED)
                    )
                }
            }
            true
        }
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

    private fun showAllNearByLocatinsOnMap() {
        for (item in mapLocationList) {
            val markerLocation = item.getGeometry()?.location
            if (markerLocation != null) {
                item.getName()?.let { createMarker(markerLocation, it, item.getVicinity()!!) }
            }
        }
    }

    private fun createMarker(
        location: Location,
        title: String,
        snippet: String
    ): Marker? {
        return if (location.getLat() != null && location.getLng() != null) {
            return mMap.addMarker(
                MarkerOptions()
                    .position(LatLng(location.getLat()!!, location.getLng()!!))
                    .anchor(0.5f, 0.5f)
                    .title(title)
                    .snippet(snippet)
            )
        } else {
            null
        }
    }
}
