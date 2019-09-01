package com.codecraft.restaurant.data.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Location() :Parcelable {
    @SerializedName("lat")
    @Expose
    private var lat: Double? = null
    @SerializedName("lng")
    @Expose
    private var lng: Double? = null

    constructor(parcel: Parcel) : this() {
        lat = parcel.readValue(Double::class.java.classLoader) as? Double
        lng = parcel.readValue(Double::class.java.classLoader) as? Double
    }

    fun getLat(): Double? {
        return lat
    }

    fun setLat(lat: Double?) {
        this.lat = lat
    }

    fun getLng(): Double? {
        return lng
    }

    fun setLng(lng: Double?) {
        this.lng = lng
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(lat)
        parcel.writeValue(lng)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Location> {
        override fun createFromParcel(parcel: Parcel): Location {
            return Location(parcel)
        }

        override fun newArray(size: Int): Array<Location?> {
            return arrayOfNulls(size)
        }
    }
}