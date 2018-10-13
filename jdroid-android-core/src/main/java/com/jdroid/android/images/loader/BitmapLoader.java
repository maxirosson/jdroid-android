package com.jdroid.android.images.loader;

import android.graphics.Bitmap;
import androidx.annotation.WorkerThread;

public interface BitmapLoader {

	@WorkerThread
	public Bitmap load(int height, int width);
}
