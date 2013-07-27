package com.jdroid.javaweb.facebook;

import com.restfb.Facebook;

public class FacebookUser {
	
	@Facebook("uid")
	private String facebookId;
	
	@Facebook("first_name")
	private String firstName;
	
	@Facebook("last_name")
	private String lastName;
	
	@Facebook("is_app_user")
	private Boolean isAppUser;
	
	/**
	 * @return the facebookId
	 */
	public String getFacebookId() {
		return facebookId;
	}
	
	/**
	 * @param facebookId the facebookId to set
	 */
	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}
	
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	/**
	 * @return the isAppUser
	 */
	public Boolean isAppUser() {
		return isAppUser;
	}
	
	/**
	 * @param isAppUser the isAppUser to set
	 */
	public void setIsAppUser(Boolean isAppUser) {
		this.isAppUser = isAppUser;
	}
	
}
