package com.jdroid.android.sample;

import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.jdroid.android.sample.ui.home.HomeActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

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

