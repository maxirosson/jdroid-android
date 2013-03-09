package com.jdroid.android.facebook;

import android.content.Context;
import com.facebook.Session;
import com.facebook.Session.Builder;
import com.facebook.SharedPreferencesTokenCachingStrategy;
import com.facebook.TokenCachingStrategy;
import com.jdroid.android.context.SecurityContext;

/**
 * An implementation of {@link TokenCachingStrategy TokenCachingStrategy} that uses Android SharedPreferences to persist
 * information, using the user id from the {@link SecurityContext} as key in the SharedPreferences. This enables the
 * possibility to cache multiple users' tokens.
 * 
 * @author Estefanía Caravatti
 */
public class MultipleUsersSharedPreferencesTokenCachingStrategy extends SharedPreferencesTokenCachingStrategy {
	
	/**
	 * @param context The {@link Context} to use.
	 */
	public MultipleUsersSharedPreferencesTokenCachingStrategy(Context context) {
		super(context, SecurityContext.get().getUser().getId().toString());
	}
	
	/**
	 * Builds a {@link Session} using this {@link TokenCachingStrategy} and with the given Facebook app id. The created
	 * session is set as the active one.
	 * 
	 * @param context
	 * @param fabebookAppId
	 */
	public void buildActiveSession(Context context, String fabebookAppId) {
		Session session = new Builder(context).setTokenCachingStrategy(this).setApplicationId(fabebookAppId).build();
		Session.setActiveSession(session);
	}
	
}
