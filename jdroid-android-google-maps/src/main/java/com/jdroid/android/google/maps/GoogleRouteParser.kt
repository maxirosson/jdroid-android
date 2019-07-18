package com.jdroid.android.google.maps

import com.jdroid.android.domain.GeoLocation
import com.jdroid.java.http.parser.json.JsonParser
import com.jdroid.java.json.JSONObject
import java.util.ArrayList

/**
 * From https://developers.google.com/maps/documentation/directions/?hl=es#Limits
 */
class GoogleRouteParser : JsonParser<JSONObject>() {

    override fun parse(json: JSONObject): Any? {
        var route: Route? = null
        // Tranform the string into a json object
        val jsonRoutes = json.getJSONArray("routes")
        // Get the route object
        if (jsonRoutes.length() > 0) {
            route = Route()
            val jsonRoute = jsonRoutes.getJSONObject(0)
            // Get the leg, only one leg as we don't support waypoints
            val leg = jsonRoute.getJSONArray("legs").getJSONObject(0)
            // Get the steps for this leg
            val steps = leg.getJSONArray("steps")
            // Number of steps for use in for loop
            val numSteps = steps.length()
            // Get the total length of the route.
            route.length = leg.getJSONObject("distance").getInt("value")
            // Loop through the steps, creating a segment for each one and decoding any polylines found as we go to
            // add to the route object's map array. Using an explicit for loop because it is faster!
            for (i in 0 until numSteps) {
                // Get the individual step
                val step = steps.getJSONObject(i)
                // Retrieve & decode this segment's polyline and add it to the route.
                route.addPoints(decodePolyLine(step.getJSONObject("polyline").getString("points")))
            }
        }
        return route
    }

    /**
     * Decode a polyline string into a list of GeoLocations. From
     * https://developers.google.com/maps/documentation/directions/?hl=es#Limits
     *
     * @param poly polyline encoded string to decode.
     * @return the list of GeoLocations represented by this polystring.
     */
    private fun decodePolyLine(poly: String): List<GeoLocation> {
        val len = poly.length
        var index = 0
        val decoded = ArrayList<GeoLocation>()
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = poly[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = poly[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            decoded.add(GeoLocation(lat / 1E5, lng / 1E5))
        }

        return decoded
    }
}
