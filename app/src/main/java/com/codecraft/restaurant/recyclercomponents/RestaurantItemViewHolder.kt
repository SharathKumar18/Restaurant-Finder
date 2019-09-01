package com.codecraft.restaurant.recyclercomponents

import android.graphics.Bitmap
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.codecraft.restaurant.R
import com.codecraft.restaurant.data.response.Restaurant
import com.codecraft.restaurant.data.response.Result
import com.codecraft.restaurant.network.ImageFetchAsyntask
import com.codecraft.restaurant.network.ResponseFetchAsyncTask
import com.codecraft.restaurant.rxbus.RxEvent
import com.codecraft.restaurant.ui.base.BaseViewHolder
import com.codecraft.restaurant.utils.AppConstants
import com.google.gson.Gson

class RestaurantItemViewHolder(var view: View) : BaseViewHolder(view) {

    override fun handleBusCallback(event: Any) {
    }

    fun bindData(result: Result) {

        val thumbnail = view.findViewById<ImageView>(R.id.restaurantThumb)
        val name = view.findViewById<TextView>(R.id.restaurantName)
        val location = view.findViewById<TextView>(R.id.restaurantLocation)
        var distance = view.findViewById<TextView>(R.id.restaurantDistance)

        result.getIcon()?.let {
            ImageFetchAsyntask.fetchImageFromServer(it,thumbnail)
        }
        name.text = result.getName()
        location.text = result.getVicinity()

        view.setOnClickListener {
            onItemClicked(result)
        }
    }

    private fun onItemClicked(result: Result) {
        val event = RxEvent(RxEvent.EVENT_RESTAURANT_ITEM_CLICKED, result)
        rxBus?.send(event)
    }

    companion object {
        fun getLayout(): Int {
            return R.layout.restaurant_item
        }
    }
}