package com.jdroid.android.google.plus;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.google.android.gms.plus.PlusShare;
import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.activity.ActivityLauncher;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.google.GooglePlayUtils;
import com.jdroid.android.intent.IntentUtils;
import com.jdroid.android.social.SocialAction;
import com.jdroid.android.social.SocialNetwork;
import com.jdroid.android.utils.ExternalAppsUtils;
import com.jdroid.java.http.MimeType;

public class GooglePlusHelperFragment extends AbstractFragment {

	private static final String SHARE_LINK_KEY = "shareLink";

	// Request code to use when launching the resolution activity
	public static final int SHARE_REQUEST_CODE = 1002;

	private String shareLink;

	public static void add(FragmentActivity activity,
						   Class<? extends GooglePlusHelperFragment> googlePlusHelperFragmentClass, Fragment targetFragment) {
		add(activity, googlePlusHelperFragmentClass, null, targetFragment);
	}

	public static void add(FragmentActivity activity,
						   Class<? extends GooglePlusHelperFragment> googlePlusHelperFragmentClass, Bundle bundle,
						   Fragment targetFragment) {

		AbstractFragmentActivity abstractFragmentActivity = (AbstractFragmentActivity)activity;
		GooglePlusHelperFragment googlePlusHelperFragment = abstractFragmentActivity.instanceFragment(
			googlePlusHelperFragmentClass, bundle);
		googlePlusHelperFragment.setTargetFragment(targetFragment, 0);
		FragmentTransaction fragmentTransaction = abstractFragmentActivity.getSupportFragmentManager().beginTransaction();
		fragmentTransaction.add(0, googlePlusHelperFragment, GooglePlusHelperFragment.class.getSimpleName());
		fragmentTransaction.commit();
	}

	public static void remove(FragmentActivity activity) {
		Fragment fragmentToRemove = get(activity);
		if (fragmentToRemove != null) {
			FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
			fragmentTransaction.remove(fragmentToRemove);
			fragmentTransaction.commit();
		}
	}

	public static GooglePlusHelperFragment get(FragmentActivity activity) {
		return ((AbstractFragmentActivity)activity).getFragment(GooglePlusHelperFragment.class);
	}

	public static void removeTarget(FragmentActivity activity) {
		GooglePlusHelperFragment googlePlusHelperFragment = GooglePlusHelperFragment.get(activity);
		if (googlePlusHelperFragment != null) {
			googlePlusHelperFragment.setTargetFragment(null, 0);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);

		if (savedInstanceState != null) {
			shareLink = savedInstanceState.getString(SHARE_LINK_KEY);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(SHARE_LINK_KEY, shareLink);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if ((requestCode == SHARE_REQUEST_CODE) && (resultCode == Activity.RESULT_OK) && (shareLink != null)) {
			AbstractApplication.get().getCoreAnalyticsSender().trackSocialInteraction(SocialNetwork.GOOGLE_PLUS.getName(),
				SocialAction.SHARE, shareLink);
			shareLink = null;
		}
	}

	public void shareDeeplink(String content, String link) {
		PlusShare.Builder builder = new PlusShare.Builder(getActivity());
		builder.setText(content);
		builder.setType(MimeType.TEXT);
		builder.setContentUrl(Uri.parse(link));
		builder.setContentDeepLinkId(link);

		Intent intent = builder.getIntent();
		shareLink = link;
		if (IntentUtils.isIntentAvailable(intent)) {
			ActivityLauncher.startActivityForResult(getActivity(), intent, SHARE_REQUEST_CODE);
		} else {
			GooglePlayUtils.showDownloadDialog(getActivity(), R.string.jdroid_googlePlus, ExternalAppsUtils.GOOGLE_PLUS_PACKAGE_NAME);
		}
	}

	public void share(String content, String link) {
		PlusShare.Builder builder = new PlusShare.Builder(getActivity());
		builder.setText(content);
		builder.setType(MimeType.TEXT);
		builder.setContentUrl(Uri.parse(link));

		Intent intent = builder.getIntent();
		shareLink = link;
		if (IntentUtils.isIntentAvailable(intent)) {
			ActivityLauncher.startActivityForResult(getActivity(), intent, SHARE_REQUEST_CODE);
		} else {
			GooglePlayUtils.showDownloadDialog(getActivity(), R.string.jdroid_googlePlus, ExternalAppsUtils.GOOGLE_PLUS_PACKAGE_NAME);
		}
	}

	public static void openCommunity(@Nullable Activity activity, String community) {
		ActivityLauncher.startActivity(activity,
			new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/u/0/communities/" + community)));
		AbstractApplication.get().getCoreAnalyticsSender().trackSocialInteraction(SocialNetwork.GOOGLE_PLUS.getName(),
			SocialAction.OPEN_PROFILE, community);
	}
}
