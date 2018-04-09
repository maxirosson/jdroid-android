package com.jdroid.android.domain;

import com.jdroid.java.date.DateUtils;

public interface User {
	
	public static final long PROFILE_PICTURE_TTL = DateUtils.MILLIS_PER_DAY;
	
	public Long getId();
	
	public String getUserName();
	
	public String getFullname();
	
	public String getEmail();
	
	public String getFirstName();
	
	public String getLastName();
	
	public String getUserToken();
	
	public String getProfilePictureUrl();
	
	public String getCoverPictureUrl();
	
}
