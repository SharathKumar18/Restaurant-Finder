package com.demo.restaurant.data.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("geometry")
    @Expose
    var geometry: Geometry? = null,
    @SerializedName("icon")
    @Expose
    var icon: String? = null,
    @SerializedName("id")
    @Expose
    var id: String? = null,
    @SerializedName("name")
    @Expose
    var name: String? = null,
    @SerializedName("place_id")
    @Expose
    var placeId: String? = null,
    @SerializedName("rating")
    @Expose
    var rating: Double? = null,
    @SerializedName("reference")
    @Expose
    var reference: String? = null,
    @SerializedName("scope")
    @Expose
    var scope: String? = null,
    @SerializedName("types")
    @Expose
    var types: List<String>? = null,
    @SerializedName("user_ratings_total")
    @Expose
    var userRatingsTotal: Int? = null,
    @SerializedName("vicinity")
    @Expose
    var vicinity: String? = null,
    @SerializedName("price_level")
    @Expose
    var priceLevel: Int? = null,
    @SerializedName("photos")
    @Expose
    var photos: List<PhotoReference>? = null
) : Parcelable {

    constructor(parcel: Parcel) : this() {
        icon = parcel.readString()
        id = parcel.readString()
        name = parcel.readString()
        placeId = parcel.readString()
        rating = parcel.readValue(Double::class.java.classLoader) as? Double
        reference = parcel.readString()
        scope = parcel.readString()
        types = parcel.createStringArrayList()
        userRatingsTotal = parcel.readValue(Int::class.java.classLoader) as? Int
        vicinity = parcel.readString()
        priceLevel = parcel.readValue(Int::class.java.classLoader) as? Int
        geometry = parcel.readParcelable(javaClass.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(icon)
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(placeId)
        parcel.writeValue(rating)
        parcel.writeString(reference)
        parcel.writeString(scope)
        parcel.writeStringList(types)
        parcel.writeValue(userRatingsTotal)
        parcel.writeString(vicinity)
        parcel.writeValue(priceLevel)
        parcel.writeParcelable(geometry, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Result> {
        override fun createFromParcel(parcel: Parcel): Result {
            return Result(parcel)
        }

        override fun newArray(size: Int): Array<Result?> {
            return arrayOfNulls(size)
        }
    }
}