package com.codecraft.restaurant.ui.detail

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.codecraft.restaurant.data.response.Result
import com.codecraft.restaurant.network.ImageFetchAsyntask
import com.codecraft.restaurant.network.ResponseFetchAsyncTask
import com.codecraft.restaurant.ui.base.BaseViewModel
import com.codecraft.restaurant.utils.ApiConstants

class DetailViewModel(application: Application) : BaseViewModel(application = application) {

    fun getApiUrl(result: Result,width:Int,height:Int): String {
        val url = StringBuilder()
        url.append(ApiConstants.BASE_URL)
            .append(ApiConstants.PHOTO_REFERENCE)
            .append(ApiConstants.PHOTO_REFERENCE_ID)
            .append(result.getPhotos()?.get(0)?.photoReference)
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