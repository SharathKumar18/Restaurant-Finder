package com.demo.restaurant.ui.detail

import android.app.Application
import com.demo.restaurant.data.response.Result
import com.demo.restaurant.ui.base.BaseViewModel
import com.demo.restaurant.utils.ApiConstants

class DetailViewModel(application: Application) : BaseViewModel(application = application) {

    fun getApiUrl(result: Result,width:Int,height:Int): String {
        val url = StringBuilder()
        url.append(ApiConstants.BASE_URL)
            .append(ApiConstants.PHOTO_REFERENCE)
            .append(ApiConstants.PHOTO_REFERENCE_ID)
            .append(result.photos?.get(0)?.photoReference)
            .append("&")
            .append(ApiConstants.MAX_WIDTH)
            .append(width)
            .append("&")
            .append(ApiConstants.MAX_HEIGHT)
            .append(height)
            .append("&")
            .append(ApiConstants.KEY)
            .append(ApiConstants.API_KEY)
        return url.toString()
    }

    override fun handleBusCallback(event: Any) {

    }

}