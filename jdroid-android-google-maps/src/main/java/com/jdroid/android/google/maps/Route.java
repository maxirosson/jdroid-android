package com.jdroid.android.google.maps;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds.Builder;
import com.google.android.gms.maps.model.PolylineOptions;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.domain.GeoLocation;

import java.util.ArrayList;
import java.util.List;

public class Route {

	private List<GeoLocation> points;
	private int length;
	private RouteMode mode;

	public Route() {
		points = new ArrayList<>();
	}

	public void addPoint(GeoLocation p) {
		points.add(p);
	}

	public void addPoints(List<GeoLocation> points) {
		this.points.addAll(points);
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getLength() {
		return length;
	}

	public PolylineOptions toPolyline(Builder builder) {
		PolylineOptions polylineOptions = new PolylineOptions();
		for (GeoLocation gp : points) {
			LatLng latLong = new LatLng(gp.getLatitude(), gp.getLongitude());
			polylineOptions.add(latLong);
			builder.include(latLong);
		}
		polylineOptions.color(AbstractApplication.get().getResources().getColor(mode.getColorId()));
		return polylineOptions;
	}

	public boolean isValid() {
		return length > 0;
	}

	void setMode(RouteMode mode) {
		this.mode = mode;
	}

	public List<GeoLocation> getPoints() {
		return points;
	}

	public RouteMode getMode() {
		return mode;
	}

}