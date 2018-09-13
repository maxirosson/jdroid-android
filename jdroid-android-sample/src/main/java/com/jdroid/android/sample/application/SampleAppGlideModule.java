package com.jdroid.android.sample.application;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.jdroid.android.glide.AbstractAppGlideModule;

@GlideModule
public class SampleAppGlideModule extends AbstractAppGlideModule {

	@Override
	public void applyOptions(Context context, GlideBuilder builder) {
		super.applyOptions(context, builder);

	}
}
