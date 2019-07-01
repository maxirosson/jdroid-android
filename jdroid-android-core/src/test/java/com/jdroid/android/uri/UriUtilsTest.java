package com.jdroid.android.uri;

import android.net.Uri;

import com.jdroid.android.AbstractUnitTest;

import org.junit.Test;

import static junit.framework.Assert.assertTrue;

public class UriUtilsTest extends AbstractUnitTest {

	@Test
	public void randomParamTest() {
		verifyRandomParam("http://jdroidtools.com", "http://jdroidtools.com/?rnd=");
		verifyRandomParam("http://jdroidtools.com/", "http://jdroidtools.com/?rnd=");
		verifyRandomParam("http://jdroidtools.com/?a=1", "http://jdroidtools.com/?a=1&rnd=");
		verifyRandomParam("http://jdroidtools.com/a", "http://jdroidtools.com/a?rnd=");
		verifyRandomParam("http://jdroidtools.com/a/b", "http://jdroidtools.com/a/b?rnd=");
		verifyRandomParam("http://jdroidtools.com/a?b=1", "http://jdroidtools.com/a?b=1&rnd=");
		verifyRandomParam("http://jdroidtools.com/a/b?c=1", "http://jdroidtools.com/a/b?c=1&rnd=");
	}

	private void verifyRandomParam(String originalUrl, String expectedUrl) {
		String newUrl = UriUtils.INSTANCE.addRandomParam(Uri.parse(originalUrl)).toString();
		assertTrue("Transformed url: " + newUrl, newUrl.startsWith(expectedUrl));
	}
}
