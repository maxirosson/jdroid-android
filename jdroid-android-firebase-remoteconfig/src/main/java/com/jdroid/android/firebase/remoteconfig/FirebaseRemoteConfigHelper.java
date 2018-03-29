package com.jdroid.android.firebase.remoteconfig;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.WorkerThread;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.firebase.analytics.FirebaseAnalyticsFactory;
import com.jdroid.android.utils.AppUtils;
import com.jdroid.android.utils.SharedPreferencesHelper;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.collections.Maps;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.util.List;
import java.util.Map;

import static android.support.annotation.RestrictTo.Scope.LIBRARY;

public class FirebaseRemoteConfigHelper {

	private static final Logger LOGGER = LoggerUtils.getLogger(FirebaseRemoteConfigHelper.class);

	private static final String MOCKS_ENABLED = "firebase.remote.config.mocks.enabled";

	private static FirebaseRemoteConfig firebaseRemoteConfig;

	private static long DEFAULT_FETCH_EXPIRATION = DateUtils.SECONDS_PER_HOUR * 12;

	private static int retryCount = 0;

	private static Boolean mocksEnabled = false;
	private static Map<String, String> mocks;
	private static SharedPreferencesHelper sharedPreferencesHelper;
	private static List<RemoteConfigParameter> remoteConfigParameters = Lists.newArrayList();
	
	@WorkerThread
	@RestrictTo(LIBRARY)
	static void init() {

		try {
			
			LOGGER.debug("Initializing Firebase Remote Config");
			
			FirebaseApp.initializeApp(AbstractApplication.get());

			firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

			FirebaseRemoteConfigSettings.Builder configSettingsBuilder = new FirebaseRemoteConfigSettings.Builder();
			configSettingsBuilder.setDeveloperModeEnabled(!AppUtils.isReleaseBuildType());

			firebaseRemoteConfig.setConfigSettings(configSettingsBuilder.build());

			if (!Lists.isNullOrEmpty(remoteConfigParameters)) {
				Map<String, Object> defaults = Maps.newHashMap();
				for (RemoteConfigParameter each : remoteConfigParameters) {
					Object defaultValue = each.getDefaultValue();
					if (defaultValue != null) {
						defaults.put(each.getKey(), defaultValue);
					}
				}
				firebaseRemoteConfig.setDefaults(defaults);
			}

			if (!AppUtils.isReleaseBuildType()) {
				sharedPreferencesHelper = SharedPreferencesHelper.get(FirebaseRemoteConfigHelper.class);
				mocks  = (Map<String, String>)sharedPreferencesHelper.loadAllPreferences();
				mocksEnabled = sharedPreferencesHelper.loadPreferenceAsBoolean(MOCKS_ENABLED, false);
			}

			fetch(DEFAULT_FETCH_EXPIRATION, true);
		} catch (Exception e) {
			AbstractApplication.get().getExceptionHandler().logHandledException("Error initializing Firebase Remote Config", e);
		}
	}
	
	public static void setDefaultFetchExpiration(long defaultFetchExpiration) {
		DEFAULT_FETCH_EXPIRATION = defaultFetchExpiration;
	}
	
	@RestrictTo(LIBRARY)
	public static void fetchNow() {
		fetchNow(null);
	}
	
	@RestrictTo(LIBRARY)
	public static void fetchNow(OnSuccessListener<Void> onSuccessListener) {
		fetch(0, false, onSuccessListener);
	}
	
	@RestrictTo(LIBRARY)
	public static void fetch(long cacheExpirationSeconds, Boolean setExperimentUserProperty) {
		fetch(cacheExpirationSeconds, setExperimentUserProperty, null);
	}
	
	@RestrictTo(LIBRARY)
	public static void fetch(final long cacheExpirationSeconds, final Boolean setExperimentUserProperty, final OnSuccessListener<Void> onSuccessListener) {
		if (firebaseRemoteConfig != null) {
			Task<Void> task = firebaseRemoteConfig.fetch(cacheExpirationSeconds);
			task.addOnSuccessListener(new OnSuccessListener<Void>() {
				@Override
				public void onSuccess(Void aVoid) {

					retryCount = 0;

					LOGGER.debug("Firebase Remote Config fetch succeeded");
					// Once the config is successfully fetched it must be activated before newly fetched values are returned.

					Boolean result = firebaseRemoteConfig.activateFetched();
					// true if there was a Fetched Config, and it was activated. false if no Fetched Config was found, or the Fetched Config was already activated.
					LOGGER.debug("Firebase Remote Config activate fetched result: " + result);

					if (setExperimentUserProperty && !Lists.isNullOrEmpty(remoteConfigParameters)) {
						ExecutorUtils.execute(new Runnable() {
							@Override
							public void run() {
								for (RemoteConfigParameter each : remoteConfigParameters) {
									if (each.isUserProperty()) {
										String experimentVariant = FirebaseRemoteConfig.getInstance().getString(each.getKey());
										FirebaseAnalyticsFactory.getFirebaseAnalyticsHelper().setUserProperty(each.getKey(), experimentVariant);
									}
								}
							}
						});
					}

					if (onSuccessListener != null) {
						ExecutorUtils.execute(new Runnable() {
							@Override
							public void run() {
								onSuccessListener.onSuccess(null);
							}
						});
					}
				}
			});
			task.addOnFailureListener(new OnFailureListener() {
				@Override
				public void onFailure(@NonNull Exception exception) {
					LOGGER.error("Firebase Remote Config fetch failed", exception);
					retryCount++;

					if (retryCount <= 3) {
						Bundle bundle = new Bundle();
						bundle.putLong(FirebaseRemoteConfigFetchCommand.CACHE_EXPIRATION_SECONDS, cacheExpirationSeconds);
						bundle.putBoolean(FirebaseRemoteConfigFetchCommand.SET_EXPERIMENT_USER_PROPERTY, setExperimentUserProperty);
						new FirebaseRemoteConfigFetchCommand().start(bundle);
					}
				}
			});
		} else {
			init();
		}
	}

	@Nullable
	public static FirebaseRemoteConfig getFirebaseRemoteConfig() {
		return firebaseRemoteConfig;
	}

	private static FirebaseRemoteConfigValue getFirebaseRemoteConfigValue(RemoteConfigParameter remoteConfigParameter) {
		if (mocksEnabled && !AppUtils.isReleaseBuildType()) {
			return new MockFirebaseRemoteConfigValue(remoteConfigParameter, mocks);
		} else if (firebaseRemoteConfig == null) {
			return new StaticFirebaseRemoteConfigValue(remoteConfigParameter);
		} else {
			return firebaseRemoteConfig.getValue(remoteConfigParameter.getKey());
		}
	}

	public static String getString(RemoteConfigParameter remoteConfigParameter) {
		FirebaseRemoteConfigValue firebaseRemoteConfigValue = getFirebaseRemoteConfigValue(remoteConfigParameter);
		Object value;
		if (firebaseRemoteConfigValue.getSource() == FirebaseRemoteConfig.VALUE_SOURCE_STATIC) {
			value = remoteConfigParameter.getDefaultValue();
		} else {
			value = firebaseRemoteConfigValue.asString();
		}
		log(remoteConfigParameter, firebaseRemoteConfigValue, value);
		return value != null ? value.toString() : null;
	}

	public static Boolean getBoolean(RemoteConfigParameter remoteConfigParameter) {
		FirebaseRemoteConfigValue firebaseRemoteConfigValue = getFirebaseRemoteConfigValue(remoteConfigParameter);
		Object value;
		if (firebaseRemoteConfigValue.getSource() == FirebaseRemoteConfig.VALUE_SOURCE_STATIC) {
			value = remoteConfigParameter.getDefaultValue();
		} else {
			value = firebaseRemoteConfigValue.asBoolean();
		}
		log(remoteConfigParameter, firebaseRemoteConfigValue, value);
		return (Boolean)value;
	}

	public static Double getDouble(RemoteConfigParameter remoteConfigParameter) {
		FirebaseRemoteConfigValue firebaseRemoteConfigValue = getFirebaseRemoteConfigValue(remoteConfigParameter);
		Object value;
		if (firebaseRemoteConfigValue.getSource() == FirebaseRemoteConfig.VALUE_SOURCE_STATIC) {
			value = remoteConfigParameter.getDefaultValue();
			if (value instanceof Float) {
				value = ((Float)value).doubleValue();
			}
		} else {
			value = firebaseRemoteConfigValue.asDouble();
		}
		log(remoteConfigParameter, firebaseRemoteConfigValue, value);
		return (Double)value;
	}

	public static Long getLong(RemoteConfigParameter remoteConfigParameter) {
		FirebaseRemoteConfigValue firebaseRemoteConfigValue = getFirebaseRemoteConfigValue(remoteConfigParameter);
		Object value;
		if (firebaseRemoteConfigValue.getSource() == FirebaseRemoteConfig.VALUE_SOURCE_STATIC) {
			value = remoteConfigParameter.getDefaultValue();
			if (value instanceof Integer) {
				value = ((Integer)value).longValue();
			}
		} else {
			value = firebaseRemoteConfigValue.asLong();
		}
		log(remoteConfigParameter, firebaseRemoteConfigValue, value);
		return (Long)value;
	}
	
	public static String getSourceName(RemoteConfigParameter remoteConfigParameter) {
		return getSourceName(getFirebaseRemoteConfigValue(remoteConfigParameter));
	}
	
	private static String getSourceName(FirebaseRemoteConfigValue firebaseRemoteConfigValue) {
		String source = null;
		if (firebaseRemoteConfigValue.getSource() == FirebaseRemoteConfig.VALUE_SOURCE_STATIC) {
			source = "Static";
		} else if (firebaseRemoteConfigValue.getSource() == FirebaseRemoteConfig.VALUE_SOURCE_REMOTE) {
			source = "Remote";
		} else if (firebaseRemoteConfigValue.getSource() == FirebaseRemoteConfig.VALUE_SOURCE_DEFAULT) {
			source = "Default";
		} else if (firebaseRemoteConfigValue.getSource() == -1) {
			source = "Mock";
		}
		return source;
	}

	private static void log(RemoteConfigParameter remoteConfigParameter, FirebaseRemoteConfigValue firebaseRemoteConfigValue, Object value) {
		if (LoggerUtils.isEnabled()) {
			String source = getSourceName(firebaseRemoteConfigValue);
			LOGGER.info("Loaded Firebase Remote Config. Source [" + source + "] Key [" + remoteConfigParameter.getKey() + "] Value [" + value + "]");
		}
	}
	
	@RestrictTo(LIBRARY)
	public static Boolean isMocksEnabled() {
		return mocksEnabled;
	}
	
	@RestrictTo(LIBRARY)
	public static void setMocksEnabled(Boolean mocksEnabled) {
		if (mocksEnabled) {
			for (RemoteConfigParameter each : remoteConfigParameters) {
				String value = getString(each);
				sharedPreferencesHelper.savePreference(each.getKey(), value);
				mocks.put(each.getKey(), value);
			}
		}
		FirebaseRemoteConfigHelper.mocksEnabled = mocksEnabled;
		sharedPreferencesHelper.savePreference(MOCKS_ENABLED, mocksEnabled);
	}
	
	@RestrictTo(LIBRARY)
	public static void setParameterMock(RemoteConfigParameter remoteConfigParameter, String value) {
		sharedPreferencesHelper.savePreferenceAsync(remoteConfigParameter.getKey(), value);
		mocks.put(remoteConfigParameter.getKey(), value);
	}
	
	public static void addRemoteConfigParameter(RemoteConfigParameter remoteConfigParameter) {
		remoteConfigParameters.add(remoteConfigParameter);
	}
	
	public static void addRemoteConfigParameters(List<RemoteConfigParameter> params) {
		remoteConfigParameters.addAll(params);
	}
	
	public static void addRemoteConfigParameters(RemoteConfigParameter... remoteConfigParameters) {
		addRemoteConfigParameters(Lists.newArrayList(remoteConfigParameters));
	}
	
	public static List<RemoteConfigParameter> getRemoteConfigParameters() {
		return remoteConfigParameters;
	}
}
