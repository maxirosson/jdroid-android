package com.jdroid.android.social.googleplus;

import java.util.List;
import com.google.android.gms.plus.model.people.Person;
import com.jdroid.android.social.SocialUser;

/**
 * 
 * @author Maxi Rosson
 */
public interface GooglePlusListener {
	
	public void onGooglePlusSignInCanceled();
	
	public void onGooglePlusAccessRevoked();
	
	public void onGooglePlusConnectionFailed();
	
	public void onGooglePlusConnected(Person me);
	
	public void onGooglePlusDisconnected();
	
	public void onGooglePlusFriendsLoaded(List<SocialUser> friends);
}
