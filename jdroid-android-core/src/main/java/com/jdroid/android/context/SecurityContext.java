package com.jdroid.android.context;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.domain.User;

public class SecurityContext {
	
	private static final SecurityContext INSTANCE = new SecurityContext();
	
	private User user;
	
	/**
	 * @return The {@link SecurityContext} instance
	 */
	public static SecurityContext get() {
		return INSTANCE;
	}
	
	private SecurityContext() {
		if (AbstractApplication.get().getUserRepository() != null) {
			user = AbstractApplication.get().getUserRepository().getUser();
		}
	}
	
	public void attach(User user) {
		this.user = user;
		AbstractApplication.get().getUserRepository().saveUser(user);
	}
	
	public void detachUser() {
		AbstractApplication.get().getUserRepository().removeUser();
		user = null;
	}
	
	/**
	 * @return The user logged in the application
	 */
	public User getUser() {
		return user;
	}
	
	/**
	 * @return Whether the user is authenticated or not
	 */
	public Boolean isAuthenticated() {
		return user != null;
	}
}
