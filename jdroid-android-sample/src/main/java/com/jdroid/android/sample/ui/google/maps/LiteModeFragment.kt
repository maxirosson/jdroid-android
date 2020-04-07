package com.jdroid.android.sample.ui.google.maps

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.google.GooglePlayServicesUtils
import com.jdroid.android.google.maps.AbstractMapFragment
import com.jdroid.android.sample.R

class LiteModeFragment : AbstractFragment() {

    override fun getContentFragmentLayout(): Int? {
        return R.layout.lite_mode_map_fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        AbstractMapFragment.doMapToolbarWorkaround(savedInstanceState)

        super.onViewCreated(view, savedInstanceState)

        if (GooglePlayServicesUtils.isGooglePlayServicesAvailable(requireActivity())) {
            val boundsBuilder = LatLngBounds.Builder()
            val latLng1 = LatLng(-34.608861, -58.370833)
            boundsBuilder.include(latLng1)

            val latLng2 = LatLng(-34.623556, -58.448611)
            boundsBuilder.include(latLng2)

            val options = GoogleMapOptions()
            options.camera(CameraPosition.fromLatLngZoom(boundsBuilder.build().center, 12f))
            options.mapType(GoogleMap.MAP_TYPE_NORMAL)
            options.liteMode(true)

            val mapContainer = findView<ViewGroup>(R.id.mapContainer)

            val mapView = MapView(activity, options)
            mapContainer.addView(mapView)

            mapView.onCreate(savedInstanceState)
            mapView.getMapAsync { googleMap ->
                googleMap.uiSettings.isMapToolbarEnabled = false
                googleMap.setOnMapLoadedCallback {
                    val builder = LatLngBounds.Builder()
                    val latLng3 = LatLng(-34.608861, -58.370833)
                    builder.include(latLng1)
                    val options1 = MarkerOptions()
                    options1.position(latLng3)
                    options1.title("Title 1")
                    options1.snippet("Snippet 1")
                    options1.anchor(0.5f, 0.5f)
                    options1.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                    googleMap.addMarker(options1)
                }
            }
        }
    }
}
