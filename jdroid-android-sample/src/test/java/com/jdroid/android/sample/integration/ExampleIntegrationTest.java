package com.jdroid.android.sample.integration;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.debug.http.HttpDebugConfiguration;
import com.jdroid.android.firebase.analytics.FirebaseAnalyticsFactory;
import com.jdroid.android.leakcanary.LeakCanaryHelper;
import com.jdroid.android.sample.AbstractIntegrationTest;
import com.jdroid.android.sample.TestAndroidApplication;
import com.jdroid.android.sample.TestAppContext;
import com.jdroid.android.sample.TestExceptionHandler;
import com.jdroid.android.sample.usecase.SampleItemsUseCase;
import com.jdroid.android.strictmode.StrictModeHelper;
import com.jdroid.android.utils.AppUtils;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

public class ExampleIntegrationTest extends AbstractIntegrationTest {

	@Test
	public void example() {
		SampleItemsUseCase sampleItemsUseCase = new SampleItemsUseCase();
		sampleItemsUseCase.run();
		assertNotNull(sampleItemsUseCase.getItems());
		assertEquals(16, sampleItemsUseCase.getItems().size());
	}

	@Test
	public void example2() {
		assertFalse(HttpDebugConfiguration.isHttpMockEnabled());
		assertEquals(TestAndroidApplication.class, AbstractApplication.get().getClass());
		assertNull(AbstractApplication.get().getHomeActivityClass());
		assertEquals(TestAppContext.class, AbstractApplication.get().getAppContext().getClass());
		assertFalse(LeakCanaryHelper.isLeakCanaryEnabled());
		assertFalse(StrictModeHelper.isStrictModeEnabled());
		assertFalse(AppUtils.isReleaseBuildType());
		assertTrue(FirebaseAnalyticsFactory.getFirebaseAnalyticsHelper().isFirebaseAnalyticsEnabled());
		assertEquals(TestExceptionHandler.class, AbstractApplication.get().getExceptionHandler().getClass());
	}
}
