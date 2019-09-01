package com.codecraft.restaurant.data.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Result() : Parcelable {
    @SerializedName("geometry")
    @Expose
    private var geometry: Geometry? = null
    @SerializedName("icon")
    @Expose
    private var icon: String? = null
    @SerializedName("id")
    @Expose
    private var id: String? = null
    @SerializedName("name")
    @Expose
    private var name: String? = null
    @SerializedName("place_id")
    @Expose
    private var placeId: String? = null
    @SerializedName("rating")
    @Expose
    private var rating: Double? = null
    @SerializedName("reference")
    @Expose
    private var reference: String? = null
    @SerializedName("scope")
    @Expose
    private var scope: String? = null
    @SerializedName("types")
    @Expose
    private var types: List<String>? = null
    @SerializedName("user_ratings_total")
    @Expose
    private var userRatingsTotal: Int? = null
    @SerializedName("vicinity")
    @Expose
    private var vicinity: String? = null
    @SerializedName("price_level")
    @Expose
    private var priceLevel: Int? = null
    @SerializedName("photos")
    @Expose
    private var photos: List<PhotoReference>? = null

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
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(icon)
        dest?.writeString(id)
        dest?.writeString(name)
        dest?.writeString(placeId)
        dest?.writeValue(rating)
        dest?.writeString(reference)
        dest?.writeString(scope)
        dest?.writeValue(userRatingsTotal)
        dest?.writeString(vicinity)
        dest?.writeString(priceLevel.toString())
    }

    override fun describeContents(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun getPhotos(): List<PhotoReference>? {
        return photos
    }

    fun setPhotos(photos: List<PhotoReference>?) {
        this.photos = photos
    }

    fun getGeometry(): Geometry? {
        return geometry
    }

    fun setGeometry(geometry: Geometry) {
        this.geometry = geometry
    }

    fun getIcon(): String? {
        return icon
    }

    fun setIcon(icon: String) {
        this.icon = icon
    }

    fun getId(): String? {
        return id
    }

    fun setId(id: String) {
        this.id = id
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getPlaceId(): String? {
        return placeId
    }

    fun setPlaceId(placeId: String) {
        this.placeId = placeId
    }

    fun getRating(): Double? {
        return rating
    }

    fun setRating(rating: Double?) {
        this.rating = rating
    }

    fun getReference(): String? {
        return reference
    }

    fun setReference(reference: String) {
        this.reference = reference
    }

    fun getScope(): String? {
        return scope
    }

    fun setScope(scope: String) {
        this.scope = scope
    }

    fun getTypes(): List<String>? {
        return types
    }

    fun setTypes(types: List<String>) {
        this.types = types
    }

    fun getUserRatingsTotal(): Int? {
        return userRatingsTotal
    }

    fun setUserRatingsTotal(userRatingsTotal: Int?) {
        this.userRatingsTotal = userRatingsTotal
    }

    fun getVicinity(): String? {
        return vicinity
    }

    fun setVicinity(vicinity: String) {
        this.vicinity = vicinity
    }

    fun getPriceLevel(): Int? {
        return priceLevel
    }

    fun setPriceLevel(priceLevel: Int?) {
        this.priceLevel = priceLevel
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