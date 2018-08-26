package com.jdroid.android.sample.ui.appinvite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jdroid.android.about.appinvite.AppInviteView;
import com.jdroid.android.firebase.invites.AppInviteHelper;
import com.jdroid.android.firebase.invites.AppInviteStats;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.sample.R;

public class AppInviteFragment extends AbstractFragment {

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.app_invite_fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		AppInviteView appInviteView = findView(R.id.appInvite);
		appInviteView.configure(getActivity());

		AppInviteView appInviteWithStats = findView(R.id.appInviteWithStats);
		if (AppInviteStats.displayAppInviteView(getActivity())) {
			appInviteWithStats.configure(getActivity());
			appInviteWithStats.setVisibility(View.VISIBLE);
		} else {
			appInviteWithStats.setVisibility(View.GONE);
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		AppInviteHelper.onActivityResult(requestCode, resultCode, data);
	}
}
