package com.jdroid.android.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

public class BoundLocationManager {

	private static final Logger LOGGER = LoggerUtils.getLogger(BoundLocationManager.class);

	public static void bindLocationListenerIn(LifecycleOwner lifecycleOwner, LocationListener listener) {
		new BoundLocationListener(lifecycleOwner, listener);
	}

	@SuppressWarnings("MissingPermission")
	static class BoundLocationListener implements LifecycleObserver {

		private LocationManager locationManager;
		private final LocationListener listener;

		public BoundLocationListener(LifecycleOwner lifecycleOwner, LocationListener listener) {
			this.listener = listener;
			lifecycleOwner.getLifecycle().addObserver(this);
		}

		@OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
		void addLocationListener() {
			// TODO Use the Fused Location Provider from Google Play Services instead.
			// https://developers.google.com/android/reference/com/google/android/gms/location/FusedLocationProviderApi
			locationManager = (LocationManager)AbstractApplication.get().getSystemService(Context.LOCATION_SERVICE);
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
			LOGGER.debug("Listener added");

			// Force an update with the last location, if available.
			Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (lastLocation != null) {
				listener.onLocationChanged(lastLocation);
			}
		}

		@OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
		void removeLocationListener() {
			if (locationManager == null) {
				return;
			}
			locationManager.removeUpdates(listener);
			locationManager = null;
			LOGGER.debug("Listener removed");
		}
	}
}
