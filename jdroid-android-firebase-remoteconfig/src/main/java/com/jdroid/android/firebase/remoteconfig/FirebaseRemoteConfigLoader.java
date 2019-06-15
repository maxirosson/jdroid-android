package com.jdroid.android.firebase.remoteconfig;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.concurrent.AppExecutors;
import com.jdroid.android.firebase.analytics.FirebaseAnalyticsFactory;
import com.jdroid.android.utils.AppUtils;
import com.jdroid.android.utils.SharedPreferencesHelper;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.remoteconfig.RemoteConfigLoader;
import com.jdroid.java.remoteconfig.RemoteConfigParameter;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.java.utils.StringUtils;

import org.slf4j.Logger;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.WorkerThread;

import static androidx.annotation.RestrictTo.Scope.LIBRARY;

public class FirebaseRemoteConfigLoader implements RemoteConfigLoader {

	private static final Logger LOGGER = LoggerUtils.getLogger(FirebaseRemoteConfigLoader.class);

	public static final String CONFIG_STALE = "FIREBASE_REMOTE_CONFIG_STALE";

	private FirebaseRemoteConfig firebaseRemoteConfig;

	private int retryCount = 0;
	private long defaultFetchExpiration = TimeUnit.HOURS.toSeconds(12);

	private List<RemoteConfigParameter> remoteConfigParametersAsUserProperties = Lists.newArrayList();

	public static FirebaseRemoteConfigLoader get() {
		return ((FirebaseRemoteConfigLoader)AbstractApplication.get().getRemoteConfigLoader());
	}

	@WorkerThread
	private void init() {

		if (firebaseRemoteConfig == null) {
			synchronized (this) {
				if (firebaseRemoteConfig == null) {
					try {

						LOGGER.debug("Initializing Firebase Remote Config");

						FirebaseApp.initializeApp(AbstractApplication.get());

						firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

						FirebaseRemoteConfigSettings.Builder configSettingsBuilder = new FirebaseRemoteConfigSettings.Builder();
						if (!AppUtils.isReleaseBuildType()) {
							configSettingsBuilder.setMinimumFetchIntervalInSeconds(0);
						}

						Task<Void> task = firebaseRemoteConfig.setConfigSettingsAsync(configSettingsBuilder.build());

						try {
							Tasks.await(task);
						} catch (ExecutionException e) {
							AbstractApplication.get().getExceptionHandler().logHandledException("Error initializing Firebase Remote Config", e);
						} catch (InterruptedException e) {
							AbstractApplication.get().getExceptionHandler().logHandledException("Error initializing Firebase Remote Config", e);
						}

					} catch (Exception e) {
						AbstractApplication.get().getExceptionHandler().logHandledException("Error initializing Firebase Remote Config", e);
					}
				}
			}
		}

	}

	@WorkerThread
	@Override
	public void fetch() {
		fetch(false, null);
	}

	@WorkerThread
	@RestrictTo(LIBRARY)
	public void fetch(@NonNull Boolean setExperimentUserProperty, @Nullable OnSuccessListener<Void> onSuccessListener) {
		init();

		long cacheExpirationSeconds = defaultFetchExpiration;
		// If the app is using developer mode or cache is stale, cacheExpiration is set to 0,
		// so each fetch will retrieve values from the service.
		if (firebaseRemoteConfig.getInfo().getConfigSettings().getMinimumFetchIntervalInSeconds() == 0 ||
			SharedPreferencesHelper.get().loadPreferenceAsBoolean(CONFIG_STALE, false)) {
			cacheExpirationSeconds = 0;
			SharedPreferencesHelper.get().savePreferenceAsync(FirebaseRemoteConfigLoader.CONFIG_STALE, false);
		}

		LOGGER.debug("Firebase Remote Config fetch started. Cache Expiration: " + cacheExpirationSeconds);

		Task<Void> task = firebaseRemoteConfig.fetch(cacheExpirationSeconds);
		task.addOnSuccessListener(new OnSuccessListener<Void>() {
			@Override
			public void onSuccess(Void aVoid) {

				retryCount = 0;

				LOGGER.debug("Firebase Remote Config fetch succeeded");
				// Once the config is successfully fetched it must be activated before newly fetched values are returned.

				Task<Boolean> activateTask = firebaseRemoteConfig.activate();
				activateTask.addOnSuccessListener(new OnSuccessListener<Boolean>() {
					@Override
					public void onSuccess(Boolean result) {
						// true if there was a Fetched Config, and it was activated. false if no Fetched Config was found, or the Fetched Config was already activated.
						LOGGER.debug("Firebase Remote Config activate fetched result: " + result);

						if (setExperimentUserProperty && !Lists.isNullOrEmpty(remoteConfigParametersAsUserProperties)) {
							AppExecutors.getNetworkIOExecutor().execute(new Runnable() {
								@Override
								public void run() {
									for (RemoteConfigParameter each : remoteConfigParametersAsUserProperties) {
										String experimentVariant = FirebaseRemoteConfig.getInstance().getString(each.getKey());
										FirebaseAnalyticsFactory.INSTANCE.getFirebaseAnalyticsFacade().setUserProperty(each.getKey(), experimentVariant);
									}
								}
							});
						}

						if (onSuccessListener != null) {
							AppExecutors.getNetworkIOExecutor().execute(new Runnable() {
								@Override
								public void run() {
									onSuccessListener.onSuccess(null);
								}
							});
						}
					}
				});
				activateTask.addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception exception) {
						AbstractApplication.get().getExceptionHandler().logHandledException("Firebase Remote Config activation failed", exception);
						retryCount++;

						if (retryCount <= 3) {
							new FirebaseRemoteConfigFetchCommand().start(setExperimentUserProperty);
						}
					}
				});
			}
		});
		task.addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception exception) {
				AbstractApplication.get().getExceptionHandler().logHandledException("Firebase Remote Config fetch failed", exception);
				retryCount++;

				if (retryCount <= 3) {
					new FirebaseRemoteConfigFetchCommand().start(setExperimentUserProperty);
				}
			}
		});
	}

	public void setDefaultFetchExpiration(long defaultFetchExpiration) {
		this.defaultFetchExpiration = defaultFetchExpiration;
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
