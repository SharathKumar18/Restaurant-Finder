package com.codecraft.restaurant.rxbus


data class RxEvent<T>(var eventTag: Int = 0, var data: T? = null) {

    companion object {
        const val EVENT_LOAD_HOME = 1
        const val EVENT_LOCATION_UPDATED = 2
    }
}
