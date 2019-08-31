package com.codecraft.restaurant.ui.home

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.codecraft.restaurant.data.model.UiHelper
import com.codecraft.restaurant.data.response.Restaurant
import com.codecraft.restaurant.network.ResponseFetchAsyntask
import com.codecraft.restaurant.ui.base.BaseViewModel
import com.codecraft.restaurant.utils.AppConstants.RESULT_STATUS
import com.google.gson.Gson

class HomeViewModel(application: Application) : BaseViewModel(application = application) {

    private val liveData = MutableLiveData<Restaurant>()

    fun getRestaurantLiveData(): MutableLiveData<Restaurant> {
        return liveData
    }

    fun fetchRestaurantData() {
        showProgress()
        ResponseFetchAsyntask.getInstance()?.fetchResultFromServer("https://maps.googleapis.com/maps/api/place/nearbysearch/json?type=restaurant&key=AIzaSyDlIW0lNweq5jN9wO2F5AVV8FVNXeRqCwk&rankby=distance&location=51.52864165,-0.10179430")
        ResponseFetchAsyntask.getInstance()?.setResultListener(object :
            ResponseFetchAsyntask.OnResultListener{
            override fun onResultSuccess(restaurant: String) {
                val data =  Gson().fromJson<Any>(restaurant, Restaurant::class.java)
                if(data is Restaurant && !data.getStatus().equals(RESULT_STATUS)){
                    liveData.value=data
                }
                hideProgress()
            }

            override fun onResultFailed(value: String) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }

    override fun handleBusCallback(event: Any) {

    }




    /* val response = StringBuffer()
       val url: URL
       var urlConnection: HttpsURLConnection? = null
       try {
           url = URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?type=restaurant&key=AIzaSyDlIW0lNweq5jN9wO2F5AVV8FVNXeRqCwk&rankby=distance&location=51.52864165,-0.10179430")

           urlConnection = url
               .openConnection() as HttpsURLConnection

           val `in` = urlConnection.inputStream

           val isw = InputStreamReader(`in`)

           var data = isw.read()
           while (data != -1) {
               val current = data.toChar()
               data = isw.read()
               response.append(current)
           }
       } catch (e: Exception) {
           e.printStackTrace()
       } finally {
           urlConnection?.disconnect()
       }
       serverResponse = response.toString()
       Log.v("CatalogClient", serverResponse)
       return serverResponse*/
}