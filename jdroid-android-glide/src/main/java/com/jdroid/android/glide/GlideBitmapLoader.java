package com.jdroid.android.glide;

import android.graphics.Bitmap;

import com.bumptech.glide.request.RequestOptions;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.images.loader.BitmapLoader;

import java.util.concurrent.ExecutionException;

public class GlideBitmapLoader implements BitmapLoader {

	private String url;

	public GlideBitmapLoader(String url) {
		this.url = url;
	}

	@Override
	public Bitmap load(int height, int width) {
		RequestOptions options = new RequestOptions();
		options = options.override(width, height);
		try {
			return GlideHelper.with(AbstractApplication.get()).asBitmap().load(url).apply(options).submit().get();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		}
	}
}
