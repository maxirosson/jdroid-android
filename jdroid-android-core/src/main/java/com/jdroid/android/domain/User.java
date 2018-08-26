package com.jdroid.android.domain;

import java.util.concurrent.TimeUnit;

public interface User {

	public static final long PROFILE_PICTURE_TTL = TimeUnit.DAYS.toMillis(1);

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
