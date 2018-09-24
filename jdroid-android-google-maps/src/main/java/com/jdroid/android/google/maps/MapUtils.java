package com.jdroid.android.google.maps;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.domain.GeoLocation;

public class MapUtils {

	public static BitmapDescriptor safeVectorToBitmap(@DrawableRes int drawableRes, @ColorRes int colorRes) {
		try {
			return vectorToBitmap(drawableRes, colorRes);
		} catch (Exception e) {
			AbstractApplication.get().getExceptionHandler().logHandledException(e);
		}
		return null;
	}

	/**
	 * Convert a {@link Drawable} to a {@link BitmapDescriptor}, for use as a marker icon.
	 */
	public static BitmapDescriptor vectorToBitmap(@DrawableRes int drawableRes, @ColorRes int colorRes) {
		Drawable vectorDrawable = VectorDrawableCompat.create(AbstractApplication.get().getResources(), drawableRes, null);
		Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		DrawableCompat.setTint(vectorDrawable, AbstractApplication.get().getResources().getColor(colorRes));
		vectorDrawable.draw(canvas);
		return BitmapDescriptorFactory.fromBitmap(bitmap);
	}

	public static LatLng createLatLng(GeoLocation geoLocation) {
		return geoLocation != null ? new LatLng(geoLocation.getLatitude(), geoLocation.getLongitude()) : null;
	}
}
