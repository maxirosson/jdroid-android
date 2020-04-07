package com.jdroid.android.google.maps

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback
import com.google.android.gms.maps.StreetViewPanorama
import com.google.android.gms.maps.StreetViewPanoramaOptions
import com.google.android.gms.maps.StreetViewPanoramaView
import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.google.GooglePlayServicesUtils

open class AbstractStreetViewFragment : AbstractFragment(), OnStreetViewPanoramaReadyCallback {

    private lateinit var streetViewContainer: ViewGroup
    private lateinit var updateGoogleplayServicesContainer: View
    private var googlePlayServicesEnabled: Boolean = true

    private var streetViewPanoramaView: StreetViewPanoramaView? = null

    override fun getContentFragmentLayout(): Int? {
        return R.layout.jdroid_streetview_fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateGoogleplayServicesContainer = findView(R.id.updateGoogleplayServicesContainer)
        streetViewContainer = findView(R.id.streetViewContainer)

        initStreetViewPanoramaView(savedInstanceState)
    }

    private fun initStreetViewPanoramaView(savedInstanceState: Bundle?) {
        if (GooglePlayServicesUtils.isGooglePlayServicesAvailable(requireActivity())) {
            streetViewPanoramaView = StreetViewPanoramaView(activity, createStreetViewPanoramaOptions())
            streetViewPanoramaView!!.getStreetViewPanoramaAsync(this)

            // *** IMPORTANT ***
            // StreetViewPanoramaView requires that the Bundle you pass contain _ONLY_
            // StreetViewPanoramaView SDK objects or sub-Bundles.
            var streetViewBundle: Bundle? = null
            if (savedInstanceState != null) {
                streetViewBundle = savedInstanceState.getBundle(STREETVIEW_BUNDLE_KEY)
            }
            streetViewPanoramaView!!.onCreate(streetViewBundle)

            streetViewContainer.removeAllViews()
            streetViewContainer.addView(streetViewPanoramaView)
        } else {
            googlePlayServicesEnabled = false
        }
    }

    protected open fun createStreetViewPanoramaOptions(): StreetViewPanoramaOptions {
        return StreetViewPanoramaOptions()
    }

    override fun onStreetViewPanoramaReady(streetViewPanorama: StreetViewPanorama) {
        // Do nothing
    }

    override fun onResume() {
        streetViewPanoramaView?.onResume()
        super.onResume()

        if (GooglePlayServicesUtils.isGooglePlayServicesAvailable(requireActivity())) {
            if (!googlePlayServicesEnabled) {
                initStreetViewPanoramaView(null)
                streetViewPanoramaView!!.onResume()
                googlePlayServicesEnabled = true
            }
            displayStreetView()
        } else {
            showUpdateGooglePlayServices()
        }
    }

    private fun displayStreetView() {
        updateGoogleplayServicesContainer.visibility = View.GONE
        streetViewContainer.visibility = View.VISIBLE
    }

    private fun showUpdateGooglePlayServices() {
        streetViewContainer.visibility = View.GONE
        updateGoogleplayServicesContainer.visibility = View.VISIBLE

        val button = findView<Button>(R.id.button)
        button.setOnClickListener { GooglePlayServicesUtils.launchGooglePlayServicesUpdate() }
    }

    override fun onPause() {
        streetViewPanoramaView?.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        streetViewPanoramaView?.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        streetViewPanoramaView?.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if (streetViewPanoramaView != null) {
            var mStreetViewBundle = outState.getBundle(STREETVIEW_BUNDLE_KEY)
            if (mStreetViewBundle == null) {
                mStreetViewBundle = Bundle()
                outState.putBundle(STREETVIEW_BUNDLE_KEY, mStreetViewBundle)
            }

            streetViewPanoramaView?.onSaveInstanceState(outState)
        }
    }

    companion object {
        private const val STREETVIEW_BUNDLE_KEY = "StreetViewBundleKey"
    }
}
