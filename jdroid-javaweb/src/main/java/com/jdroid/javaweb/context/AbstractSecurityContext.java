package com.jdroid.javaweb.context;

import org.slf4j.Logger;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.javaweb.domain.Entity;
import com.jdroid.javaweb.exception.InvalidAuthenticationException;

/**
 * A context holding per session values
 * 
 * @param <T>
 */
public abstract class AbstractSecurityContext<T extends Entity> {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(AbstractSecurityContext.class);
	
	private T user;
	
	/**
	 * @return the logged user
	 */
	public T getUser() {
		return user;
	}
	
	/**
	 * @param email The user's email
	 * @param password The user's password
	 * @return The User
	 * @throws InvalidAuthenticationException Thrown if the email and/or the password are invalid
	 */
	public T authenticateUser(String email, String password) throws InvalidAuthenticationException {
		user = verifyPassword(email, password);
		LOGGER.info("User [id: " + user.getId() + ", email: " + email + "] authenticated.");
		return user;
	}
	
	public T authenticateUser(String userToken) throws InvalidAuthenticationException {
		user = verifyToken(userToken);
		LOGGER.info("User [id: " + user.getId() + ", userToken: " + userToken + "] authenticated.");
		return user;
	}
	
	/**
	 * @param username The email to verify
	 * @param password The password to verify
	 * @return The user
	 * @throws InvalidAuthenticationException Thrown if the email and/or the password are invalid
	 */
	protected abstract T verifyPassword(String email, String password) throws InvalidAuthenticationException;
	
	/**
	 * @param userToken The userToken to verify
	 * @return The user
	 * @throws InvalidAuthenticationException Thrown if the userToken is invalid
	 */
	protected abstract T verifyToken(String userToken) throws InvalidAuthenticationException;
	
	public void invalidate() {
		this.user = null;
	}
	
	public Boolean isAuthenticated() {
		return user != null;
	}
}
