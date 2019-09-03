package com.codecraft.restaurant.ui.home

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.codecraft.restaurant.R
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeActivityTest {

    @Rule @JvmField
    var activityRule: ActivityTestRule<HomeActivity> = ActivityTestRule(HomeActivity::class.java)

    @Test
    fun testActivityVisibleOnLaunch() {
        onView(withId(R.id.homeContainer))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testToolbarVisible() {
        onView(withId(R.id.toolbar)).perform(setViewVisibility(true))
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()))
    }

    @Test
    fun testToolbarNameIsHome() {
        onView(withId(R.id.toolbar)).perform(setViewVisibility(true))
        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText(R.string.title_home))))
    }

    private fun setViewVisibility(value: Boolean): ViewAction {
        return object : ViewAction {
            override fun perform(uiController: androidx.test.espresso.UiController?, view: View?) {
                view?.visibility = if (value) View.VISIBLE else View.GONE
            }

            override fun getDescription(): String {
                return "Show / Hide View"
            }

            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(View::class.java)
            }
        }
    }
}
