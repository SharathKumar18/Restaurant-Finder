package com.codecraft.restaurant.recyclercomponents

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codecraft.restaurant.data.response.Result

class RestaurantRecyclerAdapter(private var results: List<Result>?) :
    RecyclerView.Adapter<RestaurantItemViewHolder>() {

    override fun onBindViewHolder(holder: RestaurantItemViewHolder, position: Int) {
        results?.get(position)?.let { holder.bindData(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(RestaurantItemViewHolder.getLayout(), parent, false)
        return RestaurantItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return results?.size ?: 0
    }

    fun updateItems(data: List<Result>?, previousTotal: Int?) {
        results = data
        if (results != null) {
            data?.size?.let { previousTotal?.let { it1 -> notifyItemRangeChanged(it1, previousTotal +  it) } }
        } else {
            notifyDataSetChanged()
        }
    }
}