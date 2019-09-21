package com.demo.restaurant.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class PhotoReference(
    @SerializedName("height")
    @Expose
    var height: Int? = null,
    @SerializedName("html_attributions")
    @Expose
    var htmlAttributions: List<String>? = null,
    @SerializedName("photo_reference")
    @Expose
    var photoReference: String? = null,
    @SerializedName("width")
    @Expose
    var width: Int? = null
)