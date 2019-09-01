package com.codecraft.restaurant.ui.detail

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.ScaleGestureDetector
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.codecraft.restaurant.R
import com.codecraft.restaurant.data.model.UiHelper
import com.codecraft.restaurant.data.response.Result
import com.codecraft.restaurant.network.ImageFetchAsyntask
import com.codecraft.restaurant.rxbus.RxEvent
import com.codecraft.restaurant.ui.base.BaseFragment
import com.codecraft.restaurant.utils.AppConstants
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlin.math.max
import kotlin.math.min

class DetailFragment : BaseFragment() {

    private var result: Result? = null
    private var mScaleGestureDetector: ScaleGestureDetector? = null
    private var scaleFactor = 1.0f

    override fun getFragmentLayoutId(): Int {
        return R.layout.fragment_detail
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        result = arguments?.getParcelable(RESTAURANT_DATA)
    }

    override fun initViews(view: View) {
        detailTitle.text=result?.getName()
        detailThumb.post {
            result?.let {
                Log.i("detailData", "" + getViewModel()?.getApiUrl(it,detailThumb.width,detailThumb.height))
                getViewModel()?.getApiUrl(it,detailThumb.width,detailThumb.height)?.let {
                    ImageFetchAsyntask.fetchImageFromServer(
                        it, detailThumb
                    )
                }
            }
        }

        getViewModel()?.getUiLiveData()?.observe(this,
            Observer<UiHelper> { t -> t?.let { handleUICallbacks(uiHelper = it) } })
        mScaleGestureDetector = ScaleGestureDetector(context, ScaleListener())

        view.setOnTouchListener { v, event ->
            mScaleGestureDetector?.onTouchEvent(event)
            true
        }
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            scaleFactor *= scaleGestureDetector.scaleFactor
            val minScaleFactor = min(scaleFactor, MAX_SCALE)
            scaleFactor = max(MIN_SCALE, minScaleFactor)
            detailThumb.scaleX = scaleFactor
            detailThumb.scaleY = scaleFactor
            return true
        }
    }

    private fun handleUICallbacks(uiHelper: UiHelper) {
        when (uiHelper.status) {
            AppConstants.UIConstants.SHOW_PROGRESS ->
                progressCircular.visibility = View.VISIBLE
            AppConstants.UIConstants.HIDE_PROGRESS -> progressCircular.visibility = View.GONE
        }
    }

    private fun getViewModel(): DetailViewModel? {
        return ViewModelProviders.of(this).get(DetailViewModel::class.java)
    }

    override fun resumeScreen() {
    }


    override fun handleBusCallback(event: Any) {
        if (event is RxEvent<*>) {
            when (event.eventTag) {
                RxEvent.EVENT_LOCATION_UPDATED -> {

                }
            }
        }
    }

    companion object {
        private const val RESTAURANT_DATA = "restaurantData"
        private const val MAX_SCALE = 10.0f
        private const val MIN_SCALE = 1f

        fun newInstance(result: Result): DetailFragment {
            val args = Bundle()
            val fragment = DetailFragment()
            args.putParcelable(RESTAURANT_DATA, result)
            fragment.arguments = args
            return fragment
        }
    }
}