package com.jdroid.android.sample.ui.glide;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.glide.GlideHelper;
import com.jdroid.android.glide.LoggingRequestListener;
import com.jdroid.android.sample.R;

public class GlideFragment extends AbstractFragment {

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.glide_fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		ImageView imageView = findView(R.id.image);

		findView(R.id.withActivity).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				GlideHelper.with(getActivity()).load("https://jdroidtools.com/images/mainImage.png").listener(new LoggingRequestListener<>()).into(imageView);
			}
		});

		findView(R.id.withFragment).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				RequestOptions options = new RequestOptions();
				options = options.placeholder(new ColorDrawable(Color.BLACK));
				GlideHelper.with(GlideFragment.this).load("https://jdroidtools.com/images/android.png").listener(new LoggingRequestListener<>()).apply(options).into(imageView);
			}
		});

		findView(R.id.withApplicationContext).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				RequestOptions options = new RequestOptions();
				options = options.placeholder(R.drawable.jdroid_ic_about_black_24dp);
				GlideHelper.with(AbstractApplication.get()).load("https://jdroidtools.com/images/gradle.png").listener(new LoggingRequestListener<>()).apply(options).into(imageView);
			}
		});

		findView(R.id.withNullContext).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				RequestOptions options = new RequestOptions();
				options = options.placeholder(R.drawable.jdroid_ic_about_black_24dp);
				GlideHelper.with((Context)null).load("https://jdroidtools.com/images/gradle.png").listener(new LoggingRequestListener<>()).apply(options).into(imageView);
			}
		});

		findView(R.id.invalidResourceUrl).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				RequestOptions options = new RequestOptions();
				options = options.placeholder(R.drawable.jdroid_ic_about_black_24dp);
				GlideHelper.with(AbstractApplication.get()).load("https://jdroidtools.com/images/invalid.png").listener(new LoggingRequestListener<>()).apply(options).into(imageView);
			}
		});

		findView(R.id.outOfMemory).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				RequestOptions options = new RequestOptions();
				options = options.placeholder(R.drawable.jdroid_ic_about_black_24dp);
				options = options.override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
				GlideHelper.with(AbstractApplication.get()).load("https://upload.wikimedia.org/wikipedia/commons/2/2c/A_new_map_of_Great_Britain_according_to_the_newest_and_most_exact_observations_%288342715024%29.jpg").listener(new LoggingRequestListener<>()).apply(options).into(imageView);
			}
		});
	}
}
