package com.codecraft.restaurant.data.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("lat")
    @Expose
    var lat: Double? = null,
    @SerializedName("lng")
    @Expose
    var lng: Double? = null
) : Parcelable {

    constructor(parcel: Parcel) : this() {
        lat = parcel.readValue(Double::class.java.classLoader) as? Double
        lng = parcel.readValue(Double::class.java.classLoader) as? Double
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