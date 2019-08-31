package com.codecraft.restaurant.ui.splash

import android.os.Bundle
import android.os.Handler
import android.view.View

import com.codecraft.restaurant.R
import com.codecraft.restaurant.rxbus.RxEvent
import com.codecraft.restaurant.ui.base.BaseFragment
import com.codecraft.restaurant.utils.AppConstants

class SplashFragment : BaseFragment() {

    private var mHandler: Handler? = null
    private var mRunnable: Runnable? = null

    private fun sendNextScreenEvent() {
        val event = RxEvent(RxEvent.EVENT_LOAD_HOME, null)
        rxBus!!.send(event)
    }

    override fun handleBusCallback(event: Any): Int {
        return 0
    }

    override fun onDestroy() {
        mHandler!!.removeCallbacks(mRunnable)
        mHandler = null
        super.onDestroy()
    }

    override fun getFragmentLayoutId(): Int {
        return R.layout.fragment_splash
    }

    override fun initViews(view: View) {
        mHandler = Handler()
        mRunnable = Runnable { this.sendNextScreenEvent() }
        mHandler!!.postDelayed(mRunnable, AppConstants.SPLASH_DELAY)
    }

    override fun resumeScreen() {

    }

    companion object {
        fun newInstance(): SplashFragment {
            val args = Bundle()
            val fragment = SplashFragment()
            fragment.arguments = args
            return fragment
        }
    }
}

