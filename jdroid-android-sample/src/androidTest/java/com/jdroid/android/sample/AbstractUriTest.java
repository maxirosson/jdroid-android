package com.jdroid.android.sample;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;

import org.junit.After;

public class AbstractUriTest {

	private Activity activity;

	@After
	public void finishActivity() {
		if (activity != null) {
			activity.finish();
		}
	}

	public void openUri(String uri) {

		Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();

		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setPackage(instrumentation.getTargetContext().getPackageName());
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setData(Uri.parse(uri));

		activity = instrumentation.startActivitySync(intent);

		instrumentation.waitForIdleSync();

		if (activity == null) {
			// Log an error message to logcat/instrumentation, that the Activity failed to launch
			String errorMessage = String.format("Failed to launch uri ", uri);
			Bundle bundle = new Bundle();
			bundle.putString(Instrumentation.REPORT_KEY_STREAMRESULT, AbstractUriTest.class.getSimpleName() + " " + errorMessage);
			instrumentation.sendStatus(0, bundle);
			throw new RuntimeException(errorMessage);
		}
	}
}
