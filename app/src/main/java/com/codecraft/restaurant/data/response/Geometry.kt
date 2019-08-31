package com.codecraft.restaurant.data.response

import android.location.Location
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class Geometry {

    @SerializedName("location")
    @Expose
    private var location: Location? = null

    fun getLocation(): Location? {
        return location
    }

    fun setLocation(location: Location) {
        this.location = location
    }
}