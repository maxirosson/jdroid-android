package com.jdroid.android.google.maps;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaOptions;
import com.google.android.gms.maps.StreetViewPanoramaView;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.google.GooglePlayServicesUtils;

public class AbstractStreetViewFragment extends AbstractFragment implements OnStreetViewPanoramaReadyCallback {

	private static final String STREETVIEW_BUNDLE_KEY = "StreetViewBundleKey";

	private ViewGroup streetViewContainer;
	private View updateGoogleplayServicesContainer;
	private Boolean googlePlayServicesEnabled = true;

	private StreetViewPanoramaView streetViewPanoramaView;

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.jdroid_streetview_fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		updateGoogleplayServicesContainer = findView(R.id.updateGoogleplayServicesContainer);
		streetViewContainer = findView(R.id.streetViewContainer);

		initStreetViewPanoramaView(savedInstanceState);
	}

	private void initStreetViewPanoramaView(Bundle savedInstanceState) {
		if (GooglePlayServicesUtils.isGooglePlayServicesAvailable(getActivity())) {
			streetViewPanoramaView = new StreetViewPanoramaView(getActivity(), createStreetViewPanoramaOptions());
			streetViewPanoramaView.getStreetViewPanoramaAsync(this);

			// *** IMPORTANT ***
			// StreetViewPanoramaView requires that the Bundle you pass contain _ONLY_
			// StreetViewPanoramaView SDK objects or sub-Bundles.
			Bundle streetViewBundle = null;
			if (savedInstanceState != null) {
				streetViewBundle = savedInstanceState.getBundle(STREETVIEW_BUNDLE_KEY);
			}
			streetViewPanoramaView.onCreate(streetViewBundle);

			streetViewContainer.removeAllViews();
			streetViewContainer.addView(streetViewPanoramaView);
		} else {
			googlePlayServicesEnabled = false;
		}
	}

	protected StreetViewPanoramaOptions createStreetViewPanoramaOptions() {
		return new StreetViewPanoramaOptions();
	}

	@Override
	public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
		// Do nothing
	}

	@Override
	public void onResume() {
		if (streetViewPanoramaView != null) {
			streetViewPanoramaView.onResume();
		}
		super.onResume();

		if (GooglePlayServicesUtils.isGooglePlayServicesAvailable(getActivity())) {
			if (!googlePlayServicesEnabled) {
				initStreetViewPanoramaView(null);
				streetViewPanoramaView.onResume();
				googlePlayServicesEnabled = true;
			}
			displayStreetView();
		} else {
			showUpdateGooglePlayServices();
		}
	}

	private void displayStreetView() {
		updateGoogleplayServicesContainer.setVisibility(View.GONE);
		streetViewContainer.setVisibility(View.VISIBLE);
	}

	private void showUpdateGooglePlayServices() {
		streetViewContainer.setVisibility(View.GONE);
		updateGoogleplayServicesContainer.setVisibility(View.VISIBLE);

		Button button = findView(R.id.button);
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				GooglePlayServicesUtils.launchGooglePlayServicesUpdate();
			}
		});
	}

	@Override
	public void onPause() {
		if (streetViewPanoramaView != null) {
			streetViewPanoramaView.onPause();
		}
		super.onPause();
	}

	@Override
	public void onDestroy() {
		if (streetViewPanoramaView != null) {
			streetViewPanoramaView.onDestroy();
		}
		super.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();

		if (streetViewPanoramaView != null) {
			streetViewPanoramaView.onLowMemory();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		if (streetViewPanoramaView != null) {
			Bundle mStreetViewBundle = outState.getBundle(STREETVIEW_BUNDLE_KEY);
			if (mStreetViewBundle == null) {
				mStreetViewBundle = new Bundle();
				outState.putBundle(STREETVIEW_BUNDLE_KEY, mStreetViewBundle);
			}

			streetViewPanoramaView.onSaveInstanceState(outState);
		}
	}
}
