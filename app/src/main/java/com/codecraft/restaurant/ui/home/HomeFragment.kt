package com.codecraft.restaurant.ui.home

import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.codecraft.restaurant.R
import com.codecraft.restaurant.data.model.UiHelper
import com.codecraft.restaurant.data.response.Restaurant
import com.codecraft.restaurant.data.response.Result
import com.codecraft.restaurant.recyclercomponents.RestaurantRecyclerAdapter
import com.codecraft.restaurant.rxbus.RxEvent
import com.codecraft.restaurant.ui.base.BaseFragment
import com.codecraft.restaurant.utils.AppConstants
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment() {

    private var restaurantAdapter: RestaurantRecyclerAdapter? = null
    private var layoutManager: LinearLayoutManager? = null
    private var totalItems:Int? = 0

    override fun getFragmentLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun initViews(view: View) {
        getViewModel()?.getUiLiveData()?.observe(this,
            Observer<UiHelper> { t -> t?.let { handleUICallbacks(uiHelper = it) } })
        observeLiveData()
        setUpRecyclerView(null)
        getViewModel()?.fetchRestaurantData()
    }

    private fun observeLiveData() {
        getViewModel()?.getRestaurantLiveData()?.observe(this,
            Observer<Restaurant> { t ->
                if(restaurantAdapter==null){
                    setUpRecyclerView(t?.getResults())
                }else{
                    restaurantAdapter?.updateItems(t?.getResults(),totalItems)
                    totalItems=t?.getResults()?.size
                }
            })
    }

    override fun resumeScreen() {
    }

    override fun handleBusCallback(event: Any) {
        if (event is RxEvent<*>) {
            when (event.eventTag) {
                RxEvent.EVENT_LOCATION_UPDATED -> {
                    if(event.data is Location){

                    }
                }
            }
        }
    }

    /*private val recyclerViewOnScrollListener = object : RecyclerView.OnScrollListener() {
        fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
        }

        fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (layoutManager != null) {
                if (dy > 0) {
                    val visibleItemCount = layoutManager!!.getChildCount()
                    val totalItemCount = layoutManager!!.getItemCount()
                    val previousVisibleItems = layoutManager!!.findFirstVisibleItemPosition()
                    if (!loading) {
                        if (visibleItemCount + previousVisibleItems >= totalItemCount && mPreviousTotal >= ApiConstants.DEFAULT_COUNT) {
                            loading = true
                            fetchRestaurantList(
                                getViewModel()!!.getCityName(),
                                getViewModel()!!.getItemCount()
                            )
                        }
                    }
                }
            }
        }
    }*/


    private fun setUpRecyclerView(results: List<Result>?) {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        restaurantRecyclerView.layoutManager= layoutManager
        restaurantAdapter = RestaurantRecyclerAdapter(results)
        restaurantRecyclerView.adapter = restaurantAdapter
        restaurantRecyclerView.addItemDecoration(
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        )
    }


    private fun handleUICallbacks(uiHelper: UiHelper) {
        when (uiHelper.status) {
            AppConstants.UIConstants.SHOW_PROGRESS ->
                progressCircular.visibility = View.VISIBLE
            AppConstants.UIConstants.HIDE_PROGRESS -> progressCircular.visibility = View.GONE
        }
    }


    private fun getViewModel(): HomeViewModel? {
        return ViewModelProviders.of(this).get(HomeViewModel::class.java)
    }

    companion object {
        fun newInstance(): HomeFragment {
            val args = Bundle()
            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
