package com.pritam.listapp.ui

import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.pritam.listapp.R
import com.pritam.listapp.ui.activity.MainActivity
import com.pritam.listapp.util.RecyclerViewItemCountAssertion.Companion.withItemCount
import com.pritam.listapp.util.TestUtiliy.waitId
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class ListsFragmentTest {

    private val waitTimeout: Long = 10 * 1000 // seconds

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setUp() {
        onView(isRoot()).perform(waitId(R.id.tv_title, waitTimeout))
    }

    @Test
    fun recyclerViewItemFirstTitleTest() {
        val textView = onView(
            allOf(
                withId(R.id.tv_title), withText("Beavers"),
                childAtPosition(
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Beavers")))
    }

    @Test
    fun recyclerViewItemCountTest() {
        onView(withId(R.id.recyclerView)).check(withItemCount(13))
    }

    @Test
    fun swipeRefreshTest() {
        onView(withId(R.id.swipe_refresh_layout))
            .perform(withCustomConstraints(swipeDown(), isDisplayingAtLeast(85)))
    }

    private fun withCustomConstraints(
        action: ViewAction,
        constraints: Matcher<View>
    ): ViewAction? {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return constraints
            }

            override fun getDescription(): String {
                return action.description
            }

            override fun perform(uiController: UiController?, view: View?) {
                action.perform(uiController, view)
            }
        }
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}

