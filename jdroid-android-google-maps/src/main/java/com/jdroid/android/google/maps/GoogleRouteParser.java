package com.jdroid.android.google.maps;

import com.jdroid.android.domain.GeoLocation;
import com.jdroid.java.http.parser.json.JsonParser;
import com.jdroid.java.json.JSONArray;
import com.jdroid.java.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * From https://developers.google.com/maps/documentation/directions/?hl=es#Limits
 */
public class GoogleRouteParser extends JsonParser<JSONObject> {

	/**
	 * @see JsonParser#parse(java.lang.Object)
	 */
	@Override
	public Object parse(JSONObject json) {
		Route route = null;
		// Tranform the string into a json object
		JSONArray jsonRoutes = json.getJSONArray("routes");
		// Get the route object
		if (jsonRoutes.length() > 0) {
			route = new Route();
			JSONObject jsonRoute = jsonRoutes.getJSONObject(0);
			// Get the leg, only one leg as we don't support waypoints
			JSONObject leg = jsonRoute.getJSONArray("legs").getJSONObject(0);
			// Get the steps for this leg
			JSONArray steps = leg.getJSONArray("steps");
			// Number of steps for use in for loop
			int numSteps = steps.length();
			// Get the total length of the route.
			route.setLength(leg.getJSONObject("distance").getInt("value"));
			// Loop through the steps, creating a segment for each one and decoding any polylines found as we go to
			// add to the route object's map array. Using an explicit for loop because it is faster!
			for (int i = 0; i < numSteps; i++) {
				// Get the individual step
				JSONObject step = steps.getJSONObject(i);
				// Retrieve & decode this segment's polyline and add it to the route.
				route.addPoints(decodePolyLine(step.getJSONObject("polyline").getString("points")));
			}
		}
		return route;
	}

	/**
	 * Decode a polyline string into a list of GeoLocations. From
	 * https://developers.google.com/maps/documentation/directions/?hl=es#Limits
	 *
	 * @param poly polyline encoded string to decode.
	 * @return the list of GeoLocations represented by this polystring.
	 */
	private List<GeoLocation> decodePolyLine(String poly) {
		int len = poly.length();
		int index = 0;
		List<GeoLocation> decoded = new ArrayList<>();
		int lat = 0;
		int lng = 0;

		while (index < len) {
			int b;
			int shift = 0;
			int result = 0;
			do {
				b = poly.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlat = (result & 1) != 0 ? ~(result >> 1) : (result >> 1);
			lat += dlat;

			shift = 0;
			result = 0;
			do {
				b = poly.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = (result & 1) != 0 ? ~(result >> 1) : (result >> 1);
			lng += dlng;

			decoded.add(new GeoLocation(lat / 1E5, lng / 1E5));
		}

		return decoded;
	}
}
