package com.codecraft.restaurant.data.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Geometry() :Parcelable {

    @SerializedName("location")
    @Expose
    var location: Location? = null

    constructor(parcel: Parcel) : this() {
        location = parcel.readParcelable(Location::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(location, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Geometry> {
        override fun createFromParcel(parcel: Parcel): Geometry {
            return Geometry(parcel)
        }

        override fun newArray(size: Int): Array<Geometry?> {
            return arrayOfNulls(size)
        }
    }
}