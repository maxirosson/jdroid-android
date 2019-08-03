package com.jdroid.android.sample.ui.google.maps

import android.os.Bundle
import androidx.fragment.app.Fragment

import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.jdroid.android.activity.FragmentContainerActivity
import com.jdroid.android.google.maps.AbstractMapFragment

class MapActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return MapFragment::class.java
    }

    override fun getFragmentExtras(): Bundle {
        val bundle = super.getFragmentExtras()

        val boundsBuilder = LatLngBounds.Builder()
        val latLng1 = LatLng(-34.608861, -58.370833)
        boundsBuilder.include(latLng1)

        val latLng2 = LatLng(-34.623556, -58.448611)
        boundsBuilder.include(latLng2)

        val options = GoogleMapOptions()
        options.camera(CameraPosition.fromLatLngZoom(boundsBuilder.build().center, 12f))

        AbstractMapFragment.setGoogleMapOptions(bundle, options)

        return bundle
    }
}
