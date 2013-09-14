package com.jdroid.android.social.googleplus;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.PlusClient.OnAccessRevokedListener;
import com.google.android.gms.plus.PlusClient.OnPeopleLoadedListener;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.social.SocialUser;
import com.jdroid.android.social.SocialUser.SocialNetwork;
import com.jdroid.android.utils.GooglePlayUtils;
import com.jdroid.java.collections.Lists;

/**
 * @author Maxi Rosson
 */
public class GooglePlusHelperFragment extends AbstractFragment implements ConnectionCallbacks,
		OnConnectionFailedListener, OnAccessRevokedListener, OnPeopleLoadedListener {
	
	private static final int REQUEST_CODE_SIGN_IN = 1;
	
	public static void add(Activity activity, GooglePlusHelperFragment googlePlusHelperFragment, Fragment targetFragment) {
		if ((get(activity) == null) && GooglePlayUtils.isGooglePlayServicesAvailable(activity)) {
			googlePlusHelperFragment.setTargetFragment(targetFragment, 0);
			FragmentTransaction fragmentTransaction = ((AbstractFragmentActivity)activity).getSupportFragmentManager().beginTransaction();
			fragmentTransaction.add(0, googlePlusHelperFragment, GooglePlusHelperFragment.class.getSimpleName());
			fragmentTransaction.commit();
		}
	}
	
	public static GooglePlusHelperFragment get(Activity activity) {
		return ((AbstractFragmentActivity)activity).getFragment(GooglePlusHelperFragment.class);
	}
	
	private PlusClient plusClient;
	private ConnectionResult connectionResult;
	private GooglePlusAuthenticationUseCase googlePlusAuthenticationUseCase;
	
	/**
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		googlePlusAuthenticationUseCase = createGooglePlusAuthenticationUseCase();
		plusClient = new PlusClient.Builder(getActivity(), this, this).build();
	}
	
	protected GooglePlusAuthenticationUseCase createGooglePlusAuthenticationUseCase() {
		return null;
	}
	
	public GooglePlusAuthenticationUseCase getGooglePlusAuthenticationUseCase() {
		return googlePlusAuthenticationUseCase;
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onStart()
	 */
	@Override
	public void onStart() {
		super.onStart();
		plusClient.connect();
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		onResumeUseCase(googlePlusAuthenticationUseCase, this);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		onPauseUseCase(googlePlusAuthenticationUseCase, this);
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onStop()
	 */
	@Override
	public void onStop() {
		plusClient.disconnect();
		super.onStop();
	}
	
	private GooglePlusListener getGooglePlusListener() {
		return (GooglePlusListener)getTargetFragment();
	}
	
	/**
	 * @see com.google.android.gms.plus.PlusClient.OnAccessRevokedListener#onAccessRevoked(com.google.android.gms.common.ConnectionResult)
	 */
	@Override
	public void onAccessRevoked(ConnectionResult connectionResult) {
		plusClient.connect();
		getGooglePlusListener().onGooglePlusAccessRevoked();
	}
	
	/**
	 * @see com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener#onConnectionFailed(com.google.android.gms.common.ConnectionResult)
	 */
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		this.connectionResult = connectionResult;
		
		if (shouldDisconnectOnServer()) {
			googlePlusAuthenticationUseCase.setLoginMode(false);
			executeUseCase(googlePlusAuthenticationUseCase);
		}
		
		getGooglePlusListener().onGooglePlusConnectionFailed();
	}
	
	/**
	 * @see com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks#onConnected(android.os.Bundle)
	 */
	@Override
	public void onConnected(Bundle bundle) {
		
		Person me = plusClient.getCurrentPerson();
		
		if (shouldConnectOnServer(me)) {
			googlePlusAuthenticationUseCase.setGoogleUserId(me.getId());
			googlePlusAuthenticationUseCase.setLoginMode(true);
			executeUseCase(googlePlusAuthenticationUseCase);
		}
		
		getGooglePlusListener().onGooglePlusConnected(me);
	}
	
	private Boolean shouldConnectOnServer(Person me) {
		if ((me == null) || (googlePlusAuthenticationUseCase == null)) {
			return false;
		} else {
			// If the last authentication was successful for the same uer id, we avoid the request
			return !(googlePlusAuthenticationUseCase.isFinishSuccessful()
					&& googlePlusAuthenticationUseCase.isLoginMode() && googlePlusAuthenticationUseCase.getGoogleUserId().equals(
				me.getId()));
		}
	}
	
	private Boolean shouldDisconnectOnServer() {
		return (googlePlusAuthenticationUseCase != null)
				&& !(googlePlusAuthenticationUseCase.isFinishSuccessful() && !googlePlusAuthenticationUseCase.isLoginMode());
	}
	
	/**
	 * @see com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks#onDisconnected()
	 */
	@Override
	public void onDisconnected() {
		plusClient.connect();
		getGooglePlusListener().onGooglePlusDisconnected();
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onStartUseCase()
	 */
	@Override
	public void onStartUseCase() {
		// Do Nothing
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onFinishFailedUseCase(java.lang.RuntimeException)
	 */
	@Override
	public void onFinishFailedUseCase(RuntimeException runtimeException) {
		AbstractApplication.get().getExceptionHandler().logHandledException(runtimeException);
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_SIGN_IN) {
			if (resultCode == Activity.RESULT_OK) {
				if (!plusClient.isConnected() && !plusClient.isConnecting()) {
					plusClient.connect();
				}
			} else {
				getGooglePlusListener().onGooglePlusSignInCanceled();
			}
		}
	}
	
	/**
	 * @see com.google.android.gms.plus.PlusClient.OnPeopleLoadedListener#onPeopleLoaded(com.google.android.gms.common.ConnectionResult,
	 *      com.google.android.gms.plus.model.people.PersonBuffer, java.lang.String)
	 */
	@Override
	public void onPeopleLoaded(ConnectionResult status, PersonBuffer personBuffer, String nextPageToken) {
		if (status.getErrorCode() == ConnectionResult.SUCCESS) {
			
			List<SocialUser> googlePlusUsers = Lists.newArrayList();
			try {
				int count = personBuffer.getCount();
				for (int i = 0; i < count; i++) {
					Person person = personBuffer.get(i);
					googlePlusUsers.add(new SocialUser(null, person.getId(), SocialNetwork.GOOGLE_PLUS,
							person.getDisplayName(), "", person.getImage().getUrl()));
				}
				getGooglePlusListener().onGooglePlusFriendsLoaded(googlePlusUsers);
			} finally {
				personBuffer.close();
			}
		}
	}
	
	public void signIn() {
		try {
			connectionResult.startResolutionForResult(getActivity(), REQUEST_CODE_SIGN_IN);
		} catch (IntentSender.SendIntentException e) {
			// Fetch a new result to start.
			plusClient.connect();
		}
	}
	
	public void signOut() {
		if (plusClient.isConnected()) {
			plusClient.clearDefaultAccount();
			plusClient.disconnect();
			plusClient.connect();
		}
	}
	
	public void revokeAccess() {
		if (plusClient.isConnected()) {
			plusClient.revokeAccessAndDisconnect(this);
		}
	}
	
	public void loadPeople() {
		plusClient.loadVisiblePeople(this, Person.Collection.VISIBLE, null);
	}
	
	public static void revokeAccess(Context context) {
		if (GooglePlayUtils.isGooglePlayServicesAvailable(context)) {
			LogoutListener logoutListener = new LogoutListener();
			PlusClient plusClient = new PlusClient.Builder(context, logoutListener, logoutListener).build();
			logoutListener.setPlusClient(plusClient);
			plusClient.connect();
		}
	}
	
	public static class LogoutListener implements ConnectionCallbacks, OnConnectionFailedListener,
			OnAccessRevokedListener {
		
		private PlusClient plusClient;
		
		@Override
		public void onAccessRevoked(ConnectionResult arg0) {
		}
		
		@Override
		public void onConnectionFailed(ConnectionResult arg0) {
		}
		
		@Override
		public void onConnected(Bundle arg0) {
			plusClient.revokeAccessAndDisconnect(this);
		}
		
		@Override
		public void onDisconnected() {
		}
		
		public void setPlusClient(PlusClient plusClient) {
			this.plusClient = plusClient;
		}
	}
}
