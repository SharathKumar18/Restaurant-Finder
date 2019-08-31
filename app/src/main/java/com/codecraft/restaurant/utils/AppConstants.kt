package com.codecraft.restaurant.utils

object AppConstants {
    const val SPLASH_DELAY:Long = 3000
    const val GET_LOCATION = 1
    const val RESULT_STATUS = "OVER_QUERY_LIMIT"

    interface UIConstants {
        companion object {
            const val SHOW_PROGRESS = 1
            const val HIDE_PROGRESS = 2
        }
    }
}
