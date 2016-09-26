package com.jdroid.android.google.gcm;

import android.os.Bundle;

import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.activity.ActivityDelegate;
import com.jdroid.android.application.AbstractAppModule;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.debug.PreferencesAppender;

import java.util.List;

public abstract class AbstractGcmAppModule extends AbstractAppModule {

	public static final String MODULE_NAME = AbstractGcmAppModule.class.getName();

	private GcmDebugContext gcmDebugContext;
	private GcmMessageResolver gcmMessageResolver;
	private GcmListenerResolver gcmListenerResolver;
	private Boolean gcmInitialized = false;

	public AbstractGcmAppModule() {
		gcmMessageResolver = createGcmMessageResolver();
		gcmListenerResolver = createGcmListenerResolver();
	}

	public static AbstractGcmAppModule get() {
		return (AbstractGcmAppModule)AbstractApplication.get().getAppModule(MODULE_NAME);
	}

	protected GcmDebugContext createGcmDebugContext() {
		return new GcmDebugContext();
	}

	public GcmDebugContext getGcmDebugContext() {
		synchronized (AbstractApplication.class) {
			if (gcmDebugContext == null) {
				gcmDebugContext = createGcmDebugContext();
			}
		}
		return gcmDebugContext;
	}

	@Override
	public List<PreferencesAppender> getPreferencesAppenders() {
		return getGcmDebugContext().getPreferencesAppenders();
	}

	public GcmMessageResolver getGcmMessageResolver(String from) {
		return gcmMessageResolver;
	}

	public abstract GcmSender getGcmSender();

	public abstract GcmMessageResolver createGcmMessageResolver();

	public GcmListenerResolver createGcmListenerResolver() {
		return new GcmListenerResolver();
	}

	public GcmListenerResolver getGcmListenerResolver() {
		return gcmListenerResolver;
	}

	@Override
	public ActivityDelegate createActivityDelegate(AbstractFragmentActivity abstractFragmentActivity) {
		return !gcmInitialized ? new ActivityDelegate(abstractFragmentActivity) {

			@Override
			public void onCreate(Bundle savedInstanceState) {
				if (!gcmInitialized) {
					startGcmRegistration(true);
					gcmInitialized = true;
				}
			}
		} : null;
	}

	public abstract void onRegisterOnServer(String registrationToken, Boolean updateLastActiveTimestamp, Bundle bundle);

	protected List<String> getSubscriptionTopics() {
		return null;
	}

	@Override
	public void onInstanceIdTokenRefresh() {
		startGcmRegistration(false);
	}

	@Override
	public void onGooglePlayServicesUpdated() {
		startGcmRegistration(false);
	}

	@Override
	public void onInitializeGcmTasks() {
		startGcmRegistration(false);
	}

	public void startGcmRegistration(Boolean updateLastActiveTimestamp) {
		createGcmRegistrationCommand().start(updateLastActiveTimestamp);
	}

	protected GcmRegistrationCommand createGcmRegistrationCommand() {
		return new GcmRegistrationCommand();
	}
}