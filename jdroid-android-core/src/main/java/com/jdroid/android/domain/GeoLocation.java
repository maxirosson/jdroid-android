package com.jdroid.android.domain;

import android.location.Location;
import androidx.annotation.NonNull;

import java.io.Serializable;

public class GeoLocation implements Serializable {

	private static final long serialVersionUID = -2822993513206651288L;

	private Double longitude;
	private Double latitude;

	public GeoLocation(@NonNull Double latitude, @NonNull Double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public GeoLocation(Location location) {
		latitude = location.getLatitude();
		longitude = location.getLongitude();
	}

	public GeoLocation() {
		this(null, null);
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public boolean isValid() {
		return (latitude != null) && (longitude != null);
	}

	@Override
	public String toString() {
		return Double.toString(getLatitude()) + "," + Double.toString(getLongitude());
	}

	/*
	 * @return: Distance in kilometers between this location and the specified
	 */
	public double distance(Double lat, Double lon) {
		return calculateDistance(latitude, longitude, lat, lon);
	}

	/*
	 * @return: Distance in kilometers between this src location and the specified destination
	 */
	public double calculateDistance(double srcLat, double srcLong, double destLat, double destLong) {
		float[] results = new float[1];
		Location.distanceBetween(srcLat, srcLong, destLat, destLong, results);
		return results[0] / 1000;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		GeoLocation that = (GeoLocation)o;
		return (longitude != null ? longitude.equals(that.longitude) : that.longitude == null) && (latitude != null ? latitude.equals(that.latitude) : that.latitude == null);
	}

	@Override
	public int hashCode() {
		int result = longitude != null ? longitude.hashCode() : 0;
		result = 31 * result + (latitude != null ? latitude.hashCode() : 0);
		return result;
	}
}
