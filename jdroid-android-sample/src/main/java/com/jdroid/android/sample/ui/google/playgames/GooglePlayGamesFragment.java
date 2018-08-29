package com.jdroid.android.sample.ui.google.playgames;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.google.signin.GoogleSignInHelper;
import com.jdroid.android.google.signin.GoogleSignInListener;
import com.jdroid.android.loading.FragmentLoading;
import com.jdroid.android.loading.NonBlockingLoading;
import com.jdroid.android.sample.R;

public class GooglePlayGamesFragment extends AbstractFragment implements GoogleSignInListener {

	private GoogleSignInHelper googleSignInHelper;

	private SignInButton signInButton;
	private View signOutButton;
	private View revokeButton;
	private TextView status;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.google_play_games_fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		status = findView(R.id.status);

		signInButton = findView(R.id.signIn);
		signInButton.setSize(SignInButton.SIZE_WIDE);
		signInButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				googleSignInHelper.signIn();
			}
		});

		signOutButton = findView(R.id.signOut);
		signOutButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				googleSignInHelper.signOut();
			}
		});

		revokeButton = findView(R.id.revoke);
		revokeButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				googleSignInHelper.revokeAccess();
			}
		});

		googleSignInHelper = new GoogleSignInHelper(this, this) {

			@Override
			protected GoogleSignInOptions getGoogleSignInOptions() {
				return GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN;
			}
		};
	}

	@Override
	public void onStart() {
		super.onStart();

		googleSignInHelper.verifyLastSignedInAccount();

		// TODO
		//googleSignInHelper.silentSignIn();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		googleSignInHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onGoogleSignIn(GoogleSignInAccount googleSignInAccount) {
		StringBuilder builder = new StringBuilder();
		builder.append("Display Name: ");
		builder.append(googleSignInAccount.getDisplayName());
		builder.append("\nEmail: ");
		builder.append(googleSignInAccount.getEmail());
		builder.append("\nGranted Scopes: ");
		builder.append(googleSignInAccount.getGrantedScopes());
		builder.append("\nId Token: ");
		builder.append(googleSignInAccount.getIdToken() != null ? googleSignInAccount.getIdToken().substring(0, 50) : null);
		status.setText(builder.toString());


		signInButton.setVisibility(View.GONE);
		signOutButton.setVisibility(View.VISIBLE);

		Snackbar.make(findView(R.id.container), "onGoogleSignIn", Snackbar.LENGTH_SHORT).show();
	}

	@Override
	public void onGoogleSignOut() {
		status.setText(R.string.notLogged);

		signInButton.setVisibility(View.VISIBLE);
		signOutButton.setVisibility(View.GONE);

		Snackbar.make(findView(R.id.container), "onGoogleSignOut", Snackbar.LENGTH_SHORT).show();
	}

	@Override
	public void onGoogleAccessRevoked() {
		status.setText(R.string.notLogged);

		signInButton.setVisibility(View.VISIBLE);
		signOutButton.setVisibility(View.GONE);

		Snackbar.make(findView(R.id.container), "onGoogleAccessRevoked", Snackbar.LENGTH_SHORT).show();
	}

	@Override
	public FragmentLoading getDefaultLoading() {
		return new NonBlockingLoading();
	}
}
