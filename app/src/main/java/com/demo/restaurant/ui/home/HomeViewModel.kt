package com.demo.restaurant.ui.home

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.demo.restaurant.data.response.Restaurant
import com.demo.restaurant.data.response.Result
import com.demo.restaurant.network.ResponseFetchAsyncTask
import com.demo.restaurant.ui.base.BaseViewModel
import com.demo.restaurant.utils.ApiConstants.API_KEY
import com.demo.restaurant.utils.ApiConstants.BASE_URL
import com.demo.restaurant.utils.ApiConstants.DISTANCE
import com.demo.restaurant.utils.ApiConstants.KEY
import com.demo.restaurant.utils.ApiConstants.LOCATION
import com.demo.restaurant.utils.ApiConstants.NEAR_BY_RESTAURANT
import com.demo.restaurant.utils.ApiConstants.NEXT_PAGE_TOKEN
import com.demo.restaurant.utils.ApiConstants.RANK_BY
import com.demo.restaurant.utils.ApiConstants.TYPE
import com.demo.restaurant.utils.ApiConstants.TYPE_RESTAURANT
import com.demo.restaurant.utils.AppConstants
import com.demo.restaurant.utils.AppConstants.KEY_LATITUDE
import com.demo.restaurant.utils.AppConstants.KEY_LONGITUDE
import com.demo.restaurant.utils.AppConstants.RESULT_STATUS
import com.google.gson.Gson

class HomeViewModel(application: Application) : BaseViewModel(application = application) {

    private val liveData = MutableLiveData<ArrayList<Result>>()
    private var nextPageToken: String? = null
    private val errorValue: MutableLiveData<Boolean> = MutableLiveData()

    fun getRestaurantLiveData(): MutableLiveData<ArrayList<Result>> {
        return liveData
    }

    fun getErrorLiveData(): MutableLiveData<Boolean> {
        return errorValue
    }

    private fun getApiUrl(): String {
        val url = StringBuilder()
        url.append(BASE_URL)
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
            .append(preferenceHelper.getPrefFloat(KEY_LATITUDE))
            .append(",")
            .append(preferenceHelper.getPrefFloat(KEY_LONGITUDE))
        if (nextPageToken != null) {
            url.append("&")
                .append(NEXT_PAGE_TOKEN)
                .append(nextPageToken).toString()
        }
        return url.toString()
    }

    fun fetchRestaurantData() {
        showProgress()
        errorValue.value = false
        ResponseFetchAsyncTask.fetchResultFromServer(getApiUrl(), object :
            ResponseFetchAsyncTask.OnResultListener {
            override fun <T> onResultSuccess(restaurant: T) {
                sendUiData(AppConstants.UIConstants.DATA_LOADED)
                if (restaurant is String) {
                    val data = Gson().fromJson<Any>(restaurant, Restaurant::class.java)
                    if (data is Restaurant && !data.status.equals(RESULT_STATUS)) {
                        nextPageToken = data.nextPageToken
                        createMutableListData(data)
                    } else {
                        errorValue.value = true
                    }
                }
                hideProgress()
            }

            override fun onResultFailed(value: String) {
                errorValue.value = true
                hideProgress()
            }
        })
    }

    private fun createMutableListData(data: Restaurant) {
        if (liveData.value != null && liveData.value is ArrayList<Result>) {
            val previousResult = liveData.value as ArrayList<Result>
            data.results?.let { previousResult.addAll(it) }
            liveData.value = previousResult
        } else {
            liveData.value = data.results as ArrayList<Result>?
        }
    }

    override fun handleBusCallback(event: Any) {

    }
}