package com.jdroid.android.glide;

import android.app.Activity;
import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.concurrent.AppExecutors;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class GlideHelper {

	private static final Logger LOGGER = LoggerUtils.getLogger(GlideHelper.class);

	public static RequestManager with(@Nullable Fragment fragment) {
		if (fragment != null) {
			if (fragment.getActivity() != null) {
				return Glide.with(fragment);
			} else {
				// We do this to avoid an IllegalArgumentException("You cannot start a load on a fragment before it is attached")
				LOGGER.warn("Fragment " + fragment.getClass().getSimpleName() + " has not activity attached when starting a Glide load.");
				return Glide.with(AbstractApplication.get());
			}
		} else {
			LOGGER.warn("Trying to start a Glide load with a null fragment.");
			return Glide.with(AbstractApplication.get());
		}
	}

	public static RequestManager with(@Nullable Context context) {
		if (context != null) {
			if (context instanceof FragmentActivity) {
				return with((FragmentActivity)context);
			} else if (context instanceof Activity) {
				return with((Activity)context);
			} else {
				return Glide.with(context);
			}
		} else {
			LOGGER.warn("Trying to start a Glide load with a null context.");
			return Glide.with(AbstractApplication.get());
		}
	}


	private static RequestManager with(@Nullable Activity activity) {
		if (activity != null) {
			if (activity.isDestroyed()) {
				// We do this to avoid an IllegalArgumentException("You cannot start a load for a destroyed activity")
				LOGGER.warn("Activity " + activity.getClass().getSimpleName() + " is destroyed when starting a Glide load.");
				return Glide.with(AbstractApplication.get());
			} else {
				return activity instanceof FragmentActivity ? Glide.with((FragmentActivity)activity) : Glide.with(activity);
			}
		} else {
			LOGGER.warn("Trying to start a Glide load with a null activity.");
			return Glide.with(AbstractApplication.get());
		}
	}

	public static void clearDiskCache() {
		AppExecutors.INSTANCE.getDiskIOExecutor().execute(new Runnable() {
			@Override
			public void run() {
				Glide.get(AbstractApplication.get()).clearDiskCache();
			}
		});
	}

	public static void clearMemory() {
		Glide.get(AbstractApplication.get()).clearMemory();
	}
}
