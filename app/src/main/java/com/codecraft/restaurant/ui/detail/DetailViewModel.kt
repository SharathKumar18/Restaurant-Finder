package com.codecraft.restaurant.ui.detail

import android.app.Application
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.transition.Transition
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.codecraft.restaurant.application.RestaurantApp
import com.codecraft.restaurant.data.response.Restaurant
import com.codecraft.restaurant.data.response.Result
import com.codecraft.restaurant.network.ResponseFetchAsyncTask
import com.codecraft.restaurant.ui.base.BaseViewModel
import com.codecraft.restaurant.utils.ApiConstants
import com.codecraft.restaurant.utils.ApiConstants.WIDTH_PHOTO
import com.codecraft.restaurant.utils.AppConstants
import com.codecraft.restaurant.utils.PreferenceHelper

class DetailViewModel(application: Application) : BaseViewModel(application = application){

    private val liveData = MutableLiveData<Bitmap>()

    fun getPhotoData(): MutableLiveData<Bitmap> {
        return liveData
    }

    private fun getApiUrl(result: Result): String {
        val url = StringBuilder()
        url.append(ApiConstants.BASE_URL)
            .append(ApiConstants.PHOTO_REFERENCE)
            .append(ApiConstants.MAX_WIDTH)
            .append(result.getPhotos()?.get(0)?.width)
            .append("&")
            .append(ApiConstants.PHOTO_REFERENCE_ID)
            .append(result.getPhotos()?.get(0)?.photoReference)
            .append("&")
            .append(ApiConstants.KEY)
            .append(ApiConstants.API_KEY)
        //51.52864165,-0.10179430
        return url.toString()
    }

    fun fetchPhoto(result: Result) {
        showProgress()
        Log.i("restaurantData", "Url:" + getApiUrl(result))
        Glide.with(RestaurantApp.getContext())
            .load(getApiUrl(result))
            .asBitmap()
            .into(object : SimpleTarget<Bitmap>(){
                override fun onResourceReady(
                    resource: Bitmap?,
                    glideAnimation: GlideAnimation<in Bitmap>?
                ) {
                    val bitmap: Bitmap? =resource
                    liveData.value=bitmap
                    hideProgress()
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }
            })
    }

    override fun handleBusCallback(event: Any) {

    }

}