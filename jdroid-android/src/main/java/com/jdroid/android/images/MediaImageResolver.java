package com.jdroid.android.images;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import com.jdroid.android.utils.BitmapUtils;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaImageResolver implements ImageResolver {
	
	private static final MediaImageResolver INSTANCE = new MediaImageResolver();
	
	public static MediaImageResolver get() {
		return INSTANCE;
	}
	
	/**
	 * @see com.jdroid.android.images.ImageResolver#canResolve(android.net.Uri)
	 */
	@Override
	public Boolean canResolve(Uri uri) {
		return uri.toString().startsWith(MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString())
				|| uri.toString().startsWith(MediaStore.Images.Media.INTERNAL_CONTENT_URI.toString())
				|| uri.toString().startsWith("content://com.google.android.gallery3d.provider/picasa");
	}
	
	/**
	 * @see com.jdroid.android.images.ImageResolver#resolve(android.net.Uri, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Bitmap resolve(Uri uri, Integer maxWidth, Integer maxHeight) {
		return BitmapUtils.toBitmap(uri, maxWidth, maxHeight);
	}
	
}
