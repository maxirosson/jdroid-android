package com.jdroid.android.google.maps;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.jdroid.android.activity.ActivityIf;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.application.AppModule;
import com.jdroid.android.exception.ErrorDisplayer;
import com.jdroid.android.fragment.FragmentDelegate;
import com.jdroid.android.fragment.FragmentHelper;
import com.jdroid.android.fragment.FragmentIf;
import com.jdroid.android.google.GooglePlayServicesUtils;
import com.jdroid.android.loading.FragmentLoading;
import com.jdroid.android.permission.PermissionHelper;
import com.jdroid.android.snackbar.SnackbarBuilder;
import com.jdroid.java.exception.AbstractException;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public abstract class AbstractMapFragment extends SupportMapFragment implements FragmentIf {

	private FragmentHelper fragmentHelper;
	private GoogleMap map;

	private ViewGroup mapContainer;
	private View updateGoogleplayServicesContainer;

	private Boolean snackbarDisplayed = false;
	private PermissionHelper locationPermissionHelper;

	protected FragmentIf getFragmentIf() {
		return (FragmentIf)this.getActivity();
	}

	/**
	 * Calling this before super.oncreate() when you try to use a toolbar and the view contains a map
	 * https://code.google.com/p/android/issues/detail?id=175140
	 */
	public static void doMapToolbarWorkaround(Bundle savedInstanceState) {
		// FIXME This is just a workaround to the following error: ClassNotFoundException when unmarshalling android.support.v7.widget.Toolbar$SavedState
		// It seems to be a problem with the SupportMapFragment implementation
		// https://code.google.com/p/android/issues/detail?id=175140
		if (savedInstanceState != null) {
			SparseArray sparseArray = (SparseArray)savedInstanceState.get("android:view_state");
			if (sparseArray != null) {
				Integer keyToRemove = null;
				for (int i = 0; i < sparseArray.size(); i++) {
					int key = sparseArray.keyAt(i);
					// get the object by the key.
					Object each = sparseArray.get(key);
					if (each.toString().startsWith("android.support.v7.widget.Toolbar$SavedState")) {
						keyToRemove = key;
					}
				}

				if (keyToRemove != null) {
					sparseArray.remove(keyToRemove);
				}
			}
		}
	}

	public static void setGoogleMapOptions(Bundle bundle, GoogleMapOptions googleMapOptions) {
		bundle.putParcelable("MapOptions", googleMapOptions);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		doMapToolbarWorkaround(savedInstanceState);
		super.onCreate(savedInstanceState);
		fragmentHelper = AbstractApplication.get().createFragmentHelper(this);
		fragmentHelper.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			setRetainInstance(true);
		}

		if (isLocationEnabled()) {
			locationPermissionHelper = PermissionHelper.createLocationPermissionHelper(this);
			locationPermissionHelper.setAppInfoDialogMessageResId(R.string.jdroid_locationPermissionRequired);
			locationPermissionHelper.setOnRequestPermissionsResultListener(new PermissionHelper.OnRequestPermissionsResultListener() {

				@RequiresPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
				@Override
				public void onRequestPermissionsGranted() {
					if (map != null) {
						map.setMyLocationEnabled(true);
					}
				}

				@Override
				public void onRequestPermissionsDenied() {
					// Nothing to do
				}
			});
		}

		getMapAsync(new OnMapReadyCallback() {

			@RequiresPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
			@Override
			public void onMapReady(final GoogleMap googleMap) {
				map = googleMap;

				// Setting an info window adapter allows us to change the both the contents and look of the
				// info window.
				InfoWindowAdapter infoWindowAdapter = getInfoWindowAdapter();
				if (infoWindowAdapter != null) {
					map.setInfoWindowAdapter(infoWindowAdapter);
				}
				map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				AbstractMapFragment.this.onMapReady(map);

				getGoogleMap().setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
					@Override
					public void onMapLoaded() {
						if (!isDetached()) {
							AbstractMapFragment.this.onMapLoaded(googleMap);
						}
					}
				});

				if (locationPermissionHelper != null) {
					Boolean locationPermissionGranted = locationPermissionHelper.checkPermission(false);
					if (locationPermissionGranted) {
						map.setMyLocationEnabled(true);
					} else if (snackbarToSuggestLocationPermissionEnabled()) {
						locationPermissionHelper.setOnRequestPermissionsResultListener(new PermissionHelper.OnRequestPermissionsResultListener() {

							@RequiresPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
							@Override
							public void onRequestPermissionsGranted() {
								if (map != null) {
									map.setMyLocationEnabled(true);
								}
							}

							@Override
							public void onRequestPermissionsDenied() {
								if (!snackbarDisplayed) {
									SnackbarBuilder snackbarBuilder = new SnackbarBuilder();
									snackbarBuilder.setActionTextResId(R.string.jdroid_allow);
									snackbarBuilder.setDuration(5000);
									snackbarBuilder.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											locationPermissionHelper.checkPermission(true);
											snackbarDisplayed = true;
										}
									});
									snackbarBuilder.setDescription(R.string.jdroid_locationPermissionSuggested);
									snackbarBuilder.build(getActivity()).show();
								}
							}
						});
					}
				}
			}
		});
	}

	protected Boolean snackbarToSuggestLocationPermissionEnabled() {
		return false;
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		locationPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = fragmentHelper.onCreateView(inflater, container, savedInstanceState);
		mapContainer = (ViewGroup)view.findViewById(R.id.mapContainer);
		View mapView = super.onCreateView(inflater, mapContainer, savedInstanceState);
		mapContainer.addView(mapView);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		fragmentHelper.onViewCreated(view, savedInstanceState);

		updateGoogleplayServicesContainer = findView(R.id.updateGoogleplayServicesContainer);
	}

	protected void onMapReady(@NonNull GoogleMap googleMap) {
		// Do Nothing
	}

	protected void onMapLoaded(@NonNull GoogleMap googleMap) {
		// Do Nothing
	}

	@Override
	public Integer getBaseFragmentLayout() {
		return fragmentHelper.getBaseFragmentLayout();
	}

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.jdroid_map_fragment;
	}

	@Override
	public void onNewIntent(Intent intent) {
		fragmentHelper.onNewIntent(intent);
	}

	@Override
	public void onStart() {
		super.onStart();
		fragmentHelper.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();

		fragmentHelper.onResume();

		if (locationPermissionHelper != null) {
			locationPermissionHelper.onResume();
		}

		if (GooglePlayServicesUtils.isGooglePlayServicesAvailable(getActivity())) {
			displayMap();
		} else {
			showUpdateGooglePlayServices();
		}
	}

	private void displayMap() {
		updateGoogleplayServicesContainer.setVisibility(View.GONE);
		mapContainer.setVisibility(View.VISIBLE);
	}

	private void showUpdateGooglePlayServices() {
		mapContainer.setVisibility(View.GONE);
		updateGoogleplayServicesContainer.setVisibility(View.VISIBLE);

		Button button = findView(R.id.button);
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				GooglePlayServicesUtils.launchGooglePlayServicesUpdate();
			}
		});
	}

	protected InfoWindowAdapter getInfoWindowAdapter() {
		return null;
	}

	protected Boolean isLocationEnabled() {
		return false;
	}

	public GoogleMap getGoogleMap() {
		return map;
	}

	@Override
	public boolean shouldRetainInstance() {
		return fragmentHelper.shouldRetainInstance();
	}

	/**
	 * @see Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		fragmentHelper.onActivityCreated(savedInstanceState);
	}

	/**
	 * @see Fragment#onPause()
	 */
	@Override
	public void onPause() {
		fragmentHelper.onBeforePause();
		super.onPause();
		fragmentHelper.onPause();
	}

	/**
	 * @see Fragment#onStop()
	 */
	@Override
	public void onStop() {
		super.onStop();
		fragmentHelper.onStop();
	}

	/**
	 * @see Fragment#onDestroyView()
	 */
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		fragmentHelper.onDestroyView();
	}

	/**
	 * @see Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		fragmentHelper.onBeforeDestroy();
		super.onDestroy();
		fragmentHelper.onDestroy();
	}

	@Override
	public <V extends View> V findView(int id) {
		return fragmentHelper.findView(id);
	}

	@Override
	public <V extends View> V findViewOnActivity(int id) {
		return fragmentHelper.findViewOnActivity(id);
	}

	@Override
	public <V extends View> V inflate(int resource) {
		return fragmentHelper.inflate(resource);
	}

	@Override
	public ErrorDisplayer createErrorDisplayer(AbstractException abstractException) {
		return fragmentHelper.createErrorDisplayer(abstractException);
	}

	@Override
	public void executeOnUIThread(Runnable runnable) {
		fragmentHelper.executeOnUIThread(runnable);
	}

	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getExtra(java.lang.String)
	 */
	@Override
	public <E> E getExtra(String key) {
		return fragmentHelper.<E>getExtra(key);
	}

	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getArgument(java.lang.String)
	 */
	@Override
	public <E> E getArgument(String key) {
		return fragmentHelper.<E>getArgument(key);
	}

	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getArgument(java.lang.String, java.lang.Object)
	 */
	@Override
	public <E> E getArgument(String key, E defaultValue) {
		return fragmentHelper.<E>getArgument(key, defaultValue);
	}

	@Override
	public void beforeInitAppBar(Toolbar appBar) {
		fragmentHelper.beforeInitAppBar(appBar);
	}

	@Override
	public void afterInitAppBar(Toolbar appBar) {
		fragmentHelper.afterInitAppBar(appBar);
	}

	@Override
	public Toolbar getAppBar() {
		return fragmentHelper.getAppBar();
	}

	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getActivityIf()
	 */
	@Override
	public ActivityIf getActivityIf() {
		return fragmentHelper.getActivityIf();
	}

	// //////////////////////// Analytics //////////////////////// //

	@NonNull
	@Override
	public String getScreenViewName() {
		return fragmentHelper.getScreenViewName();
	}

	// //////////////////////// Loading //////////////////////// //

	/**
	 * @see com.jdroid.android.fragment.FragmentIf#showLoading()
	 */
	@Override
	public void showLoading() {
		fragmentHelper.showLoading();
	}

	/**
	 * @see com.jdroid.android.fragment.FragmentIf#dismissLoading()
	 */
	@Override
	public void dismissLoading() {
		fragmentHelper.dismissLoading();
	}

	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getDefaultLoading()
	 */
	@Override
	public FragmentLoading getDefaultLoading() {
		return fragmentHelper.getDefaultLoading();
	}

	/**
	 * @see com.jdroid.android.fragment.FragmentIf#setLoading(com.jdroid.android.loading.FragmentLoading)
	 */
	@Override
	public void setLoading(FragmentLoading loading) {
		fragmentHelper.setLoading(loading);
	}

	@Override
	public Integer getMenuResourceId() {
		return fragmentHelper.getMenuResourceId();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return fragmentHelper.onOptionsItemSelected(item);
	}

	@Override
	public boolean isSecondaryFragment() {
		return fragmentHelper.isSecondaryFragment();
	}

	@Override
	public FragmentDelegate createFragmentDelegate(AppModule appModule) {
		return fragmentHelper.createFragmentDelegate(appModule);
	}

	@Override
	public FragmentDelegate getFragmentDelegate(AppModule appModule) {
		return fragmentHelper.getFragmentDelegate(appModule);
	}

	@Override
	public boolean onBackPressedHandled() {
		return fragmentHelper.onBackPressedHandled();
	}
}
