package com.codecraft.restaurant.dagger.component

import com.codecraft.restaurant.dagger.module.AppModule
import com.codecraft.restaurant.ui.MapsActivity
import com.codecraft.restaurant.ui.base.BaseActivity
import com.codecraft.restaurant.ui.base.BaseFragment
import com.codecraft.restaurant.ui.base.BaseViewHolder
import com.codecraft.restaurant.ui.base.BaseViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(baseActivity: BaseActivity)

    fun inject(baseFragment: BaseFragment)

    fun inject(baseViewModel: BaseViewModel)

    fun inject(viewHolder: BaseViewHolder)

    fun inject(mapsActivity: MapsActivity)

}
