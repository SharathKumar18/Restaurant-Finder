package com.codecraft.restaurant.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Restaurant : BaseResponse() {

    @SerializedName("html_attributions")
    @Expose
    private var htmlAttributions: List<Any>? = null
    @SerializedName("next_page_token")
    @Expose
    private var nextPageToken: String? = null
    @SerializedName("results")
    @Expose
    private var results: List<Result>? = null
    @SerializedName("status")
    @Expose
    private var status: String? = null

    fun getHtmlAttributions(): List<Any>? {
        return htmlAttributions
    }

    fun setHtmlAttributions(htmlAttributions: List<Any>) {
        this.htmlAttributions = htmlAttributions
    }

    fun getNextPageToken(): String? {
        return nextPageToken
    }

    fun setNextPageToken(nextPageToken: String) {
        this.nextPageToken = nextPageToken
    }

    fun getResults(): List<Result>? {
        return results
    }

    fun setResults(results: List<Result>) {
        this.results = results
    }

    fun getStatus(): String? {
        return status
    }

    fun setStatus(status: String) {
        this.status = status
    }
}