package com.codecraft.restaurant.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.codecraft.restaurant.rxbus.MainBus
import com.codecraft.restaurant.rxbus.RxHelper
import com.google.android.material.snackbar.Snackbar
import io.reactivex.observers.DisposableObserver

abstract class BaseFragment : Fragment() {

    protected abstract fun getFragmentLayoutId(): Int
    protected abstract fun initViews(view: View)
    abstract fun resumeScreen()
    protected abstract fun handleBusCallback(event: Any)
    var rxBus: MainBus? = null
    private var disposable: DisposableObserver<Any>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(getFragmentLayoutId(), container, false)
        rxBus = RxHelper.getInstance()
        registerForBusCallback()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
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

    private fun unSubScribe() {
        if (disposable != null && !disposable!!.isDisposed) {
            disposable!!.dispose()
            disposable = null
        }
    }

    override fun onDestroyView() {
        unSubScribe()
        super.onDestroyView()
    }

    fun showSnackBarMessage(message: String) {
        val snackBar = view?.let { Snackbar.make(it, message, Snackbar.LENGTH_LONG) }
        snackBar?.show()
    }
}

