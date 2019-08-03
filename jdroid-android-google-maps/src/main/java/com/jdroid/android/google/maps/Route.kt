package com.jdroid.android.google.maps

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds.Builder
import com.google.android.gms.maps.model.PolylineOptions
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.domain.GeoLocation

class Route {

    val points = mutableListOf<GeoLocation>()
    var length: Int? = null
    var mode: RouteMode? = null

    fun addPoint(p: GeoLocation) {
        points.add(p)
    }

    fun addPoints(points: List<GeoLocation>) {
        this.points.addAll(points)
    }

    fun toPolyline(builder: Builder): PolylineOptions {
        val polylineOptions = PolylineOptions()
        for (gp in points) {
            val latLong = LatLng(gp.requireLatitude(), gp.requireLongitude())
            polylineOptions.add(latLong)
            builder.include(latLong)
        }
        polylineOptions.color(AbstractApplication.get().resources.getColor(mode!!.colorId))
        return polylineOptions
    }

    fun isValid(): Boolean {
        return length != null && length!! > 0
    }
}