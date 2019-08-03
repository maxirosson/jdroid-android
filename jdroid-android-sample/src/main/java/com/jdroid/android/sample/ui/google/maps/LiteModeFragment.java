package com.jdroid.android.sample.ui.google.maps;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.google.GooglePlayServicesUtils;
import com.jdroid.android.google.maps.AbstractMapFragment;
import com.jdroid.android.sample.R;

public class LiteModeFragment extends AbstractFragment {

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.lite_mode_map_fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		AbstractMapFragment.Companion.doMapToolbarWorkaround(savedInstanceState);

		super.onViewCreated(view, savedInstanceState);

		if (GooglePlayServicesUtils.INSTANCE.isGooglePlayServicesAvailable(getActivity())) {
			LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
			LatLng latLng1 = new LatLng(-34.608861, -58.370833);
			boundsBuilder.include(latLng1);

			LatLng latLng2 = new LatLng(-34.623556, -58.448611);
			boundsBuilder.include(latLng2);

			GoogleMapOptions options = new GoogleMapOptions();
			options.camera(CameraPosition.fromLatLngZoom(boundsBuilder.build().getCenter(), 12));
			options.mapType(GoogleMap.MAP_TYPE_NORMAL);
			options.liteMode(true);

			ViewGroup mapContainer = findView(R.id.mapContainer);

			MapView mapView = new MapView(getActivity(), options);
			mapContainer.addView(mapView);

			mapView.onCreate(savedInstanceState);
			mapView.getMapAsync(new OnMapReadyCallback() {
				@Override
				public void onMapReady(final GoogleMap googleMap) {
					googleMap.getUiSettings().setMapToolbarEnabled(false);
					googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
						@Override
						public void onMapLoaded() {
							LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
							LatLng latLng1 = new LatLng(-34.608861, -58.370833);
							boundsBuilder.include(latLng1);
							MarkerOptions options1 = new MarkerOptions();
							options1.position(latLng1);
							options1.title("Title 1");
							options1.snippet("Snippet 1");
							options1.anchor(0.5f, 0.5f);
							options1.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
							googleMap.addMarker(options1);
						}
					});
				}
			});
		}
	}
}
