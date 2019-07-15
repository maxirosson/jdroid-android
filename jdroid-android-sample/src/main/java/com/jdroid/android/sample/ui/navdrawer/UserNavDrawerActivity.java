package com.jdroid.android.sample.ui.navdrawer;

import android.os.Bundle;

import com.jdroid.android.activity.FragmentContainerActivity;
import com.jdroid.android.auth.SecurityContext;
import com.jdroid.android.domain.User;
import com.jdroid.java.utils.RandomUtils;

import androidx.fragment.app.Fragment;

public class UserNavDrawerActivity extends FragmentContainerActivity {

	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		user = new User() {
			@Override
			public Long getId() {
				return RandomUtils.INSTANCE.getLong();
			}

			@Override
			public String getUserName() {
				return null;
			}

			@Override
			public String getFullname() {
				return "Tony Stark";
			}

			@Override
			public String getEmail() {
				return "tonystark@ironmail.com";
			}

			@Override
			public String getFirstName() {
				return null;
			}

			@Override
			public String getLastName() {
				return null;
			}

			@Override
			public String getUserToken() {
				return null;
			}

			@Override
			public String getProfilePictureUrl() {
				return null;
			}

			@Override
			public String getCoverPictureUrl() {
				return null;
			}
		};
		SecurityContext.INSTANCE.attach(user);
	}

	@Override
	protected void onStart() {
		super.onStart();

		SecurityContext.INSTANCE.attach(user);
	}

	@Override
	protected void onStop() {
		super.onStop();

		SecurityContext.INSTANCE.detachUser();
	}

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return UserNavDrawerFragment.class;
	}
}