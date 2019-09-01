package com.codecraft.restaurant.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.codecraft.restaurant.R
import com.codecraft.restaurant.data.response.Location
import com.codecraft.restaurant.data.response.Result
import com.codecraft.restaurant.utils.AppConstants
import com.codecraft.restaurant.utils.PreferenceHelper
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


open class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var mapFragment: SupportMapFragment? = null
    private var mapLocationList = ArrayList<Result>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        mapLocationList = intent.getParcelableArrayListExtra(AppConstants.MAP_LOCATIONS)
        Log.i("parcedList", "" + mapLocationList.size)
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment?.getMapAsync(this)

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        for (item in mapLocationList) {
            val markerLocation = item.getGeometry()?.location
            if (markerLocation != null) {
                item.getName()?.let { createMarker(markerLocation, it, item.getVicinity()!!) }
            }
        }
        val sydney = LatLng(
            PreferenceHelper.getInstance().getPrefFloat(AppConstants.KEY_LATITUDE).toDouble(),
            PreferenceHelper.getInstance().getPrefFloat(AppConstants.KEY_LONGITUDE).toDouble()
        )
        val marker = mMap.addMarker(MarkerOptions().position(sydney).title("Current Location")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))
        marker.showInfoWindow()
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                sydney,
                AppConstants.MAP_ZOOM
            )
        )
        mapFragment?.onResume()
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
