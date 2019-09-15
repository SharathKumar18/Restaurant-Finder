package com.demo.restaurant.recyclercomponents

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.demo.restaurant.R
import com.demo.restaurant.data.response.Result
import com.demo.restaurant.network.ImageFetchAsyncTask
import com.demo.restaurant.rxbus.RxEvent
import com.demo.restaurant.ui.base.BaseViewHolder

class RestaurantItemViewHolder(var view: View) : BaseViewHolder(view) {

    fun bindData(result: Result) {
        val thumbnail = view.findViewById<ImageView>(R.id.restaurantThumb)
        val name = view.findViewById<TextView>(R.id.restaurantName)
        val location = view.findViewById<TextView>(R.id.restaurantLocation)
        var distance = view.findViewById<TextView>(R.id.restaurantDistance)

        result.icon?.let { ImageFetchAsyncTask.fetchImageFromServer(it, thumbnail) }
        name.text = result.name
        location.text = result.vicinity
        view.setOnClickListener {
            onItemClicked(result)
        }
    }

    private fun onItemClicked(result: Result) {
        val event = RxEvent(RxEvent.EVENT_RESTAURANT_ITEM_CLICKED, result)
        rxBus.send(event)
    }

    override fun handleBusCallback(event: Any) {
    }

    companion object {
        fun getLayout(): Int {
            return R.layout.restaurant_item
        }
    }
}