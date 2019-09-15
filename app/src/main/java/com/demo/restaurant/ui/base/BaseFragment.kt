package com.demo.restaurant.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.demo.restaurant.application.RestaurantApp
import com.demo.restaurant.rxbus.RxHelper
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject

abstract class BaseFragment : Fragment() {

    protected abstract fun getFragmentLayoutId(): Int
    protected abstract fun initViews(view: View)
    abstract fun resumeScreen()
    protected abstract fun handleBusCallback(event: Any)
    @Inject
    lateinit var rxBus: RxHelper
    private var disposable: DisposableObserver<Any>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(getFragmentLayoutId(), container, false)
        RestaurantApp.getContext()?.getApplicationComponent()?.inject(this)
        registerForBusCallback()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
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
}

