package com.jdroid.android.sample.ui.google.maps;

import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.model.LatLng;
import com.jdroid.android.google.maps.AbstractStreetViewFragment;

public class StreetViewFragment extends AbstractStreetViewFragment {

	@Override
	public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
		streetViewPanorama.setPosition(new LatLng(-33.87365, 151.20689));
		streetViewPanorama.setStreetNamesEnabled(false);
	}
}
