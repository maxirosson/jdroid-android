package com.jdroid.android.sample;


import com.jdroid.android.sample.ui.home.HomeActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class AnalyticsTest {

	@Rule
	public ActivityTestRule<HomeActivity> activityRule = new ActivityTestRule(HomeActivity.class);

	@Test
	public void listGoesOverTheFold() {
		ViewInteraction viewInteraction = onView(withText("Analytics"));
		viewInteraction.check(matches(isDisplayed()));
		viewInteraction.perform(click());

		onView(withId(R.id.sendExampleEvent)).check(matches(isDisplayed())).perform(click());
		onView(withId(R.id.sendExampleTransaction)).check(matches(isDisplayed())).perform(click());
		onView(withId(R.id.sendExampleTiming)).check(matches(isDisplayed())).perform(click());
	}
}

