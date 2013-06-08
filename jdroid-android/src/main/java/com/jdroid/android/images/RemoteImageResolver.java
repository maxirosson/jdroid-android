package com.jdroid.android.images;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.slf4j.Logger;
import android.graphics.Bitmap;
import android.net.Uri;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.utils.BitmapUtils;
import com.jdroid.java.http.apache.DefaultHttpClientFactory;
import com.jdroid.java.utils.FileUtils;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.java.utils.ValidationUtils;

/**
 * 
 * @author Maxi Rosson
 */
public class RemoteImageResolver implements ImageResolver {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(RemoteImageResolver.class);
	
	private static final RemoteImageResolver INSTANCE = new RemoteImageResolver();
	
	public static RemoteImageResolver get() {
		return INSTANCE;
	}
	
	/**
	 * @see com.jdroid.android.images.ImageResolver#canResolve(android.net.Uri)
	 */
	@Override
	public Boolean canResolve(Uri uri) {
		String url = uri.toString();
		return ValidationUtils.isValidURL(url);
	}
	
	/**
	 * @see com.jdroid.android.images.ImageResolver#resolve(android.net.Uri, java.lang.Integer, java.lang.Integer)
	 */
	@SuppressWarnings("resource")
	@Override
	public Bitmap resolve(Uri uri, Integer maxWidth, Integer maxHeight) {
		
		String url = uri.toString();
		
		// REVIEW Identify images by hashcode. Find a better way to do this
		File file = new File(AbstractApplication.get().getImagesCacheDirectory(), String.valueOf(url.hashCode()));
		
		// If the file doesn't exists on the SD cache directory, it is retrieved from the web
		if (!file.exists()) {
			
			InputStream is = null;
			OutputStream os = null;
			try {
				// make client for http.
				HttpClient client = DefaultHttpClientFactory.get().createHttpClient();
				
				// make request.
				HttpUriRequest request = new HttpGet(url);
				
				// execute request.
				HttpResponse httpResponse = client.execute(request);
				
				if (httpResponse.getStatusLine().getStatusCode() != 404) {
					// Process response
					is = httpResponse.getEntity().getContent();
					os = new FileOutputStream(file);
					FileUtils.copyStream(is, os);
					LOGGER.debug("Image [" + url + "] downloaded.");
				} else {
					LOGGER.debug("Image [" + url + "] not found.");
					return null;
				}
				
			} catch (Exception ex) {
				LOGGER.error("Error when downloading image [" + url + "]", ex);
				return null;
			} finally {
				FileUtils.safeClose(os);
				FileUtils.safeClose(is);
			}
		}
		return BitmapUtils.toBitmap(Uri.fromFile(file), maxWidth, maxHeight);
	}
	
}
