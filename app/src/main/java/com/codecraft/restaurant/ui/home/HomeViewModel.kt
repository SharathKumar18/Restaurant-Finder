package com.codecraft.restaurant.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.codecraft.restaurant.data.response.Restaurant
import com.codecraft.restaurant.network.ResponseFetchAsyncTask
import com.codecraft.restaurant.ui.base.BaseViewModel
import com.codecraft.restaurant.utils.ApiConstants.API_KEY
import com.codecraft.restaurant.utils.ApiConstants.BASE_URL
import com.codecraft.restaurant.utils.ApiConstants.DISTANCE
import com.codecraft.restaurant.utils.ApiConstants.KEY
import com.codecraft.restaurant.utils.ApiConstants.LOCATION
import com.codecraft.restaurant.utils.ApiConstants.TYPE
import com.codecraft.restaurant.utils.ApiConstants.NEAR_BY_RESTAURANT
import com.codecraft.restaurant.utils.ApiConstants.NEXT_PAGE_TOKEN
import com.codecraft.restaurant.utils.ApiConstants.RANK_BY
import com.codecraft.restaurant.utils.ApiConstants.TYPE_RESTAURANT
import com.codecraft.restaurant.utils.AppConstants.KEY_LATITUDE
import com.codecraft.restaurant.utils.AppConstants.KEY_LONGITUDE
import com.codecraft.restaurant.utils.AppConstants.RESULT_STATUS
import com.codecraft.restaurant.utils.PreferenceHelper
import com.google.gson.Gson

class HomeViewModel(application: Application) : BaseViewModel(application = application) {

    private val liveData = MutableLiveData<Restaurant>()
    private var nextPageToken: String? = null

    fun getRestaurantLiveData(): MutableLiveData<Restaurant> {
        return liveData
    }

    private fun getApiUrl(): String {
        return StringBuilder().append(BASE_URL)
            .append(NEAR_BY_RESTAURANT)
            .append(TYPE)
            .append(TYPE_RESTAURANT)
            .append("&")
            .append(KEY)
            .append(API_KEY)
            .append("&")
            .append(RANK_BY)
            .append(DISTANCE)
            .append("&")
            .append(LOCATION)
            .append(PreferenceHelper.getInstance().getPrefFloat(KEY_LATITUDE))
            .append(",")
            .append(PreferenceHelper.getInstance().getPrefFloat(KEY_LONGITUDE))
            .append("&")
            .append(NEXT_PAGE_TOKEN)
            .append(nextPageToken).toString()
        
        //51.52864165,-0.10179430
    }

    fun fetchRestaurantData() {
        showProgress()
        ResponseFetchAsyncTask.fetchResultFromServer(getApiUrl())
        ResponseFetchAsyncTask.setResultListener(object :
            ResponseFetchAsyncTask.OnResultListener {
            override fun onResultSuccess(restaurant: String) {
                Log.i("restaurantData", "Url:" + getApiUrl() + "\nResponse" + restaurant)
                val data = Gson().fromJson<Any>(restaurant, Restaurant::class.java)
                if (data is Restaurant && !data.getStatus().equals(RESULT_STATUS)) {
                    nextPageToken = data.getNextPageToken()
                    liveData.value = data
                }
                hideProgress()
            }

            override fun onResultFailed(value: String) {
            }

        })
    }

    override fun handleBusCallback(event: Any) {

    }
}