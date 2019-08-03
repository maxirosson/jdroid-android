package com.jdroid.android.domain

import android.location.Location

import java.io.Serializable

class GeoLocation : Serializable {

    var longitude: Double? = null
    var latitude: Double? = null

    val isValid: Boolean
        get() = latitude != null && longitude != null

    constructor(latitude: Double? = null, longitude: Double? = null) {
        this.latitude = latitude
        this.longitude = longitude
    }

    constructor(location: Location) {
        latitude = location.latitude
        longitude = location.longitude
    }

    fun requireLongitude(): Double {
        return longitude!!
    }

    fun requireLatitude(): Double {
        return latitude!!
    }

    override fun toString(): String {
        return latitude.toString() + "," + longitude.toString()
    }

    /*
	 * @return: Distance in kilometers between this location and the specified
	 */
    fun distance(lat: Double, lon: Double): Double {
        return calculateDistance(requireLatitude(), requireLongitude(), lat, lon)
    }

    /*
	 * @return: Distance in kilometers between this src location and the specified destination
	 */
    fun calculateDistance(srcLat: Double, srcLong: Double, destLat: Double, destLong: Double): Double {
        val results = FloatArray(1)
        Location.distanceBetween(srcLat, srcLong, destLat, destLong, results)
        return (results[0] / 1000).toDouble()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val that = other as GeoLocation?
        return (if (longitude != null) longitude == that!!.longitude else that!!.longitude == null) && if (latitude != null) latitude == that.latitude else that.latitude == null
    }

    override fun hashCode(): Int {
        var result = if (longitude != null) longitude!!.hashCode() else 0
        result = 31 * result + if (latitude != null) latitude!!.hashCode() else 0
        return result
    }

    companion object {

        private const val serialVersionUID = -2822993513206651288L
    }
}
