package com.jdroid.android.google.maps

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresPermission
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.jdroid.android.activity.ActivityIf
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.application.AppModule
import com.jdroid.android.exception.ErrorDisplayer
import com.jdroid.android.fragment.FragmentDelegate
import com.jdroid.android.fragment.FragmentHelper
import com.jdroid.android.fragment.FragmentIf
import com.jdroid.android.google.GooglePlayServicesUtils
import com.jdroid.android.loading.FragmentLoading
import com.jdroid.android.permission.PermissionHelper
import com.jdroid.android.snackbar.SnackbarBuilder
import com.jdroid.java.exception.AbstractException

abstract class AbstractMapFragment : SupportMapFragment(), FragmentIf {

    private lateinit var fragmentHelper: FragmentHelper
    var map: GoogleMap? = null
        private set

    private lateinit var mapContainer: ViewGroup
    private lateinit var updateGoogleplayServicesContainer: View

    private var snackbarDisplayed: Boolean = false
    private var locationPermissionHelper: PermissionHelper? = null

    protected fun getFragmentIf(): FragmentIf {
        return this.activity as FragmentIf
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        doMapToolbarWorkaround(savedInstanceState)
        super.onCreate(savedInstanceState)
        fragmentHelper = AbstractApplication.get().createFragmentHelper(this)
        fragmentHelper.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            retainInstance = true
        }

        if (isLocationEnabled()) {
            locationPermissionHelper = PermissionHelper.createLocationPermissionHelper(this)
            locationPermissionHelper!!.setAppInfoDialogMessageResId(R.string.jdroid_locationPermissionRequired)
            locationPermissionHelper!!.setOnRequestPermissionsResultListener(object :
                PermissionHelper.OnRequestPermissionsResultListener {

                @RequiresPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                override fun onRequestPermissionsGranted() {
                    map?.isMyLocationEnabled = true
                }

                override fun onRequestPermissionsDenied() {
                    // Nothing to do
                }
            })
        }

        getMapAsync(object : OnMapReadyCallback {

            @RequiresPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
            override fun onMapReady(googleMap: GoogleMap) {
                map = googleMap

                // Setting an info window adapter allows us to change the both the contents and look of the
                // info window.
                val infoWindowAdapter = getInfoWindowAdapter()
                if (infoWindowAdapter != null) {
                    map!!.setInfoWindowAdapter(infoWindowAdapter)
                }
                map!!.setMapType(GoogleMap.MAP_TYPE_NORMAL)
                this@AbstractMapFragment.onMapReady(map!!)

                googleMap.setOnMapLoadedCallback {
                    if (!isDetached) {
                        this@AbstractMapFragment.onMapLoaded(googleMap)
                    }
                }

                if (locationPermissionHelper != null) {
                    val locationPermissionGranted = locationPermissionHelper!!.checkPermission(false)
                    if (locationPermissionGranted) {
                        map?.isMyLocationEnabled = true
                    } else if (snackbarToSuggestLocationPermissionEnabled()!!) {
                        locationPermissionHelper!!.setOnRequestPermissionsResultListener(object :
                            PermissionHelper.OnRequestPermissionsResultListener {

                            @RequiresPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                            override fun onRequestPermissionsGranted() {
                                map?.isMyLocationEnabled = true
                            }

                            override fun onRequestPermissionsDenied() {
                                if (!snackbarDisplayed) {
                                    val snackbarBuilder = SnackbarBuilder()
                                    snackbarBuilder.setActionTextResId(R.string.jdroid_allow)
                                    snackbarBuilder.setDuration(5000)
                                    snackbarBuilder.setOnClickListener(View.OnClickListener {
                                        locationPermissionHelper?.checkPermission(true)
                                        snackbarDisplayed = true
                                    })
                                    snackbarBuilder.setDescription(R.string.jdroid_locationPermissionSuggested)
                                    snackbarBuilder.build(activity!!).show()
                                }
                            }
                        })
                    }
                }
            }
        })
    }

    protected fun snackbarToSuggestLocationPermissionEnabled(): Boolean {
        return false
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        locationPermissionHelper?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = fragmentHelper.onCreateView(inflater, container, savedInstanceState)
        mapContainer = view.findViewById<View>(R.id.mapContainer) as ViewGroup
        val mapView = super.onCreateView(inflater, mapContainer, savedInstanceState)
        mapContainer.addView(mapView)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentHelper.onViewCreated(view, savedInstanceState)

        updateGoogleplayServicesContainer = findView(R.id.updateGoogleplayServicesContainer)
    }

    protected fun onMapReady(googleMap: GoogleMap) {
        // Do Nothing
    }

    protected open fun onMapLoaded(googleMap: GoogleMap) {
        // Do Nothing
    }

    override fun getBaseFragmentLayout(): Int? {
        return fragmentHelper.getBaseFragmentLayout()
    }

    override fun getContentFragmentLayout(): Int? {
        return R.layout.jdroid_map_fragment
    }

    override fun onNewIntent(intent: Intent) {
        fragmentHelper.onNewIntent(intent)
    }

    override fun onStart() {
        super.onStart()
        fragmentHelper.onStart()
    }

    override fun onResume() {
        super.onResume()

        fragmentHelper.onResume()
        locationPermissionHelper?.onResume()

        if (GooglePlayServicesUtils.isGooglePlayServicesAvailable(requireActivity())) {
            displayMap()
        } else {
            showUpdateGooglePlayServices()
        }
    }

    private fun displayMap() {
        updateGoogleplayServicesContainer.visibility = View.GONE
        mapContainer.visibility = View.VISIBLE
    }

    private fun showUpdateGooglePlayServices() {
        mapContainer.visibility = View.GONE
        updateGoogleplayServicesContainer.visibility = View.VISIBLE

        val button = findView<Button>(R.id.button)
        button.setOnClickListener { GooglePlayServicesUtils.launchGooglePlayServicesUpdate() }
    }

    override fun shouldRetainInstance(): Boolean {
        return fragmentHelper.shouldRetainInstance()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fragmentHelper.onActivityCreated(savedInstanceState)
    }

    override fun onPause() {
        fragmentHelper.onBeforePause()
        super.onPause()
        fragmentHelper.onPause()
    }

    override fun onStop() {
        super.onStop()
        fragmentHelper.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentHelper.onDestroyView()
    }

    override fun onDestroy() {
        fragmentHelper.onBeforeDestroy()
        super.onDestroy()
        fragmentHelper.onDestroy()
    }

    override fun <V : View> findView(id: Int): V {
        return fragmentHelper.findView(id)
    }

    override fun <V : View> findViewOnActivity(id: Int): V? {
        return fragmentHelper.findViewOnActivity(id)
    }

    override fun <V : View> inflate(resource: Int): V {
        return fragmentHelper.inflate(resource)
    }

    override fun createErrorDisplayer(abstractException: AbstractException): ErrorDisplayer {
        return fragmentHelper.createErrorDisplayer(abstractException)
    }

    override fun executeOnUIThread(runnable: Runnable) {
        fragmentHelper.executeOnUIThread(runnable)
    }

    override fun <E> getExtra(key: String): E {
        return fragmentHelper.getExtra(key)
    }

    override fun <E> getArgument(key: String): E? {
        return fragmentHelper.getArgument(key)
    }

    override fun <E> getArgument(key: String, defaultValue: E?): E? {
        return fragmentHelper.getArgument(key, defaultValue)
    }

    override fun beforeInitAppBar(appBar: Toolbar) {
        fragmentHelper.beforeInitAppBar(appBar)
    }

    override fun afterInitAppBar(appBar: Toolbar) {
        fragmentHelper.afterInitAppBar(appBar)
    }

    override fun getAppBar(): Toolbar? {
        return fragmentHelper.getAppBar()
    }

    override fun getActivityIf(): ActivityIf? {
        return fragmentHelper.getActivityIf()
    }

    // //////////////////////// Analytics //////////////////////// //

    override fun getScreenViewName(): String {
        return fragmentHelper.getScreenViewName()
    }

    // //////////////////////// Loading //////////////////////// //

    override fun showLoading() {
        fragmentHelper.showLoading()
    }

    override fun dismissLoading() {
        fragmentHelper.dismissLoading()
    }

    override fun getDefaultLoading(): FragmentLoading? {
        return fragmentHelper.getDefaultLoading()
    }

    override fun setLoading(loading: FragmentLoading) {
        fragmentHelper.setLoading(loading)
    }

    override fun getMenuResourceId(): Int? {
        return fragmentHelper.getMenuResourceId()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return fragmentHelper.onOptionsItemSelected(item)
    }

    override fun isSecondaryFragment(): Boolean {
        return fragmentHelper.isSecondaryFragment()
    }

    override fun createFragmentDelegate(appModule: AppModule): FragmentDelegate? {
        return fragmentHelper.createFragmentDelegate(appModule)
    }

    override fun getFragmentDelegate(appModule: AppModule): FragmentDelegate? {
        return fragmentHelper.getFragmentDelegate(appModule)
    }

    override fun onBackPressedHandled(): Boolean {
        return fragmentHelper.onBackPressedHandled()
    }

    protected open fun getInfoWindowAdapter(): InfoWindowAdapter? {
        return null
    }

    protected open fun isLocationEnabled(): Boolean {
        return false
    }

    companion object {

        /**
         * Calling this before super.oncreate() when you try to use a toolbar and the view contains a map
         * https://code.google.com/p/android/issues/detail?id=175140
         */
        fun doMapToolbarWorkaround(savedInstanceState: Bundle?) {
            // FIXME This is just a workaround to the following error: ClassNotFoundException when unmarshalling android.support.v7.widget.Toolbar$SavedState
            // It seems to be a problem with the SupportMapFragment implementation
            // https://code.google.com/p/android/issues/detail?id=175140
            if (savedInstanceState != null) {
                val sparseArray = savedInstanceState.get("android:view_state") as SparseArray<*>?
                if (sparseArray != null) {
                    var keyToRemove: Int? = null
                    for (i in 0 until sparseArray.size()) {
                        val key = sparseArray.keyAt(i)
                        // get the object by the key.
                        val each = sparseArray.get(key)
                        if (each.toString().startsWith("android.support.v7.widget.Toolbar\$SavedState")) {
                            keyToRemove = key
                        }
                    }

                    if (keyToRemove != null) {
                        sparseArray.remove(keyToRemove)
                    }
                }
            }
        }

        fun setGoogleMapOptions(bundle: Bundle, googleMapOptions: GoogleMapOptions) {
            bundle.putParcelable("MapOptions", googleMapOptions)
        }
    }
}
