package com.jdroid.android.google.maps;

import com.jdroid.android.api.AndroidApiService;
import com.jdroid.android.domain.GeoLocation;
import com.jdroid.java.http.DefaultServer;
import com.jdroid.java.http.HttpService;
import com.jdroid.java.http.Server;

public class GoogleMapService extends AndroidApiService {

	private static final Server GMAPS_API = new DefaultServer("maps.googleapis.com/maps/api");

	private static final Object DIRECTIONS = "directions";
	private static final String OUTPUT_FORMAT = "json";
	private static final String ORIGIN = "origin";
	private static final String DESTINATION = "destination";
	private static final String SENSOR = "sensor";
	private static final String MODE = "mode";

	public Route findDirections(GeoLocation source, GeoLocation destination, RouteMode mode) {
		HttpService httpService = newGetService(DIRECTIONS, OUTPUT_FORMAT);
		httpService.addQueryParameter(ORIGIN, toHttpParam(source));
		httpService.addQueryParameter(DESTINATION, toHttpParam(destination));
		httpService.addQueryParameter(SENSOR, "true");
		httpService.addQueryParameter(MODE, mode.getArgName());
		Route route = httpService.execute(new GoogleRouteParser());
		if (route != null) {
			route.setMode(mode);
		}
		return route;
	}

	private String toHttpParam(GeoLocation geoLocation) {
		return Double.toString(geoLocation.getLatitude()) + "," + Double.toString(geoLocation.getLongitude());
	}

	@Override
	protected Server getServer() {
		return GMAPS_API;
	}
}
