package com.jdroid.android.google.signin;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public interface GoogleSignInListener {

	void onGoogleSignIn(GoogleSignInAccount googleSignInAccount);

	void onGoogleSignOut();

	void onGoogleAccessRevoked();
}
