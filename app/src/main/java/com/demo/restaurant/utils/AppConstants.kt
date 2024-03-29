package com.demo.restaurant.utils

object AppConstants {
    const val MAP_ZOOM: Float = 16f
    const val SPLASH_DELAY: Long = 3000
    const val GET_LOCATION = 1
    const val RESULT_STATUS = "OVER_QUERY_LIMIT"
    const val KEY_LONGITUDE = "longitude"
    const val KEY_LATITUDE = "latitude"
    const val PREF_NAME = "RestaurantAppPreference"
    const val MAP_LOCATIONS = "mapLocations"

    interface UIConstants {
        companion object {
            const val SHOW_PROGRESS = 1
            const val HIDE_PROGRESS = 2
            const val DATA_LOADED = 3
        }
    }
}
