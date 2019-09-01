package com.codecraft.restaurant.ui.home

import android.location.Location
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.codecraft.restaurant.R
import com.codecraft.restaurant.data.model.UiHelper
import com.codecraft.restaurant.data.response.Result
import com.codecraft.restaurant.recyclercomponents.RestaurantRecyclerAdapter
import com.codecraft.restaurant.rxbus.RxEvent
import com.codecraft.restaurant.ui.base.BaseFragment
import com.codecraft.restaurant.utils.AppConstants
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {

    private var restaurantAdapter: RestaurantRecyclerAdapter? = null
    private lateinit var layoutManager: LinearLayoutManager
    private var totalItems: Int? = 0
    private var loading = false

    override fun getFragmentLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun initViews(view: View) {
        swipeRefresh.setOnRefreshListener(this)
        observeLiveData()
        setUpRecyclerView(null)
        getViewModel()?.fetchRestaurantData()
    }

    private fun observeLiveData() {
        getViewModel()?.getRestaurantLiveData()?.observe(this,
            Observer<List<Result>> { listData ->
                loading = false
                if (restaurantAdapter == null) {
                    setUpRecyclerView(listData)
                } else {
                    restaurantAdapter?.updateItems(listData, totalItems)
                    totalItems = listData?.size
                }
            })
        getViewModel()?.getUiLiveData()?.observe(this,
            Observer<UiHelper> { t -> t?.let { handleUICallbacks(uiHelper = it) } })

        getViewModel()?.getErrorLiveData()?.observe(this, Observer {
            if(it){
                errorText.visibility=VISIBLE
            }else{
                errorText.visibility= GONE

            }
        })
    }

    override fun resumeScreen() {
        val event = RxEvent(RxEvent.SHOW_TOOLBAR_HOME, null)
        rxBus.send(event)
    }

    override fun onRefresh() {
        swipeRefresh.isRefreshing = false
        getViewModel()?.fetchRestaurantData()
    }

    override fun handleBusCallback(event: Any) {
        if (event is RxEvent<*>) {
            when (event.eventTag) {
                RxEvent.EVENT_LOCATION_UPDATED -> {
                    if (event.data is Location) {
                        getViewModel()?.fetchRestaurantData()
                    }
                }
            }
        }
    }

    private val recyclerViewOnScrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dy > 0) {
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val previousVisibleItems = layoutManager.findFirstVisibleItemPosition()
                if (!loading) {
                    if ((visibleItemCount + previousVisibleItems) >= totalItemCount) {
                        loading = true
                        getViewModel()?.fetchRestaurantData()
                    }
                }
            }
        }
    }

    private fun setUpRecyclerView(results: List<Result>?) {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        restaurantRecyclerView.layoutManager = layoutManager
        restaurantAdapter = RestaurantRecyclerAdapter(results)
        restaurantRecyclerView.adapter = restaurantAdapter
        restaurantRecyclerView.addItemDecoration(
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        )
        restaurantRecyclerView.addOnScrollListener(recyclerViewOnScrollListener)
    }


    private fun handleUICallbacks(uiHelper: UiHelper) {
        when (uiHelper.status) {
            AppConstants.UIConstants.SHOW_PROGRESS ->
                progressCircular.visibility = View.VISIBLE
            AppConstants.UIConstants.HIDE_PROGRESS -> progressCircular.visibility = View.GONE
            AppConstants.UIConstants.DATA_LOADED -> loading = false
        }
    }

    fun getFragmentData(): HomeViewModel? {
        return getViewModel()
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
