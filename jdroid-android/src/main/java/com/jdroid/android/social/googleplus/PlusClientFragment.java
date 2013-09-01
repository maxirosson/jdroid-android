package com.jdroid.android.social.googleplus;

import java.util.Arrays;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.PlusClient.OnAccessRevokedListener;
import com.jdroid.android.R;

/**
 * A fragment that you can add to your layout to automate connecting a {@link PlusClient}. The key integration points
 * are to attach the fragment with {@link #getPlusClientFragment} in your Activity's {@code onCreate} method, then call
 * {@link #signIn} when the user clicks your sign-in button and forward calls to {@code onActivityResult} to
 * {@link #handleOnActivityResult}.
 */
public final class PlusClientFragment extends Fragment implements ConnectionCallbacks, OnConnectionFailedListener,
		OnAccessRevokedListener {
	
	private static final int REQUEST_CODE_PLUS_CLIENT_FRAGMENT = 0;
	
	/**
	 * Tag to refer to this fragment.
	 */
	private static final String TAG_PLUS_CLIENT = "plusClientFragment";
	
	/**
	 * Tag to refer to an error (resolution) dialog.
	 */
	private static final String TAG_ERROR_DIALOG = "plusClientFragmentErrorDialog";
	
	/**
	 * Tag to refer to a progress dialog when sign in is requested.
	 */
	private static final String TAG_PROGRESS_DIALOG = "plusClientFragmentProgressDialog";
	
	/**
	 * Array of strings representing visible activities to request for {@link #getArguments()}.
	 */
	private static final String ARG_VISIBLE_ACTIVITIES = "visible_activities";
	
	/**
	 * Integer request code to apply to requests, as set by {@link #signIn(int)}.
	 */
	private static final String STATE_REQUEST_CODE = "request_code";
	
	/**
	 * Signed in successfully connection state.
	 */
	private static final ConnectionResult CONNECTION_RESULT_SUCCESS = new ConnectionResult(ConnectionResult.SUCCESS,
			null);
	
	/**
	 * An invalid request code to use to indicate that {@link #signIn(int)} hasn't been called.
	 */
	private static final int INVALID_REQUEST_CODE = -1;
	
	// The PlusClient to connect.
	private PlusClient plusClient;
	
	// The last result from onConnectionFailed.
	private ConnectionResult lastConnectionResult;
	
	// The request specified in signIn or INVALID_REQUEST_CODE if not signing in.
	private int requestCode;
	
	// A handler to post callbacks (rather than call them in a potentially reentrant way.)
	private Handler mHandler;
	
	/**
	 * Local handler to send callbacks on sign in.
	 */
	private final class PlusClientFragmentHandler extends Handler {
		
		public static final int WHAT_SIGNED_IN = 1;
		
		public PlusClientFragmentHandler() {
			super(Looper.getMainLooper());
		}
		
		@Override
		public void handleMessage(Message msg) {
			if (msg.what != WHAT_SIGNED_IN) {
				return;
			}
			
			if (plusClient.isConnected()) {
				getOnSignedInListener().onSignedIn(plusClient);
			}
		}
	}
	
	private OnSignedInListener getOnSignedInListener() {
		return (OnSignedInListener)getTargetFragment();
	}
	
	/**
	 * Listener interface for sign in events. Activities hosting a {@link PlusClientFragment} must implement this.
	 */
	public static interface OnSignedInListener {
		
		/**
		 * Called when the {@link PlusClient} has been connected successfully.
		 * 
		 * @param plusClient The connected {@link PlusClient} to make requests on.
		 */
		void onSignedIn(PlusClient plusClient);
	}
	
	/**
	 * Attach a {@link PlusClient} managing fragment to you activity.
	 * 
	 * @param activity The activity to attach the fragment to.
	 * @param targetFragment
	 * @param visibleActivities An array of visible activities to request.
	 * @return The fragment managing a {@link PlusClient}.
	 */
	public static PlusClientFragment getPlusClientFragment(FragmentActivity activity, Fragment targetFragment,
			String[] visibleActivities) {
		
		if (!(targetFragment instanceof OnSignedInListener)) {
			throw new IllegalArgumentException(
					"The target fragment must implement OnSignedInListener to receive callbacks.");
		}
		
		// Check if the fragment is already attached.
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		Fragment fragment = fragmentManager.findFragmentByTag(TAG_PLUS_CLIENT);
		if (fragment instanceof PlusClientFragment) {
			// The fragment is attached. If it has the right visible activities, return it.
			if (Arrays.equals(visibleActivities, fragment.getArguments().getStringArray(ARG_VISIBLE_ACTIVITIES))) {
				fragment.setTargetFragment(targetFragment, 0);
				return (PlusClientFragment)fragment;
			}
		}
		
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		// If a fragment was already attached, remove it to clean up.
		if (fragment != null) {
			fragmentTransaction.remove(fragment);
		}
		
		// Create a new fragment and attach it to the fragment manager.
		Bundle arguments = new Bundle();
		arguments.putStringArray(ARG_VISIBLE_ACTIVITIES, visibleActivities);
		PlusClientFragment signInFragment = new PlusClientFragment();
		signInFragment.setArguments(arguments);
		signInFragment.setTargetFragment(targetFragment, 0);
		fragmentTransaction.add(signInFragment, TAG_PLUS_CLIENT);
		fragmentTransaction.commit();
		return signInFragment;
	}
	
	/**
	 * Creates a {@link PlusClient} and kicks-off the connection flow.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Retain instance to avoid reconnecting on rotate. This means that onDestroy and onCreate
		// will not be called on configuration changes.
		setRetainInstance(true);
		mHandler = new PlusClientFragmentHandler();
		
		// Create the PlusClient.
		PlusClient.Builder plusClientBuilder = new PlusClient.Builder(getActivity().getApplicationContext(), this, this);
		String[] visibleActivities = getArguments().getStringArray(ARG_VISIBLE_ACTIVITIES);
		if ((visibleActivities != null) && (visibleActivities.length > 0)) {
			plusClientBuilder.setVisibleActivities(visibleActivities);
		}
		plusClient = plusClientBuilder.build();
		
		if (savedInstanceState == null) {
			requestCode = INVALID_REQUEST_CODE;
		} else {
			requestCode = savedInstanceState.getInt(STATE_REQUEST_CODE, INVALID_REQUEST_CODE);
		}
	}
	
	/**
	 * Disconnects the {@link PlusClient} to avoid leaks.
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (plusClient.isConnecting() || plusClient.isConnected()) {
			plusClient.disconnect();
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_REQUEST_CODE, requestCode);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (requestCode == INVALID_REQUEST_CODE) {
			// No user interaction, hide the progress dialog.
			hideProgressDialog();
			hideErrorDialog();
		} else if ((lastConnectionResult != null) && !lastConnectionResult.isSuccess() && !isShowingErrorDialog()) {
			showProgressDialog();
		}
	}
	
	@Override
	public void onStart() {
		super.onStart();
		if (requestCode == INVALID_REQUEST_CODE) {
			lastConnectionResult = null;
			connectPlusClient();
		}
	}
	
	@Override
	public void onConnected(Bundle connectionHint) {
		// Successful connection!
		lastConnectionResult = CONNECTION_RESULT_SUCCESS;
		requestCode = INVALID_REQUEST_CODE;
		
		if (isResumed()) {
			hideProgressDialog();
		}
		
		getOnSignedInListener().onSignedIn(plusClient);
	}
	
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		lastConnectionResult = connectionResult;
		// On a failed connection try again.
		if (isResumed() && (requestCode != INVALID_REQUEST_CODE)) {
			resolveLastResult();
		}
	}
	
	@Override
	public void onAccessRevoked(ConnectionResult status) {
		// Reconnect to get a new mPlusClient.
		lastConnectionResult = null;
		// Cancel sign in.
		requestCode = INVALID_REQUEST_CODE;
		
		// Reconnect to fetch the sign-in (account chooser) intent from the plus client.
		connectPlusClient();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// Let new activities know the signed-in state.
		if (plusClient.isConnected()) {
			mHandler.sendEmptyMessage(PlusClientFragmentHandler.WHAT_SIGNED_IN);
		}
	}
	
	@Override
	public void onDisconnected() {
		// Do nothing.
	}
	
	/**
	 * Shows any UI required to resolve the error connecting.
	 * 
	 */
	public void signIn() {
		if (requestCode < 0) {
			throw new IllegalArgumentException("A non-negative request code is required.");
		}
		
		if (plusClient.isConnected()) {
			// Already connected! Schedule callback.
			mHandler.sendEmptyMessage(PlusClientFragmentHandler.WHAT_SIGNED_IN);
			return;
		}
		
		if (requestCode != INVALID_REQUEST_CODE) {
			// We're already signing in.
			return;
		}
		
		requestCode = REQUEST_CODE_PLUS_CLIENT_FRAGMENT;
		if (lastConnectionResult == null) {
			// We're starting up, show progress.
			showProgressDialog();
			return;
		}
		
		resolveLastResult();
	}
	
	/**
	 * Perform resolution given a non-null result.
	 */
	private void resolveLastResult() {
		if (GooglePlayServicesUtil.isUserRecoverableError(lastConnectionResult.getErrorCode())) {
			// Show a dialog to install or enable Google Play services.
			showErrorDialog(ErrorDialogFragment.create(lastConnectionResult.getErrorCode(), requestCode));
			return;
		}
		
		if (lastConnectionResult.hasResolution()) {
			startResolution();
		}
	}
	
	public static final class ErrorDialogFragment extends GooglePlayServicesErrorDialogFragment {
		
		public static ErrorDialogFragment create(int errorCode, int requestCode) {
			ErrorDialogFragment fragment = new ErrorDialogFragment();
			fragment.setArguments(createArguments(errorCode, requestCode));
			return fragment;
		}
		
		@Override
		public void onCancel(DialogInterface dialog) {
			super.onCancel(dialog);
			FragmentActivity activity = getActivity();
			if (activity == null) {
				return;
			}
			
			Fragment fragment = activity.getSupportFragmentManager().findFragmentByTag(TAG_PLUS_CLIENT);
			if (fragment instanceof PlusClientFragment) {
				((PlusClientFragment)fragment).onDialogCanceled(getTag());
			}
		}
		
		@Override
		public void onDismiss(DialogInterface dialog) {
			super.onDismiss(dialog);
			FragmentActivity activity = getActivity();
			if (activity == null) {
				return;
			}
			
			Fragment fragment = activity.getSupportFragmentManager().findFragmentByTag(TAG_PLUS_CLIENT);
			if (fragment instanceof PlusClientFragment) {
				((PlusClientFragment)fragment).onDialogDismissed(getTag());
			}
		}
	}
	
	private void onDialogCanceled(String tag) {
		requestCode = INVALID_REQUEST_CODE;
		hideProgressDialog();
	}
	
	private void onDialogDismissed(String tag) {
		if (TAG_PROGRESS_DIALOG.equals(tag)) {
			requestCode = INVALID_REQUEST_CODE;
			hideProgressDialog();
		}
	}
	
	private void showProgressDialog() {
		DialogFragment progressDialog = (DialogFragment)getFragmentManager().findFragmentByTag(TAG_PROGRESS_DIALOG);
		if (progressDialog == null) {
			progressDialog = ProgressDialogFragment.create();
			progressDialog.show(getFragmentManager(), TAG_PROGRESS_DIALOG);
		}
	}
	
	public static final class ProgressDialogFragment extends DialogFragment {
		
		private static final String ARG_MESSAGE = "message";
		
		public static ProgressDialogFragment create(int message) {
			ProgressDialogFragment progressDialogFragment = new ProgressDialogFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_MESSAGE, message);
			progressDialogFragment.setArguments(args);
			return progressDialogFragment;
		}
		
		public static ProgressDialogFragment create() {
			return create(R.string.signingToGooglePlus);
		}
		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			ProgressDialog progressDialog = new ProgressDialog(getActivity());
			progressDialog.setIndeterminate(true);
			progressDialog.setMessage(getString(getArguments().getInt(ARG_MESSAGE)));
			return progressDialog;
		}
		
		@Override
		public void onCancel(DialogInterface dialog) {
			super.onCancel(dialog);
			FragmentActivity activity = getActivity();
			if (activity == null) {
				return;
			}
			
			Fragment fragment = activity.getSupportFragmentManager().findFragmentByTag(TAG_PLUS_CLIENT);
			if (fragment instanceof PlusClientFragment) {
				((PlusClientFragment)fragment).onDialogCanceled(getTag());
			}
		}
		
		@Override
		public void onDismiss(DialogInterface dialog) {
			super.onDismiss(dialog);
			FragmentActivity activity = getActivity();
			if (activity == null) {
				return;
			}
			
			Fragment fragment = activity.getSupportFragmentManager().findFragmentByTag(TAG_PLUS_CLIENT);
			if (fragment instanceof PlusClientFragment) {
				((PlusClientFragment)fragment).onDialogDismissed(getTag());
			}
		}
	}
	
	protected void hideProgressDialog() {
		FragmentManager manager = getFragmentManager();
		if (manager != null) {
			DialogFragment progressDialog = (DialogFragment)manager.findFragmentByTag(TAG_PROGRESS_DIALOG);
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
		}
	}
	
	private void showErrorDialog(DialogFragment errorDialog) {
		DialogFragment oldErrorDialog = (DialogFragment)getFragmentManager().findFragmentByTag(TAG_ERROR_DIALOG);
		if (oldErrorDialog != null) {
			oldErrorDialog.dismiss();
		}
		
		errorDialog.show(getFragmentManager(), TAG_ERROR_DIALOG);
	}
	
	private boolean isShowingErrorDialog() {
		DialogFragment errorDialog = (DialogFragment)getFragmentManager().findFragmentByTag(TAG_ERROR_DIALOG);
		return (errorDialog != null) && !errorDialog.isHidden();
	}
	
	private void hideErrorDialog() {
		DialogFragment errorDialog = (DialogFragment)getFragmentManager().findFragmentByTag(TAG_ERROR_DIALOG);
		if (errorDialog != null) {
			errorDialog.dismiss();
		}
	}
	
	private void startResolution() {
		try {
			lastConnectionResult.startResolutionForResult(getActivity(), requestCode);
			hideProgressDialog();
		} catch (SendIntentException e) {
			// The intent we had is not valid right now, perhaps the remote process died.
			// Try to reconnect to get a new resolution intent.
			lastConnectionResult = null;
			showProgressDialog();
			connectPlusClient();
		}
	}
	
	public boolean handleOnActivityResult(int requestCode, int resultCode, Intent data) {
		if (this.requestCode != requestCode) {
			return false;
		}
		
		switch (resultCode) {
			case Activity.RESULT_OK:
				lastConnectionResult = null;
				connectPlusClient();
				break;
			case Activity.RESULT_CANCELED:
				// User canceled sign in, clear the request code.
				requestCode = INVALID_REQUEST_CODE;
				
				// Attempt to connect again.
				connectPlusClient();
				break;
		}
		return true;
	}
	
	/**
	 * Sign out of the app.
	 */
	public void signOut() {
		if (plusClient.isConnected()) {
			plusClient.clearDefaultAccount();
		}
		
		if (plusClient.isConnecting() || plusClient.isConnected()) {
			plusClient.disconnect();
			// Reconnect to get a new mPlusClient.
			lastConnectionResult = null;
			// Cancel sign in.
			requestCode = INVALID_REQUEST_CODE;
			
			// Reconnect to fetch the sign-in (account chooser) intent from the plus client.
			connectPlusClient();
		}
	}
	
	/**
	 * Revoke access to the current app.
	 */
	public void revokeAccessAndDisconnect() {
		if (plusClient.isConnected()) {
			plusClient.revokeAccessAndDisconnect(this);
		}
	}
	
	/**
	 * Attempts to connect the client to Google Play services if the client isn't already connected, and isn't in the
	 * process of being connected.
	 */
	private void connectPlusClient() {
		if (!plusClient.isConnecting() && !plusClient.isConnected()) {
			plusClient.connect();
		}
	}
}
