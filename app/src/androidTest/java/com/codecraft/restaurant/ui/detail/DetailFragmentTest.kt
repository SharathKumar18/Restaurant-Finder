package com.codecraft.restaurant.ui.detail

import android.view.View
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import androidx.test.rule.ActivityTestRule
import com.codecraft.restaurant.R
import com.codecraft.restaurant.ui.home.HomeActivity
import org.hamcrest.Matcher
import org.hamcrest.core.AllOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.matchers.JUnitMatchers.containsString
import com.codecraft.restaurant.data.response.Result

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