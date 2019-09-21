package com.demo.restaurant.application

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.demo.restaurant.dagger.component.AppComponent
import com.demo.restaurant.dagger.component.DaggerAppComponent
import com.demo.restaurant.dagger.module.AppModule

class RestaurantApp : Application(), LifecycleObserver {

    private lateinit var appComponent:AppComponent

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        initDaggerComponent()
    }

    private fun initDaggerComponent() {
        appComponent= DaggerAppComponent.builder().appModule(AppModule()).build()
    }

    fun getApplicationComponent(): AppComponent {
        return appComponent
    }

    override fun onTerminate() {
        super.onTerminate()
        instance = null
    }

    companion object {
        private var instance: RestaurantApp? = null
        fun getContext(): RestaurantApp? {
            return instance
        }
    }
}
