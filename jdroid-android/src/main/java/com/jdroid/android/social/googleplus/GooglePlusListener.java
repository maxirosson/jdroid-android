package com.jdroid.android.social.googleplus;

import java.util.List;
import com.google.android.gms.plus.model.people.Person;
import com.jdroid.android.social.SocialUser;

/**
 * 
 * @author Maxi Rosson
 */
public interface GooglePlusListener {
	
	public void onSignInCanceled();
	
	public void onAccessRevoked();
	
	public void onConnectionFailed();
	
	public void onConnected(Person me);
	
	public void onDisconnected();
	
	public void onPeopleLoaded(List<SocialUser> friends);
}
