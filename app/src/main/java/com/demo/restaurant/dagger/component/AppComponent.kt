package com.demo.restaurant.dagger.component

import com.demo.restaurant.dagger.module.AppModule
import com.demo.restaurant.ui.MapsActivity
import com.demo.restaurant.ui.base.BaseActivity
import com.demo.restaurant.ui.base.BaseFragment
import com.demo.restaurant.ui.base.BaseViewHolder
import com.demo.restaurant.ui.base.BaseViewModel
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
