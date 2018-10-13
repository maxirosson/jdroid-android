package com.jdroid.android.sample.ui.google.maps;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.jdroid.android.activity.FragmentContainerActivity;
import com.jdroid.android.google.maps.AbstractMapFragment;

public class MapActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return MapFragment.class;
	}

	@Override
	protected Bundle getFragmentExtras() {
		Bundle bundle = super.getFragmentExtras();

		LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
		LatLng latLng1 = new LatLng(-34.608861, -58.370833);
		boundsBuilder.include(latLng1);

		LatLng latLng2 = new LatLng(-34.623556, -58.448611);
		boundsBuilder.include(latLng2);

		GoogleMapOptions options = new GoogleMapOptions();
		options.camera(CameraPosition.fromLatLngZoom(boundsBuilder.build().getCenter(), 12));

		AbstractMapFragment.setGoogleMapOptions(bundle, options);

		return bundle;
	}
}
