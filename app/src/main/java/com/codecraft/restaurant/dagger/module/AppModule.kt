package com.codecraft.restaurant.dagger.module

import com.codecraft.restaurant.rxbus.RxHelper
import com.codecraft.restaurant.utils.PreferenceHelper
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
