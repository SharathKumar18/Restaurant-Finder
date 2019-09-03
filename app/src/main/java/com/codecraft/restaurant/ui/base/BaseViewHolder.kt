package com.codecraft.restaurant.ui.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.codecraft.restaurant.application.RestaurantApp
import com.codecraft.restaurant.rxbus.RxHelper
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject

abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    @Inject
    lateinit var rxBus: RxHelper
    private var disposable: DisposableObserver<Any>? = null
    protected abstract fun handleBusCallback(event: Any)

    init {
        initDagger()
        registerForBusCallback()
    }

    private fun initDagger() {
        RestaurantApp.getContext()?.getApplicationComponent()?.inject(this)
    }

    private fun registerForBusCallback() {
        disposable = object : DisposableObserver<Any>() {
            override fun onNext(event: Any) {
                handleBusCallback(event)
            }

            override fun onError(e: Throwable) {}

            override fun onComplete() {}
        }
        rxBus?.toObservable()?.share()?.subscribeWith(disposable)
    }

}