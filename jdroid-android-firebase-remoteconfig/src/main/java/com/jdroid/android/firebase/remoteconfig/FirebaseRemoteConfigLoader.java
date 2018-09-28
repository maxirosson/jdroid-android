package com.jdroid.android.firebase.remoteconfig;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.WorkerThread;

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
import com.jdroid.java.collections.Lists;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.remoteconfig.RemoteConfigLoader;
import com.jdroid.java.remoteconfig.RemoteConfigParameter;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.java.utils.StringUtils;

import org.slf4j.Logger;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static androidx.annotation.RestrictTo.Scope.LIBRARY;

public class FirebaseRemoteConfigLoader implements RemoteConfigLoader {

	private static final Logger LOGGER = LoggerUtils.getLogger(FirebaseRemoteConfigLoader.class);

	private FirebaseRemoteConfig firebaseRemoteConfig;

	private int retryCount = 0;
	private long defaultFetchExpiration = TimeUnit.HOURS.toSeconds(12);

	private List<RemoteConfigParameter> remoteConfigParametersAsUserProperties = Lists.newArrayList();

	public static FirebaseRemoteConfigLoader get() {
		return ((FirebaseRemoteConfigLoader)AbstractApplication.get().getRemoteConfigLoader());
	}

	@WorkerThread
	@RestrictTo(LIBRARY)
	void init() {

		try {

			LOGGER.debug("Initializing Firebase Remote Config");

			FirebaseApp.initializeApp(AbstractApplication.get());

			firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

			FirebaseRemoteConfigSettings.Builder configSettingsBuilder = new FirebaseRemoteConfigSettings.Builder();
			configSettingsBuilder.setDeveloperModeEnabled(!AppUtils.isReleaseBuildType());

			firebaseRemoteConfig.setConfigSettings(configSettingsBuilder.build());

			fetch(defaultFetchExpiration, true);
		} catch (Exception e) {
			AbstractApplication.get().getExceptionHandler().logHandledException("Error initializing Firebase Remote Config", e);
		}
	}

	public void setDefaultFetchExpiration(long defaultFetchExpiration) {
		this.defaultFetchExpiration = defaultFetchExpiration;
	}

	@Override
	public void fetch() {
		fetch(null);
	}

	public void fetch(OnSuccessListener<Void> onSuccessListener) {
		fetch(0, false, onSuccessListener);
	}

	@RestrictTo(LIBRARY)
	public void fetch(long cacheExpirationSeconds, Boolean setExperimentUserProperty) {
		fetch(cacheExpirationSeconds, setExperimentUserProperty, null);
	}

	@RestrictTo(LIBRARY)
	public void fetch(final long cacheExpirationSeconds, final Boolean setExperimentUserProperty, final OnSuccessListener<Void> onSuccessListener) {
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

					if (setExperimentUserProperty && !Lists.isNullOrEmpty(remoteConfigParametersAsUserProperties)) {
						ExecutorUtils.execute(new Runnable() {
							@Override
							public void run() {
								for (RemoteConfigParameter each : remoteConfigParametersAsUserProperties) {
									String experimentVariant = FirebaseRemoteConfig.getInstance().getString(each.getKey());
									FirebaseAnalyticsFactory.getFirebaseAnalyticsHelper().setUserProperty(each.getKey(), experimentVariant);
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
	public FirebaseRemoteConfig getFirebaseRemoteConfig() {
		return firebaseRemoteConfig;
	}

	private FirebaseRemoteConfigValue getFirebaseRemoteConfigValue(@NonNull RemoteConfigParameter remoteConfigParameter) {
		if (firebaseRemoteConfig == null) {
			return new StaticFirebaseRemoteConfigValue(remoteConfigParameter);
		} else {
			return firebaseRemoteConfig.getValue(remoteConfigParameter.getKey());
		}
	}

	@Override
	public String getString(@NonNull RemoteConfigParameter remoteConfigParameter) {
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

	@Override
	public List<String> getStringList(@NonNull RemoteConfigParameter remoteConfigParameter) {
		return StringUtils.splitWithCommaSeparator(getString(remoteConfigParameter));
	}

	@Override
	public Boolean getBoolean(@NonNull RemoteConfigParameter remoteConfigParameter) {
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

	@Override
	public Double getDouble(@NonNull RemoteConfigParameter remoteConfigParameter) {
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

	@Override
	public Long getLong(@NonNull RemoteConfigParameter remoteConfigParameter) {
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

	@Override
	public Object getObject(@NonNull RemoteConfigParameter remoteConfigParameter) {
		FirebaseRemoteConfigValue firebaseRemoteConfigValue = getFirebaseRemoteConfigValue(remoteConfigParameter);
		Object value;
		if (firebaseRemoteConfigValue.getSource() == FirebaseRemoteConfig.VALUE_SOURCE_STATIC) {
			value = remoteConfigParameter.getDefaultValue();
		} else {
			value = firebaseRemoteConfigValue.asString();
		}
		log(remoteConfigParameter, firebaseRemoteConfigValue, value);
		return value;
	}

	public String getSourceName(@NonNull RemoteConfigParameter remoteConfigParameter) {
		return getSourceName(getFirebaseRemoteConfigValue(remoteConfigParameter));
	}

	private String getSourceName(@NonNull FirebaseRemoteConfigValue firebaseRemoteConfigValue) {
		String source = null;
		if (firebaseRemoteConfigValue.getSource() == FirebaseRemoteConfig.VALUE_SOURCE_STATIC) {
			source = "Static";
		} else if (firebaseRemoteConfigValue.getSource() == FirebaseRemoteConfig.VALUE_SOURCE_REMOTE) {
			source = "Remote";
		} else if (firebaseRemoteConfigValue.getSource() == FirebaseRemoteConfig.VALUE_SOURCE_DEFAULT) {
			source = "Default";
		}
		return source;
	}

	private void log(@NonNull RemoteConfigParameter remoteConfigParameter, @NonNull FirebaseRemoteConfigValue firebaseRemoteConfigValue, Object value) {
		if (LoggerUtils.isEnabled()) {
			String source = getSourceName(firebaseRemoteConfigValue);
			LOGGER.info("Loaded Firebase Remote Config. Source [" + source + "] Key [" + remoteConfigParameter.getKey() + "] Value [" + value + "]");
		}
	}

	public void addRemoteConfigParametersAsUserProperties(List<RemoteConfigParameter> remoteConfigParametersAsUserProperties) {
		this.remoteConfigParametersAsUserProperties.addAll(remoteConfigParametersAsUserProperties);
	}

	public List<RemoteConfigParameter> getRemoteConfigParametersAsUserProperties() {
		return remoteConfigParametersAsUserProperties;
	}
}
