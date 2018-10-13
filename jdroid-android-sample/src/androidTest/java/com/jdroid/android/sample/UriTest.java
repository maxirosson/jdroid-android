package com.jdroid.android.sample;


import com.jdroid.android.sample.ui.uri.UriMapperNoFlagsActivity;

import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class UriTest extends AbstractUriTest {

	@Test
	public void noFlagsTestOk() {
		openUri("http://jdroidtools.com/uri/noflags?a=1");

		ViewInteraction viewInteraction = onView(withId(R.id.activity));
		viewInteraction.check(matches(isDisplayed()));
		viewInteraction.check(matches(withText(UriMapperNoFlagsActivity.class.getSimpleName())));
	}

	@Test
	public void noFlagsTestFail() {
		openUri("http://jdroidtools.com/uri/noflags");

		ViewInteraction viewInteraction = onView(withText("Analytics"));
		viewInteraction.check(matches(isDisplayed()));
	}
}

