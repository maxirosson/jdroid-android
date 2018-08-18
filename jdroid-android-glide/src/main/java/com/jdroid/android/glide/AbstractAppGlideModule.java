package com.jdroid.android.glide;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.executor.GlideExecutor;
import com.bumptech.glide.module.AppGlideModule;

public class AbstractAppGlideModule extends AppGlideModule {

	@Override
	public void applyOptions(Context context, GlideBuilder builder) {
		super.applyOptions(context, builder);

		GlideExecutor.UncaughtThrowableStrategy uncaughtThrowableStrategy = new LoggingUncaughtThrowableStrategy();
		builder.setDiskCacheExecutor(GlideExecutor.newDiskCacheExecutor(uncaughtThrowableStrategy));
		builder.setSourceExecutor(GlideExecutor.newSourceExecutor(uncaughtThrowableStrategy));
		//builder.setLogLevel(Log.INFO);
	}

	@Override
	public boolean isManifestParsingEnabled() {
		return false;
	}
}
