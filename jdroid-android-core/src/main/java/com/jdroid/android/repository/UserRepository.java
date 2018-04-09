package com.jdroid.android.repository;

import com.jdroid.android.domain.User;

/**
 * {@link User} repository
 */
public interface UserRepository {
	
	/**
	 * @return The logged {@link User} of the application. Returns null if there is no {@link User} logged
	 */
	public User getUser();
	
	/**
	 * @param user The logged {@link User} to save
	 */
	public void saveUser(User user);
	
	/**
	 * Remove the {@link User} from the repository
	 */
	public void removeUser();
}
