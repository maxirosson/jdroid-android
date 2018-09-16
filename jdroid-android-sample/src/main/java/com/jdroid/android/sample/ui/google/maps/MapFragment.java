package com.jdroid.android.sample.ui.google.maps;

import android.support.annotation.NonNull;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.jdroid.android.google.maps.AbstractMapFragment;
import com.jdroid.android.sample.R;

public class MapFragment extends AbstractMapFragment {

	@Override
	protected InfoWindowAdapter getInfoWindowAdapter() {
		return new InfoWindowAdapter() {

			@Override
			public View getInfoWindow(Marker marker) {
				return null;
			}

			@Override
			public View getInfoContents(Marker marker) {
				return null;
			}
		};
	}

	@Override
	protected void onMapLoaded(@NonNull GoogleMap googleMap) {
		LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
		PolylineOptions linePolylineOptions = new PolylineOptions();

		LatLng latLng1 = new LatLng(-34.608861, -58.370833);
		boundsBuilder.include(latLng1);
		linePolylineOptions.add(latLng1);
		MarkerOptions options1 = new MarkerOptions();
		options1.position(latLng1);
		options1.title("Title 1");
		options1.snippet("Snippet 1");
		options1.anchor(0.5f, 0.5f);
		options1.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
		googleMap.addMarker(options1);

		LatLng latLng2 = new LatLng(-34.623556, -58.448611);
		boundsBuilder.include(latLng2);
		linePolylineOptions.add(latLng2);
		MarkerOptions options2 = new MarkerOptions();
		options2.position(latLng2);
		options2.title("Title 2");
		options2.snippet("Snippet 2");
		options2.anchor(0.5f, 0.5f);
		options2.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
		googleMap.addMarker(options2);

		googleMap.addPolyline(linePolylineOptions);

		googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 50));
	}

	@Override
	protected Boolean isLocationEnabled() {
		return true;
	}
}
