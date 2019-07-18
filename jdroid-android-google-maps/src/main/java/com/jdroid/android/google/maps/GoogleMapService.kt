package com.jdroid.android.google.maps

import com.jdroid.android.api.AndroidApiService
import com.jdroid.android.domain.GeoLocation
import com.jdroid.java.http.DefaultServer
import com.jdroid.java.http.Server

class GoogleMapService : AndroidApiService() {

    fun findDirections(source: GeoLocation, destination: GeoLocation, mode: RouteMode): Route? {
        val httpService = newGetService(DIRECTIONS, OUTPUT_FORMAT)
        httpService.addQueryParameter(ORIGIN, toHttpParam(source))
        httpService.addQueryParameter(DESTINATION, toHttpParam(destination))
        httpService.addQueryParameter(SENSOR, "true")
        httpService.addQueryParameter(MODE, mode.argName)
        val route = httpService.execute<Route>(GoogleRouteParser())
        if (route != null) {
            route.mode = mode
        }
        return route
    }

    private fun toHttpParam(geoLocation: GeoLocation): String {
        return geoLocation.latitude.toString() + "," + geoLocation.longitude.toString()
    }

    override fun getServer(): Server {
        return GMAPS_API
    }

    companion object {

        private val GMAPS_API = DefaultServer("maps.googleapis.com/maps/api")

        private const val DIRECTIONS = "directions"
        private const val OUTPUT_FORMAT = "json"
        private const val ORIGIN = "origin"
        private const val DESTINATION = "destination"
        private const val SENSOR = "sensor"
        private const val MODE = "mode"
    }
}
