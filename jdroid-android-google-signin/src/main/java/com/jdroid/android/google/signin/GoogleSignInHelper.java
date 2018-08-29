package com.jdroid.android.google.signin;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.jdroid.android.activity.ActivityLauncher;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.java.utils.RandomUtils;

import org.slf4j.Logger;

public class GoogleSignInHelper {

	private static Logger LOGGER = LoggerUtils.getLogger(GoogleSignInHelper.class);

	private static final int RC_SIGN_IN = RandomUtils.get16BitsInt();

	private GoogleSignInClient googleSignInClient;

	private AbstractFragment abstractFragment;
	private GoogleSignInListener googleSignInListener;

	public GoogleSignInHelper(AbstractFragment abstractFragment, GoogleSignInListener googleSignInListener) {
		this.abstractFragment = abstractFragment;
		this.googleSignInListener = googleSignInListener;
		googleSignInClient = GoogleSignIn.getClient(abstractFragment.getContext(), getGoogleSignInOptionsBuilder().build());
	}

	protected GoogleSignInOptions.Builder getGoogleSignInOptionsBuilder() {
		// Configure sign-in to request the user's ID, email address, and basic
		// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
		GoogleSignInOptions.Builder builder = new GoogleSignInOptions.Builder(getGoogleSignInOptions());
		builder.requestEmail();
		if (isRequestIdTokenEnabled()) {
			builder.requestIdToken(AbstractApplication.get().getAppContext().getServerClientId());
		}
		if (isServerAuthCodeEnabled()) {
			builder.requestServerAuthCode(AbstractApplication.get().getAppContext().getServerClientId(), false);
		}
		return builder;
	}

	protected GoogleSignInOptions getGoogleSignInOptions() {
		return GoogleSignInOptions.DEFAULT_SIGN_IN;
	}

	protected Boolean isRequestIdTokenEnabled() {
		return false;
	}

	protected Boolean isServerAuthCodeEnabled() {
		return false;
	}

	public GoogleSignInAccount getLastSignedInAccount() {
		return GoogleSignIn.getLastSignedInAccount(abstractFragment.getContext());
	}

	public void verifyLastSignedInAccount() {
		GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(abstractFragment.getContext());

		if (googleSignInAccount != null) {
			if (googleSignInListener != null) {
				googleSignInListener.onGoogleSignIn(googleSignInAccount);
			}
		} else {
			if (googleSignInListener != null) {
				googleSignInListener.onGoogleSignOut();
			}
		}
	}

	public void silentSignIn() {
		// Attempt to silently refresh the GoogleSignInAccount. If the GoogleSignInAccount
		// already has a valid token this method may complete immediately.
		//
		// If the user has not previously signed in on this device or the sign-in has expired,
		// this asynchronous branch will attempt to sign in the user silently and get a valid
		// ID token. Cross-device single sign on will occur in this branch.
		Task<GoogleSignInAccount> task = googleSignInClient.silentSignIn();
		if (task.isSuccessful()) {
			// There's immediate result available.
			handleSignInResult(task);
		} else {
			showLoading();
			task.addOnCompleteListener(abstractFragment.getActivity(), new OnCompleteListener<GoogleSignInAccount>() {
				@Override
				public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
					handleSignInResult(task);
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
		Intent signInIntent = googleSignInClient.getSignInIntent();
		ActivityLauncher.startActivityForResult(abstractFragment.getActivity(), signInIntent, RC_SIGN_IN);
	}

	public void signOut() {
		googleSignInClient.signOut().addOnCompleteListener(abstractFragment.getActivity(), new OnCompleteListener<Void>() {
			@Override
			public void onComplete(@NonNull Task<Void> task) {
				LOGGER.debug("SignOut");
				if (googleSignInListener != null) {
					googleSignInListener.onGoogleSignOut();
				}
			}
		});
	}

	public void revokeAccess() {
		googleSignInClient.revokeAccess().addOnCompleteListener(abstractFragment.getActivity(),
			new OnCompleteListener<Void>() {
				@Override
				public void onComplete(@NonNull Task<Void> task) {
					LOGGER.debug("RevokeAccess");
					if (googleSignInListener != null) {
						googleSignInListener.onGoogleAccessRevoked();
					}
				}
			});
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
		if (requestCode == RC_SIGN_IN) {
			// The Task returned from this call is always completed, no need to attach a listener.
			Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
			handleSignInResult(task);
		}
	}

	private void handleSignInResult(@NonNull Task<GoogleSignInAccount> completedTask) {
		try {
			GoogleSignInAccount account = completedTask.getResult(ApiException.class);
			if (isRequestIdTokenEnabled()) {
				if (!isTokenValid(account.getIdToken())) {
					revokeAccess();
					return;
				}
			}

			LOGGER.debug("SignIn valid");

			if (googleSignInListener != null) {
				googleSignInListener.onGoogleSignIn(account);
			}
		} catch (ApiException e) {
			AbstractApplication.get().getExceptionHandler().logHandledException(e);
			if (googleSignInListener != null) {
				googleSignInListener.onGoogleSignOut();
			}
		}
	}

	protected Boolean isTokenValid(String idToken) {
		// https://developers.google.com/identity/sign-in/android/backend-auth
		return true;
	}

	public void setGoogleSignInListener(GoogleSignInListener googleSignInListener) {
		this.googleSignInListener = googleSignInListener;
	}
}
