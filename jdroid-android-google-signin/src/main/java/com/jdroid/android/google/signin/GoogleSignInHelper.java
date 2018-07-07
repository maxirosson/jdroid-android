package com.jdroid.android.google.signin;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.jdroid.android.activity.ActivityLauncher;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.java.utils.RandomUtils;

import org.slf4j.Logger;

public class GoogleSignInHelper {

	private static Logger LOGGER = LoggerUtils.getLogger(GoogleSignInHelper.class);

	private static final int RC_SIGN_IN = RandomUtils.get16BitsInt();

	private GoogleApiClient googleApiClient;

	private AbstractFragment abstractFragment;
	private GoogleSignInListener googleSignInListener;

	public GoogleSignInHelper(AbstractFragment abstractFragment, GoogleSignInListener googleSignInListener) {
		this.abstractFragment = abstractFragment;
		this.googleSignInListener = googleSignInListener;
		googleApiClient = getGoogleApiClientBuilder().build();
	}

	protected GoogleApiClient.Builder getGoogleApiClientBuilder() {
		GoogleApiClient.Builder builder = new GoogleApiClient.Builder(abstractFragment.getActivity());
		builder.enableAutoManage(abstractFragment.getActivity(), googleSignInListener);
		builder.addApi(Auth.GOOGLE_SIGN_IN_API, getGoogleSignInOptionsBuilder().build());
		return builder;
	}

	protected GoogleSignInOptions.Builder getGoogleSignInOptionsBuilder() {
		// Configure sign-in to request the user's ID, email address, and basic
		// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
		GoogleSignInOptions.Builder builder = new GoogleSignInOptions.Builder();
		builder.requestEmail();
		if (isRequestIdTokenEnabled()) {
			builder.requestIdToken(AbstractApplication.get().getAppContext().getServerClientId());
		}
		if (isServerAuthCodeEnabled()) {
			builder.requestServerAuthCode(AbstractApplication.get().getAppContext().getServerClientId(), false);
		}
		return builder;
	}

	protected Boolean isRequestIdTokenEnabled() {
		return false;
	}

	protected Boolean isServerAuthCodeEnabled() {
		return false;
	}

	public void silentSignIn() {
		OptionalPendingResult<GoogleSignInResult> pendingResult = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
		if (pendingResult.isDone()) {
			// If the user's cached credentials are valid, the OptionalPendingResult will be "done"
			// and the GoogleSignInResult will be available instantly.
			GoogleSignInResult result = pendingResult.get();
			handleSignInResult(result);
		} else {
			// If the user has not previously signed in on this device or the sign-in has expired,
			// this asynchronous branch will attempt to sign in the user silently.  Cross-device
			// single sign-on will occur in this branch.
			showLoading();
			pendingResult.setResultCallback(new ResultCallback<GoogleSignInResult>() {
				@Override
				public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
					handleSignInResult(googleSignInResult);
					dismissLoading();
				}
			});
		}
	}

	protected void showLoading() {
		abstractFragment.showLoading();
	}

	protected void dismissLoading() {
		abstractFragment.dismissLoading();
	}

	public void signIn() {
		Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
		ActivityLauncher.startActivityForResult(abstractFragment.getActivity(), signInIntent, RC_SIGN_IN);
	}

	public void signOut() {
		Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
				new ResultCallback<Status>() {
					@Override
					public void onResult(@NonNull Status status) {
						LOGGER.debug("signOut: " + status.getStatusMessage());
						if (googleSignInListener != null) {
							googleSignInListener.onGoogleSignOut();
						}
					}
				});
	}

	public void revokeAccess() {
		Auth.GoogleSignInApi.revokeAccess(googleApiClient).setResultCallback(
				new ResultCallback<Status>() {
					@Override
					public void onResult(@NonNull Status status) {
						LOGGER.debug("revokeAccess: " + status.getStatusMessage());
						if (googleSignInListener != null) {
							googleSignInListener.onGoogleAccessRevoked();
						}
					}
				});
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
		if (requestCode == RC_SIGN_IN) {
			GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
			handleSignInResult(result);
		}
	}

	private void handleSignInResult(GoogleSignInResult result) {
		LOGGER.debug("handleSignInResult: " + result.isSuccess());
		if (result.isSuccess()) {
			if (googleSignInListener != null) {
				googleSignInListener.onGoogleSignIn(result.getSignInAccount());
			}
		} else {
			if (googleSignInListener != null) {
				googleSignInListener.onGoogleSignOut();
			}
		}
	}

	public void setGoogleSignInListener(GoogleSignInListener googleSignInListener) {
		this.googleSignInListener = googleSignInListener;
	}
}
