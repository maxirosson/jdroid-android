package com.jdroid.android.google.playgames;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.google.signin.GoogleSignInHelper;
import com.jdroid.android.google.signin.GoogleSignInListener;

public class GooglePlayGamesHelper extends GoogleSignInHelper {

	public GooglePlayGamesHelper(AbstractFragment abstractFragment, GoogleSignInListener googleSignInListener) {
		super(abstractFragment, googleSignInListener);
	}

	@Override
	protected GoogleSignInOptions getGoogleSignInOptions() {
		return GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN;
	}

}
