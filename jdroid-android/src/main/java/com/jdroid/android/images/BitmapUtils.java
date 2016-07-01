package com.jdroid.android.images;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.util.Log;
import com.jdroid.android.application.AbstractApplication;

public class BitmapUtils {

	private BitmapUtils(){}
	
	public static Bitmap toBitmap(int resId, Integer maxWidth, Integer maxHeight) {
		Bitmap bitmap = BitmapFactory.decodeResource(AbstractApplication.get().getResources(), resId);
		Bitmap scaled = Bitmap.createScaledBitmap(bitmap, maxWidth, maxHeight, true);
		bitmap.recycle();
		return scaled;
	}
	
	public static Bitmap toBitmap(InputStream input, Integer maxWidth, Integer maxHeight) {
		
		Options options = new Options();
		if ((maxWidth != null) && (maxHeight != null)) {
			// Set the scaling options.
			float scale = Math.min(maxWidth.floatValue() / options.outWidth, maxHeight.floatValue() / options.outHeight);
			options.inSampleSize = Math.round(1 / scale);
		}
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeStream(input, null, options);
	}
	
	/**
	 * Gets a {@link Bitmap} from a {@link Uri}. Resizes the image to a determined width and height.
	 * 
	 * @param uri The {@link Uri} from which the image is obtained.
	 * @param maxWidth The maximum width of the image used to scale it. If null, the image won't be scaled
	 * @param maxHeight The maximum height of the image used to scale it. If null, the image won't be scaled
	 * @return {@link Bitmap} The resized image.
	 */
	public static Bitmap toBitmap(Uri uri, Integer maxWidth, Integer maxHeight) {
		try {
			Context context = AbstractApplication.get();
			
			// First decode with inJustDecodeBounds=true to check dimensions
			Options options = new Options();
			options.inJustDecodeBounds = true;
			InputStream openInputStream = context.getContentResolver().openInputStream(uri);
			BitmapFactory.decodeStream(openInputStream, null, options);
			openInputStream.close();
			
			// Calculate inSampleSize
			if ((maxWidth != null) && (maxHeight != null)) {
				float scale = Math.min(maxWidth.floatValue() / options.outWidth, maxHeight.floatValue()
						/ options.outHeight);
				options.inSampleSize = Math.round(1 / scale);
			}
			
			// Decode bitmap with inSampleSize set
			openInputStream = context.getContentResolver().openInputStream(uri);
			options.inJustDecodeBounds = false;
			Bitmap result = BitmapFactory.decodeStream(openInputStream, null, options);
			openInputStream.close();
			return result;
		} catch (Exception e) {
			Log.e(BitmapUtils.class.getSimpleName(), e.getMessage());
			return null;
		}
	}
	
	public static ByteArrayInputStream toPNGInputStream(Uri uri, Integer maxWidth, Integer maxHeight) {
		Bitmap bitmap = BitmapUtils.toBitmap(uri, maxWidth, maxHeight);
		return BitmapUtils.toPNGInputStream(bitmap);
	}
	
	/**
	 * Compress the bitmap to a PNG and return its {@link ByteArrayInputStream}
	 * 
	 * @param bitmap The {@link Bitmap} to compress
	 * @return The {@link ByteArrayInputStream}
	 */
	public static ByteArrayInputStream toPNGInputStream(Bitmap bitmap) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
		return new ByteArrayInputStream(bytes.toByteArray());
	}
}
