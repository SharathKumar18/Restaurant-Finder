package com.codecraft.restaurant.ui.splash

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.codecraft.restaurant.R
import com.codecraft.restaurant.ui.home.HomeActivity
import com.codecraft.restaurant.utils.AppConstants
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