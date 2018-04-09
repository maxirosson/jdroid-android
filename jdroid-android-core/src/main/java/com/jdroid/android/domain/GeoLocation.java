package com.jdroid.android.domain;

import android.location.Location;

import java.io.Serializable;

public class GeoLocation implements Serializable {
	
	private static final long serialVersionUID = -2822993513206651288L;
	
	private Double longitude;
	private Double latitude;
	
	public GeoLocation(Double latitude, Double longitude) {
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
}
