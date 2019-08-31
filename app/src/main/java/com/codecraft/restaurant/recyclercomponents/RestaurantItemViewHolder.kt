package com.codecraft.restaurant.recyclercomponents

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codecraft.restaurant.R
import com.codecraft.restaurant.data.response.Result

class RestaurantItemViewHolder(var view: View) : RecyclerView.ViewHolder(view) {


    fun bindData(result: Result) {

        var thumbnail=view.findViewById<ImageView>(R.id.restaurantThumb)
        var name=view.findViewById<TextView>(R.id.restaurantName)
        var location=view.findViewById<TextView>(R.id.restaurantLocation)
        var distance=view.findViewById<TextView>(R.id.restaurantDistance)

        Glide.with(thumbnail.context)
                .load(result.getIcon())
                .placeholder(R.drawable.ic_action_restuarant)
                .error(R.drawable.ic_action_restuarant)
                .into(thumbnail)
        name.text=result.getName()
        location.text= result.getVicinity()
        distance
    }

    companion object {
        fun getLayout(): Int {
            return R.layout.restaurant_item
        }
    }

}