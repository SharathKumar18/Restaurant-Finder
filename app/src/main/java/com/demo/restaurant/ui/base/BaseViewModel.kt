package com.demo.restaurant.ui.base

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.demo.restaurant.application.RestaurantApp
import com.demo.restaurant.data.model.UiHelper
import com.demo.restaurant.rxbus.RxHelper
import com.demo.restaurant.utils.AppConstants
import com.demo.restaurant.utils.PreferenceHelper
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {

    @Inject
    lateinit var rxBus: RxHelper
    @Inject
    lateinit var preferenceHelper : PreferenceHelper
    private var disposable: DisposableObserver<Any>? = null
    protected abstract fun handleBusCallback(event: Any)
    private val uiLiveData = MutableLiveData<UiHelper>()

    init {
        initDagger()
        registerForBusCallback()
    }

    fun getUiLiveData(): MutableLiveData<UiHelper> {
        return uiLiveData
    }

    private fun initDagger() {
        RestaurantApp.getContext()?.getApplicationComponent()?.inject(this)
    }

    fun showProgress() {
        val helper = UiHelper(AppConstants.UIConstants.SHOW_PROGRESS)
        uiLiveData.value=helper
    }

    fun hideProgress() {
        val helper = UiHelper(AppConstants.UIConstants.HIDE_PROGRESS)
        uiLiveData.value=helper
    }

    fun sendUiData(status: Int) {
        val helper = UiHelper(status)
        uiLiveData.value=helper
    }

    @SuppressLint("CheckResult")
    private fun registerForBusCallback() {
        disposable = object : DisposableObserver<Any>() {
            override fun onNext(event: Any) {
                handleBusCallback(event)
            }
            override fun onError(e: Throwable) {}
            override fun onComplete() {}
        }
        rxBus.toObservable()?.share()?.subscribeWith(disposable)
    }
}