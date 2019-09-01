package com.codecraft.restaurant.ui.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.codecraft.restaurant.rxbus.MainBus
import com.codecraft.restaurant.rxbus.RxHelper
import io.reactivex.observers.DisposableObserver

abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    protected var rxBus: MainBus? = null
    private var disposable: DisposableObserver<Any>? = null
    protected abstract fun handleBusCallback(event: Any)

    init {
        rxBus = RxHelper.getInstance()
        registerForBusCallback()
    }

    private fun registerForBusCallback() {
        if (rxBus != null) {
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

}