package com.jdroid.android.google.plus;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import com.google.android.gms.plus.PlusOneButton;
import com.google.android.gms.plus.PlusOneButton.OnPlusOneClickListener;
import com.jdroid.android.activity.ActivityLauncher;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.google.GooglePlayServicesUtils;
import com.jdroid.android.google.GooglePlayUtils;
import com.jdroid.android.social.SocialAction;
import com.jdroid.android.social.SocialNetwork;

import java.util.Locale;

public class GooglePlusOneButtonHelper {

	private static final int PLUS_ONE_REQUEST_CODE = 100;
	private static final int PLUS_ONE_UNDO_REQUEST_CODE = 101;

	private String url;
	protected PlusOneButton plusOneButton;
	private Fragment context;

	public GooglePlusOneButtonHelper(Fragment context, int plusOneButton) {
		this(context, (PlusOneButton)context.getView().findViewById(plusOneButton));
	}

	public GooglePlusOneButtonHelper(Fragment context, PlusOneButton plusOneButton) {
		this.context = context;
		this.plusOneButton = plusOneButton;
		url = getUrl();
	}

	public void onResume() {

		if (GooglePlayServicesUtils.isGooglePlayServicesAvailable(context.getActivity())) {
			plusOneButton.initialize(url, new OnPlusOneClickListener() {

				@Override
				public void onPlusOneClick(Intent intent) {
					if (intent != null) {
						if (intent.getAction().toLowerCase(Locale.US).contains("undo")) {
							ActivityLauncher.startActivityForResult(context.getActivity(), intent, PLUS_ONE_UNDO_REQUEST_CODE);
						} else {
							ActivityLauncher.startActivityForResult(context.getActivity(), intent, PLUS_ONE_REQUEST_CODE);
						}
					}
				}
			});
			plusOneButton.setVisibility(View.VISIBLE);
		} else {
			plusOneButton.setVisibility(View.GONE);
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if ((requestCode == PLUS_ONE_UNDO_REQUEST_CODE) && (resultCode != 0)) {
			AbstractApplication.get().getCoreAnalyticsSender().trackSocialInteraction(SocialNetwork.GOOGLE_PLUS.getName(),
				SocialAction.PLUS_ONE_UNDO, getUrl());
			onUndoPlusOne();
		} else if ((requestCode == PLUS_ONE_REQUEST_CODE) && (resultCode != 0)) {
			AbstractApplication.get().getCoreAnalyticsSender().trackSocialInteraction(SocialNetwork.GOOGLE_PLUS.getName(),
				SocialAction.PLUS_ONE, getUrl());
			onPlusOne();
		}
	}

	protected void onPlusOne() {
		// Do Nothing
	}

	protected void onUndoPlusOne() {
		// Do Nothing
	}

	protected String getUrl() {
		return GooglePlayUtils.getGooglePlayLink();
	}
}
