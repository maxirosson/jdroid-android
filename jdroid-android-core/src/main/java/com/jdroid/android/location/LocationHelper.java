package com.jdroid.android.location;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.RequiresPermission;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.utils.AlarmUtils;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.util.List;

public class LocationHelper implements LocationListener {

	private static final Logger LOGGER = LoggerUtils.getLogger(LocationHelper.class);
	private static final String GPS_TIMEOUT_ACTION = "ACTION_GPS_TIMEOUT";

	// 2 minutes
	private static final int MAXIMUM_TIME_DELTA = 120000;

	private static final LocationHelper INSTANCE = new LocationHelper();
	private static final int LOCATION_MIN_TIME = 10000;
	private static final int LOCATION_MAX_TIME = 30000;

	private Location location;
	private Long locationTime;
	private Boolean started = false;

	private LocationManager locationManager;

	public static LocationHelper get() {
		return INSTANCE;
	}

	public LocationHelper() {

		// Acquire a reference to the system Location Manager
		locationManager = (LocationManager)AbstractApplication.get().getSystemService(Context.LOCATION_SERVICE);

		BroadcastReceiver stopLocalizationBroadcastReceiver = new BroadcastReceiver() {

			@Override
			@RequiresPermission(anyOf = { Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION })
			public void onReceive(Context context, Intent intent) {
				stopLocalization();
			}
		};
		AbstractApplication.get().registerReceiver(stopLocalizationBroadcastReceiver,
			new IntentFilter(GPS_TIMEOUT_ACTION));
	}

	public Boolean isLocationEnabled() {
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
			|| locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	}

	/**
	 * Register the listener with the Location Manager to receive location updates
	 */
	@RequiresPermission(anyOf = { Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION })
	public synchronized void startLocalization() {
		if (!started && hasSignificantlyOlderLocation()) {

			started = true;

			// get all enabled providers
			List<String> enabledProviders = locationManager.getProviders(true);

			boolean gpsProviderEnabled = enabledProviders != null && enabledProviders.contains(LocationManager.GPS_PROVIDER);
			boolean networkProviderEnabled = enabledProviders != null && enabledProviders.contains(LocationManager.NETWORK_PROVIDER);

			if (gpsProviderEnabled || networkProviderEnabled) {
				if (gpsProviderEnabled) {
					locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_MIN_TIME, 0, this);
				}
				if (networkProviderEnabled) {
					locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_MIN_TIME, 0, this);
				}

				AlarmUtils.scheduleElapsedRealtimeAlarm(SystemClock.elapsedRealtime() + LOCATION_MAX_TIME,
					getCancelPendingIntent());

				LOGGER.info("Localization started");
			} else {
				started = false;
				LOGGER.info("All providers disabled");
			}
		}
	}

	private PendingIntent getCancelPendingIntent() {
		Intent gpsIntent = new Intent(GPS_TIMEOUT_ACTION);
		return PendingIntent.getBroadcast(AbstractApplication.get(), 0, gpsIntent,
			PendingIntent.FLAG_CANCEL_CURRENT);
	}

	/**
	 * Remove the listener to receive location updates
	 */
	@RequiresPermission(anyOf = { Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION })
	public synchronized void stopLocalization() {
		if (started) {
			AlarmUtils.cancelAlarm(getCancelPendingIntent());
			locationManager.removeUpdates(this);
			if (location == null) {
				location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				if (location == null) {
					location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				}
				locationTime = DateUtils.nowMillis();
			}
			started = false;
			LOGGER.info("Localization stopped");
		}
	}

	/**
	 * @see android.location.LocationListener#onLocationChanged(android.location.Location)
	 */
	@Override
	public void onLocationChanged(Location location) {
		if (isBetterLocation(location, this.location)) {
			LOGGER.info("Location changed");
			this.location = location;
			locationTime = DateUtils.nowMillis();
		} else {
			LOGGER.info("Location discarded");
		}
	}

	public void onMapLocationChanged(Location location) {
		if (location != null) {
			LOGGER.info("Location changed");
			this.location = location;
			locationTime = DateUtils.nowMillis();
		} else {
			LOGGER.info("Location discarded");
		}
	}

	/**
	 * @see android.location.LocationListener#onStatusChanged(java.lang.String, int, android.os.Bundle)
	 */
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// Do Nothing
	}

	/**
	 * @see android.location.LocationListener#onProviderEnabled(java.lang.String)
	 */
	@Override
	public void onProviderEnabled(String provider) {
		LOGGER.info("Provider enabled: " + provider);
		// Do Nothing
	}

	/**
	 * @see android.location.LocationListener#onProviderDisabled(java.lang.String)
	 */
	@Override
	public void onProviderDisabled(String provider) {
		LOGGER.info("Provider disabled: " + provider);
		// Do Nothing
	}

	/**
	 * Determines whether one Location reading is better than the current Location fix
	 *
	 * @param location The new Location that you want to evaluate
	 * @param currentBestLocation The current Location fix, to which you want to compare the new one
	 */
	protected boolean isBetterLocation(Location location, Location currentBestLocation) {
		if (currentBestLocation == null) {
			// A new location is always better than no location
			return true;
		}

		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > MAXIMUM_TIME_DELTA;
		boolean isSignificantlyOlder = timeDelta < -MAXIMUM_TIME_DELTA;
		boolean isNewer = timeDelta > 0;

		// If it's been more than two minutes since the current location, use the new location
		// because the user has likely moved
		if (isSignificantlyNewer) {
			return true;
			// If the new location is more than two minutes older, it must be worse
		} else if (isSignificantlyOlder) {
			return false;
		}

		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int)(location.getAccuracy() - currentBestLocation.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(location.getProvider(), currentBestLocation.getProvider());

		// Determine location quality using a combination of timeliness and accuracy
		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
			return true;
		}
		return false;
	}

	/**
	 * Checks whether two providers are the same
	 */
	private boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}

	/**
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}

	public Boolean hasLocation() {
		return location != null;
	}

	public Boolean hasSignificantlyOlderLocation() {
		if (location != null) {
			long timeDelta = DateUtils.nowMillis() - locationTime;
			return timeDelta > MAXIMUM_TIME_DELTA;
		}
		return true;
	}

	public Double getLatitude() {
		return location != null ? location.getLatitude() : null;
	}

	public Double getLongitude() {
		return location != null ? location.getLongitude() : null;
	}
}
