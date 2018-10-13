package com.jdroid.android.google.inappbilling;

import androidx.annotation.NonNull;

import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.activity.ActivityDelegate;
import com.jdroid.android.application.AbstractAppModule;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.google.inappbilling.analytics.FirebaseInAppBillingAnalyticsTracker;
import com.jdroid.android.google.inappbilling.analytics.GoogleInAppBillingAnalyticsTracker;
import com.jdroid.android.google.inappbilling.analytics.InAppBillingAnalyticsSender;
import com.jdroid.android.google.inappbilling.analytics.InAppBillingAnalyticsTracker;
import com.jdroid.android.google.inappbilling.client.DeveloperPayloadVerificationStrategy;
import com.jdroid.android.google.inappbilling.client.SimpleDeveloperPayloadVerificationStrategy;
import com.jdroid.java.analytics.AnalyticsSender;
import com.jdroid.java.analytics.AnalyticsTracker;
import com.jdroid.java.collections.Lists;

import java.util.List;

public class InAppBillingAppModule extends AbstractAppModule {

	public static final String MODULE_NAME = InAppBillingAppModule.class.getName();

	public static InAppBillingAppModule get() {
		return (InAppBillingAppModule)AbstractApplication.get().getAppModule(MODULE_NAME);
	}

	private InAppBillingContext inAppBillingContext;
	private DeveloperPayloadVerificationStrategy developerPayloadVerificationStrategy = new SimpleDeveloperPayloadVerificationStrategy();

	public InAppBillingContext getInAppBillingContext() {
		if (isInAppBillingEnabled()) {
			synchronized (InAppBillingAppModule.class) {
				if (inAppBillingContext == null) {
					inAppBillingContext = createInAppBillingContext();
				}
			}
			return inAppBillingContext;
		} else {
			return null;
		}
	}

	public Boolean isInAppBillingEnabled() {
		return true;
	}

	protected InAppBillingContext createInAppBillingContext() {
		return new InAppBillingContext();
	}

	@Override
	public ActivityDelegate createActivityDelegate(AbstractFragmentActivity abstractFragmentActivity) {
		return isInAppBillingEnabled() ? new InAppBillingActivityDelegate(abstractFragmentActivity) : null;
	}

	@NonNull
	@Override
	public AnalyticsSender<? extends AnalyticsTracker> createModuleAnalyticsSender(List<? extends AnalyticsTracker> analyticsTrackers) {
		return new InAppBillingAnalyticsSender((List<InAppBillingAnalyticsTracker>)analyticsTrackers);
	}

	@Override
	public List<? extends AnalyticsTracker> createModuleAnalyticsTrackers() {
		return Lists.newArrayList(new GoogleInAppBillingAnalyticsTracker(), new FirebaseInAppBillingAnalyticsTracker());
	}

	@NonNull
	@Override
	public InAppBillingAnalyticsSender getModuleAnalyticsSender() {
		return (InAppBillingAnalyticsSender)super.getModuleAnalyticsSender();
	}

	public DeveloperPayloadVerificationStrategy getDeveloperPayloadVerificationStrategy() {
		return developerPayloadVerificationStrategy;
	}

	public void setDeveloperPayloadVerificationStrategy(DeveloperPayloadVerificationStrategy developerPayloadVerificationStrategy) {
		this.developerPayloadVerificationStrategy = developerPayloadVerificationStrategy;
	}
}