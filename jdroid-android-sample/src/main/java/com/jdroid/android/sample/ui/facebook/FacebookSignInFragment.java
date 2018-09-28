package com.jdroid.android.sample.ui.facebook;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.loading.FragmentLoading;
import com.jdroid.android.loading.NonBlockingLoading;
import com.jdroid.android.sample.R;

public class FacebookSignInFragment extends AbstractFragment {

	private LoginButton signInButton;
	private TextView status;

	private CallbackManager callbackManager;

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.facebook_signin_fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		callbackManager = CallbackManager.Factory.create();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		status = findView(R.id.status);

		signInButton = findView(R.id.signIn);
		signInButton.setFragment(this);
		signInButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(LoginResult loginResult) {
				setLoggedStatus();

				Snackbar.make(findView(R.id.container), "onSignIn", Snackbar.LENGTH_SHORT).show();
			}

			@Override
			public void onCancel() {
				// App code
			}

			@Override
			public void onError(FacebookException exception) {
				status.setText(exception.toString());
				Snackbar.make(findView(R.id.container), "onError", Snackbar.LENGTH_SHORT).show();
			}
		});
	}

	private void setLoggedStatus() {
		StringBuilder builder = new StringBuilder();
		builder.append("Name: ");
		builder.append(Profile.getCurrentProfile().getName());
		builder.append("\nId: ");
		builder.append(Profile.getCurrentProfile().getId());
		builder.append("\nAccess Token: ");
		builder.append(AccessToken.getCurrentAccessToken());
		status.setText(builder.toString());
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public FragmentLoading getDefaultLoading() {
		return new NonBlockingLoading();
	}
}
