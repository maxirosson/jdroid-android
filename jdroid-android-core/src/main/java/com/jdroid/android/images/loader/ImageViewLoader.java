package com.jdroid.android.images.loader;

import android.widget.ImageView;

public interface ImageViewLoader {

	public void displayImage(String url, ImageView imageView, Integer defaultImage, Long timeToLive);
}
