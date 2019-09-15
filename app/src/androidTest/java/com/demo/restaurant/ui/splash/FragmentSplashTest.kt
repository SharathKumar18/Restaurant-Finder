package com.demo.restaurant.ui.splash

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.demo.restaurant.R
import com.demo.restaurant.ui.home.HomeActivity
import com.demo.restaurant.utils.AppConstants
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FragmentSplashTest {

    @Rule @JvmField
    var activityRule: ActivityTestRule<HomeActivity> = ActivityTestRule(HomeActivity::class.java)

    @Before
    fun setUp() {
        activityRule.activity
            .supportFragmentManager
            .beginTransaction()
            .add(SplashFragment(), "splashFragment")
            .commit()
    }

    @Test
    fun testSplashTitleVisibleOnAppLaunch() {
        onView(withId(R.id.title)).check(matches(withText(R.string.app_name)))
    }

    @Test
    @Throws(InterruptedException::class)
    fun testRedirectionToHome() {
        Thread.sleep(AppConstants.SPLASH_DELAY)
        onView(withId(R.id.homeContainer))
            .check(matches(isDisplayed()))
    }
}