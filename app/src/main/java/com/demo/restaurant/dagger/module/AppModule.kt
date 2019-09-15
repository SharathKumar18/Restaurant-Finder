package com.demo.restaurant.dagger.module

import com.demo.restaurant.rxbus.RxHelper
import com.demo.restaurant.utils.PreferenceHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun getRxHelper(): RxHelper = RxHelper()

    @Provides
    @Singleton
    fun getPreferenceHelper(): PreferenceHelper = PreferenceHelper()

}
