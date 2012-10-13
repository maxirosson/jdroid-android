package com.jdroid.android.images;

import java.lang.ref.SoftReference;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.contacts.ContactImageResolver;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.collections.Maps;

public class ImageLoader {
	
	private static final String TAG = ImageLoader.class.getSimpleName();
	private static final ImageLoader INSTANCE = new ImageLoader();
	
	// Retry Interval in milliseconds
	private static final int RETRY_INTERVAL = 100000;
	
	private Map<String, Long> failedBitmapCache = Maps.newHashMap();
	private ImagesQueue imagesQueue = new ImagesQueue();
	private List<ImageLoaderThread> loaders = Lists.newArrayList();
	private List<ImageResolver> imageResolvers = Lists.newArrayList();
	
	public static ImageLoader get() {
		return INSTANCE;
	}
	
	private ImageLoader() {
		imageResolvers.add(ReflectedRemoteImageResolver.get());
		imageResolvers.add(FileSystemImageResolver.get());
		imageResolvers.add(RemoteImageResolver.get());
		imageResolvers.add(MediaImageResolver.get());
		imageResolvers.add(ContactImageResolver.get());
		
		ImageLoaderThread imageLoaderThread = new ImageLoaderThread();
		imageLoaderThread.start();
		loaders.add(imageLoaderThread);
	}
	
	public void displayImage(Uri uri, ImageHolder imageHolder) {
		String uriString = uri.toString();
		Bitmap bitmap = AbstractApplication.get().getBitmapLruCache().get(uriString);
		if (bitmap != null) {
			imageHolder.setImageBitmap(bitmap);
		} else {
			// If the image failed to be downloaded previously, try again after the RETRY_INTERVAL
			if (failedBitmapCache.containsKey(uriString)) {
				if ((System.currentTimeMillis() - failedBitmapCache.get(uriString)) > RETRY_INTERVAL) {
					failedBitmapCache.remove(uriString);
					queueImage(uri, imageHolder);
				}
			} else {
				queueImage(uri, imageHolder);
			}
			imageHolder.showStubImage();
		}
	}
	
	private void queueImage(Uri uri, ImageHolder imageHolder) {
		// This ImageHolder may be used for other images before. So there may be
		// some old tasks in the queue. We need to discard them.
		imagesQueue.clean(imageHolder);
		
		ImageResolver imageResolver = null;
		for (ImageResolver each : imageResolvers) {
			if (each.canResolve(uri)) {
				imageResolver = each;
				break;
			}
		}
		
		if (imageResolver != null) {
			ImageLoaderTask imageLoaderTask = new ImageLoaderTask(uri, imageHolder, imageResolver);
			synchronized (imagesQueue) {
				imagesQueue.push(imageLoaderTask);
				imagesQueue.notifyAll();
			}
		} else {
			Log.e(TAG, "The Image loader can't not resolve the uri: " + uri.toString());
		}
	}
	
	/**
	 * Task for the queue.
	 * 
	 */
	private class ImageLoaderTask implements Runnable {
		
		private Uri uri;
		private SoftReference<ImageHolder> imageHolderReference;
		private Bitmap bitmap;
		private ImageResolver imageResolver;
		
		public ImageLoaderTask(Uri uri, ImageHolder imageHolder, ImageResolver imageResolver) {
			this.uri = uri;
			imageHolderReference = new SoftReference<ImageHolder>(imageHolder);
			this.imageResolver = imageResolver;
		}
		
		private ImageHolder getImageHolder() {
			return imageHolderReference.get();
		}
		
		/**
		 * Used to display bitmap in the UI thread.
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			ImageHolder imageHolder = getImageHolder();
			if ((imageHolder != null) && imageHolder.getTag().equals(uri)) {
				if (bitmap != null) {
					imageHolder.setImageBitmap(bitmap);
				} else {
					imageHolder.showStubImage();
				}
			}
		}
		
		public String getUriString() {
			return uri.toString();
		}
		
		public Bitmap loadBitmap() {
			ImageHolder imageHolder = getImageHolder();
			if (imageHolder != null) {
				bitmap = imageResolver.resolve(uri, imageHolder.getMaximumWidth(), imageHolder.getMaximumHeight());
				Activity activity = (Activity)imageHolder.getContext();
				activity.runOnUiThread(this);
			}
			return bitmap;
		}
		
		public boolean equals(ImageHolder otherImageHolder) {
			ImageHolder imageHolder = getImageHolder();
			return (imageHolder == null) || (imageHolder == otherImageHolder);
		}
	}
	
	/**
	 * Stores list of images to download.
	 * 
	 */
	private class ImagesQueue extends Stack<ImageLoaderTask> {
		
		/**
		 * Removes all instances of this ImageHolder.
		 * 
		 * @param imageholder
		 */
		public void clean(ImageHolder imageholder) {
			for (int i = 0; i < size();) {
				if (get(i).equals(imageholder)) {
					remove(i);
				} else {
					i++;
				}
			}
		}
	}
	
	private class ImageLoaderThread extends Thread {
		
		public ImageLoaderThread() {
			super(ImageLoaderThread.class.getSimpleName());
			// Make the background thread low priority. This way it will not affect the UI performance.
			setPriority(Thread.MIN_PRIORITY);
		}
		
		/**
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			try {
				while (true) {
					// thread waits until there are any images to load in the queue.
					if (imagesQueue.isEmpty()) {
						synchronized (imagesQueue) {
							imagesQueue.wait();
						}
					}
					
					ImageLoaderTask imageToLoadTask = null;
					synchronized (imagesQueue) {
						if (!imagesQueue.isEmpty()) {
							imageToLoadTask = imagesQueue.pop();
						}
					}
					if (imageToLoadTask != null) {
						Bitmap bmp = imageToLoadTask.loadBitmap();
						if (bmp != null) {
							AbstractApplication.get().getBitmapLruCache().put(imageToLoadTask.getUriString(), bmp);
						} else {
							// Save the the image URL to retry the download in the future
							failedBitmapCache.put(imageToLoadTask.getUriString(), System.currentTimeMillis());
						}
					}
				}
			} catch (InterruptedException e) {
				Log.e(TAG, "", e);
			}
		}
	}
}
