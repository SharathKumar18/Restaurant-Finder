package com.demo.restaurant.ui.detail

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import androidx.test.rule.ActivityTestRule
import com.demo.restaurant.R
import com.demo.restaurant.ui.home.HomeActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.demo.restaurant.data.response.Result

class DetailFragmentTest {

    @Rule
    @JvmField
    var mActivityRule: ActivityTestRule<HomeActivity> =
        ActivityTestRule(HomeActivity::class.java)

    @Before
    @Throws(Throwable::class)
    fun setUp() {
        runOnUiThread { mActivityRule.activity.loadDetailFragment(Result()) }
    }

    @Test
    fun testImageViewVisible() {
        onView(withId(R.id.detailThumb))
            .check(matches(isDisplayed()))
    }
}