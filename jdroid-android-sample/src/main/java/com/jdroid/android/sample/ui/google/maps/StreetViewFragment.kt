package com.jdroid.android.sample.ui.google.maps

import com.google.android.gms.maps.StreetViewPanorama
import com.google.android.gms.maps.model.LatLng
import com.jdroid.android.google.maps.AbstractStreetViewFragment

class StreetViewFragment : AbstractStreetViewFragment() {

    override fun onStreetViewPanoramaReady(streetViewPanorama: StreetViewPanorama) {
        streetViewPanorama.setPosition(LatLng(-33.87365, 151.20689))
        streetViewPanorama.isStreetNamesEnabled = false
    }
}
