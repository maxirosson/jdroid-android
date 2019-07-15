package com.jdroid.android.about;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jdroid.android.context.AbstractAppContext;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.utils.AppUtils;
import com.jdroid.java.utils.ReflectionUtils;

public class AboutContext extends AbstractAppContext {

	@NonNull
	@SuppressWarnings("unchecked")
	public Class<? extends AbstractFragment> getAboutFragmentClass() {
		return (Class<? extends AbstractFragment>)ReflectionUtils.getClass("com.jdroid.android.about.AboutFragment");
	}

	@NonNull
	@SuppressWarnings("unchecked")
	public Class<? extends AbstractFragment> getLibrariesFragmentClass() {
		return (Class<? extends AbstractFragment>)ReflectionUtils.getClass("com.jdroid.android.about.LibrariesFragment");
	}

	@Nullable
	public Class<? extends AbstractFragment> getSpreadTheLoveFragmentClass() {
		return null;
	}

	public Boolean isBetaTestingEnabled() {
		return false;
	}

	public String getBetaTestingUrl() {
		return "https://play.google.com/apps/testing/" + AppUtils.INSTANCE.getReleaseApplicationId();
	}
}
